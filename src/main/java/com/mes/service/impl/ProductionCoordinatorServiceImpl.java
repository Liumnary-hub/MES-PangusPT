package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mes.config.WmsProperties;
import com.mes.dto.ProductionOrderDetail;
import com.mes.entity.ProductionOrder;
import com.mes.entity.ProductionTask;
import com.mes.integration.wms.WmsInboundRequest;
import com.mes.integration.wms.WmsInventoryClient;
import com.mes.mapper.ProductionOrderMapper;
import com.mes.mapper.ProductionTaskMapper;
import com.mes.service.ProductionCoordinatorService;
import com.mes.service.ProductionOrderService;
import com.mes.service.ProductionTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductionCoordinatorServiceImpl implements ProductionCoordinatorService {

    @Autowired
    private ProductionOrderService productionOrderService;

    @Autowired
    private ProductionTaskService productionTaskService;

    @Autowired
    private ProductionOrderMapper productionOrderMapper;

    @Autowired
    private ProductionTaskMapper productionTaskMapper;

    @Autowired
    private WmsInventoryClient wmsInventoryClient;

    @Autowired
    private WmsProperties wmsProperties;

    @Override
    @Transactional
    public boolean planOrder(Long orderId) {
        log.info("开始完成工单计划, orderId: {}", orderId);

        try {
            ProductionOrder order = productionOrderService.getById(orderId);
            if (order == null) {
                throw new RuntimeException("工单不存在");
            }

            // 验证状态
            if (!"draft".equals(order.getStatus())) {
                throw new RuntimeException("只有草稿状态的工单可以完成计划");
            }

            // 验证必要信息
            if (order.getPlannedQuantity() == null || order.getPlannedQuantity() <= 0) {
                throw new RuntimeException("计划数量必须大于0");
            }
            if (order.getProductCode() == null || order.getProductCode().trim().isEmpty()) {
                throw new RuntimeException("产品编码不能为空");
            }

            // 更新状态
            order.setStatus("planned");
            boolean success = productionOrderService.updateById(order);

            if (success) {
                log.info("工单计划完成成功, orderId: {}, orderCode: {}", orderId, order.getOrderCode());
            }

            return success;

        } catch (Exception e) {
            log.error("完成工单计划失败, orderId: {}", orderId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean releaseOrderWithTasks(Long orderId) {
        log.info("开始下发工单并生成任务, orderId: {}", orderId);

        try {
            ProductionOrder order = productionOrderService.getById(orderId);
            if (order == null) {
                throw new RuntimeException("工单不存在");
            }

            // 验证状态
            if (!"planned".equals(order.getStatus())) {
                throw new RuntimeException("只有已计划状态的工单可以下发");
            }

            // 生成生产任务
            boolean tasksCreated = productionTaskService.createTasksFromOrder(orderId);
            if (!tasksCreated) {
                throw new RuntimeException("生成生产任务失败");
            }

            // 更新工单状态
            order.setStatus("released");
            boolean orderUpdated = productionOrderService.updateById(order);

            if (orderUpdated) {
                log.info("工单下发成功, orderId: {}, orderCode: {}", orderId, order.getOrderCode());
            }

            return orderUpdated;

        } catch (Exception e) {
            log.error("下发工单失败, orderId: {}", orderId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long orderId) {
        log.info("开始取消工单, orderId: {}", orderId);

        try {
            ProductionOrder order = productionOrderService.getById(orderId);
            if (order == null) {
                throw new RuntimeException("工单不存在");
            }

            // 验证状态
            if (!"draft".equals(order.getStatus()) && !"planned".equals(order.getStatus())) {
                throw new RuntimeException("只有草稿和已计划状态的工单可以取消");
            }

            // 更新工单状态
            order.setStatus("cancelled");
            boolean success = productionOrderService.updateById(order);

            if (success) {
                log.info("工单取消成功, orderId: {}, orderCode: {}", orderId, order.getOrderCode());
            }

            return success;

        } catch (Exception e) {
            log.error("取消工单失败, orderId: {}", orderId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean startTaskWithOrderSync(Long taskId, String operatorId, String operatorName) {
        log.info("开始任务并同步工单状态, taskId: {}, operator: {}", taskId, operatorId);

        try {
            ProductionTask task = productionTaskService.getById(taskId);
            if (task == null) {
                throw new RuntimeException("任务不存在");
            }

            // 验证任务状态
            if (!"pending".equals(task.getStatus())) {
                throw new RuntimeException("只有待执行状态的任务可以开始");
            }

            // 更新任务状态
            task.setStatus("in_progress");
            task.setOperatorId(operatorId);
            task.setOperatorName(operatorName);
            task.setActualStartTime(LocalDateTime.now());

            boolean taskUpdated = productionTaskService.updateById(task);
            if (!taskUpdated) {
                throw new RuntimeException("更新任务状态失败");
            }

            // 同步工单状态
            syncOrderStatus(task.getOrderId());

            log.info("任务开始成功, taskId: {}, orderId: {}", taskId, task.getOrderId());
            return true;

        } catch (Exception e) {
            log.error("开始任务失败, taskId: {}", taskId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean completeTaskWithOrderSync(Long taskId, Integer completedQuantity, Integer qualifiedQuantity) {
        log.info("完成任务并同步工单状态, taskId: {}, completed: {}, qualified: {}",
                taskId, completedQuantity, qualifiedQuantity);

        try {
            ProductionTask task = productionTaskService.getById(taskId);
            if (task == null) {
                throw new RuntimeException("任务不存在");
            }

            // 验证任务状态
            if (!"in_progress".equals(task.getStatus())) {
                throw new RuntimeException("只有执行中状态的任务可以完成");
            }

            // 验证数量
            if (completedQuantity > task.getPlannedQuantity()) {
                throw new RuntimeException("完成数量不能超过计划数量");
            }
            if (qualifiedQuantity > completedQuantity) {
                throw new RuntimeException("合格数量不能超过完成数量");
            }

            // 更新任务状态
            task.setStatus("completed");
            task.setCompletedQuantity(completedQuantity);
            task.setQualifiedQuantity(qualifiedQuantity);
            task.setActualEndTime(LocalDateTime.now());

            // 计算实际工时
            if (task.getActualStartTime() != null) {
                Duration duration = Duration.between(task.getActualStartTime(), task.getActualEndTime());
                double actualHours = duration.toHours() + duration.toMinutesPart() / 60.0;
                task.setActualTime(actualHours);
            }

            boolean taskUpdated = productionTaskService.updateById(task);
            if (!taskUpdated) {
                throw new RuntimeException("更新任务状态失败");
            }

            // 同步工单状态
            syncOrderStatus(task.getOrderId());

            log.info("任务完成成功, taskId: {}, orderId: {}", taskId, task.getOrderId());
            return true;

        } catch (Exception e) {
            log.error("完成任务失败, taskId: {}", taskId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean updateTaskProgressWithOrderSync(Long taskId, Integer completedQuantity, Integer qualifiedQuantity, Double actualTime) {
        log.info("更新任务进度并同步工单状态, taskId: {}, completed: {}, qualified: {}, actualTime: {}",
                taskId, completedQuantity, qualifiedQuantity, actualTime);

        try {
            ProductionTask task = productionTaskService.getById(taskId);
            if (task == null) {
                throw new RuntimeException("任务不存在");
            }

            // 验证任务状态
            if (!"in_progress".equals(task.getStatus())) {
                throw new RuntimeException("只有执行中状态的任务可以更新进度");
            }

            // 验证数量
            if (completedQuantity > task.getPlannedQuantity()) {
                throw new RuntimeException("完成数量不能超过计划数量");
            }
            if (qualifiedQuantity > completedQuantity) {
                throw new RuntimeException("合格数量不能超过完成数量");
            }

            // 更新任务进度
            boolean progressUpdated = productionTaskService.updateTaskProgress(taskId, completedQuantity, qualifiedQuantity, actualTime);
            if (!progressUpdated) {
                throw new RuntimeException("更新任务进度失败");
            }

            // 同步工单状态
            syncOrderStatus(task.getOrderId());

            log.info("任务进度更新成功, taskId: {}, orderId: {}", taskId, task.getOrderId());
            return true;

        } catch (Exception e) {
            log.error("更新任务进度失败, taskId: {}", taskId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean pauseTask(Long taskId) {
        log.info("暂停任务, taskId: {}", taskId);

        try {
            ProductionTask task = productionTaskService.getById(taskId);
            if (task == null) {
                throw new RuntimeException("任务不存在");
            }

            if (!"in_progress".equals(task.getStatus())) {
                throw new RuntimeException("只有执行中状态的任务可以暂停");
            }

            task.setStatus("paused");
            boolean success = productionTaskService.updateById(task);

            if (success) {
                log.info("任务暂停成功, taskId: {}", taskId);
            }

            return success;

        } catch (Exception e) {
            log.error("暂停任务失败, taskId: {}", taskId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean resumeTask(Long taskId) {
        log.info("恢复任务, taskId: {}", taskId);

        try {
            ProductionTask task = productionTaskService.getById(taskId);
            if (task == null) {
                throw new RuntimeException("任务不存在");
            }

            if (!"paused".equals(task.getStatus())) {
                throw new RuntimeException("只有暂停状态的任务可以恢复");
            }

            task.setStatus("in_progress");
            boolean success = productionTaskService.updateById(task);

            if (success) {
                log.info("任务恢复成功, taskId: {}", taskId);
            }

            return success;

        } catch (Exception e) {
            log.error("恢复任务失败, taskId: {}", taskId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void syncOrderStatus(Long orderId) {
        log.debug("同步工单状态, orderId: {}", orderId);

        try {
            ProductionOrder order = productionOrderService.getById(orderId);
            if (order == null) {
                log.warn("工单不存在, orderId: {}", orderId);
                return;
            }

            // 如果工单已经是完成或取消状态，不需要同步状态，但仍需要更新完成数量
            boolean shouldUpdate = false;
            String oldStatus = order.getStatus();
            String newStatus = oldStatus;

            // 获取工单的所有任务
            List<ProductionTask> tasks = productionTaskMapper.selectByOrderId(orderId);
            if (tasks.isEmpty()) {
                log.debug("工单没有任务, 保持当前状态, orderId: {}", orderId);
                // 即使没有任务，也要确保完成数量为0
                if (order.getCompletedQuantity() == null || order.getCompletedQuantity() > 0) {
                    order.setCompletedQuantity(0);
                    shouldUpdate = true;
                }
            } else {
                // 统计任务状态
                Map<String, Long> statusCount = tasks.stream()
                        .collect(Collectors.groupingBy(ProductionTask::getStatus, Collectors.counting()));

                long totalTasks = tasks.size();
                long completedTasks = statusCount.getOrDefault("completed", 0L);
                long inProgressTasks = statusCount.getOrDefault("in_progress", 0L);
                long pausedTasks = statusCount.getOrDefault("paused", 0L);

                // 确定新的工单状态
                newStatus = determineOrderStatus(totalTasks, completedTasks, inProgressTasks, pausedTasks);

                // 更新工单完成数量（无论状态是否改变）
                updateOrderCompletedQuantity(order, tasks);

                // 如果状态有变化，更新工单状态
                if (!oldStatus.equals(newStatus)) {
                    order.setStatus(newStatus);
                    // 设置实际开始和结束时间
                    updateOrderTimestamps(order, newStatus);
                    shouldUpdate = true;
                    log.info("工单状态同步完成, orderId: {}, 旧状态: {}, 新状态: {}",
                            orderId, oldStatus, newStatus);
                } else {
                    // 即使状态没有变化，但完成数量可能变化了，也需要更新
                    shouldUpdate = true;
                }
            }

            // 如果需要更新，保存工单
            if (shouldUpdate) {
                productionOrderService.updateById(order);
                if ("completed".equals(newStatus) && !"completed".equals(oldStatus)) {
                    syncFinishedGoodsInbound(order);
                }
                log.debug("工单更新完成, orderId: {}, 完成数量: {}", orderId, order.getCompletedQuantity());
            }

        } catch (Exception e) {
            log.error("同步工单状态失败, orderId: {}", orderId, e);
            throw new RuntimeException("同步工单状态失败", e);
        }
    }

    @Override
    public ProductionOrderDetail getOrderDetail(Long orderId) {
        log.debug("获取工单详情, orderId: {}", orderId);

        try {
            ProductionOrder order = productionOrderService.getById(orderId);
            if (order == null) {
                return null;
            }

            // 获取关联的任务列表
            List<ProductionTask> tasks = productionTaskMapper.selectByOrderId(orderId);

            // 计算进度
            Double progress = calculateOrderProgress(tasks);

            // 统计任务状态
            Map<String, Long> taskStatusCount = tasks.stream()
                    .collect(Collectors.groupingBy(ProductionTask::getStatus, Collectors.counting()));

            // 构建详情对象
            ProductionOrderDetail detail = new ProductionOrderDetail();
            BeanUtils.copyProperties(order, detail);
            detail.setTasks(tasks);
            detail.setProgress(progress);
            detail.setTaskStatusCount(taskStatusCount);

            return detail;

        } catch (Exception e) {
            log.error("获取工单详情失败, orderId: {}", orderId, e);
            throw new RuntimeException("获取工单详情失败", e);
        }
    }

    @Override
    public Double getOrderProgress(Long orderId) {
        log.debug("获取工单进度, orderId: {}", orderId);

        try {
            List<ProductionTask> tasks = productionTaskMapper.selectByOrderId(orderId);
            return calculateOrderProgress(tasks);

        } catch (Exception e) {
            log.error("获取工单进度失败, orderId: {}", orderId, e);
            throw new RuntimeException("获取工单进度失败", e);
        }
    }

    /**
     * 工单首次完成时，把成品推入 WMS 成品仓。
     * requestId 固定，重复调用由 WMS 幂等。
     */
    private void syncFinishedGoodsInbound(ProductionOrder order) {
        String warehouseCode = wmsProperties.getFinishedGoodsWarehouseCode();
        if (!StringUtils.hasText(warehouseCode)) {
            log.warn("未配置成品仓编码，跳过完工入库, orderId: {}", order.getId());
            return;
        }
        if (!StringUtils.hasText(order.getProductCode())) {
            throw new RuntimeException("工单产品编码为空，无法完工入库");
        }

        Integer quantity = order.getCompletedQuantity();
        if (quantity == null || quantity <= 0) {
            log.warn("工单完成数量为0，跳过完工入库, orderId: {}", order.getId());
            return;
        }

        WmsInboundRequest request = new WmsInboundRequest();
        request.setRequestId("MES-ORDER-" + order.getId() + "-FINISHED-IN");
        request.setCommodityCode(order.getProductCode());
        request.setWarehouseCode(warehouseCode);
        request.setQuantity(quantity);
        request.setBatchNo(order.getOrderCode());
        request.setSourceType("MES_PRODUCTION_ORDER");
        request.setSourceNo(order.getOrderCode());
        request.setSourceItemNo(String.valueOf(order.getId()));
        request.setDescription("MES完工入库 " + order.getOrderCode());
        wmsInventoryClient.inbound(request);
    }

    // ========== 私有方法 ==========

    /**
     * 确定工单状态
     */
    private String determineOrderStatus(long totalTasks, long completedTasks, long inProgressTasks, long pausedTasks) {
        if (completedTasks == totalTasks) {
            // 所有任务完成
            return "completed";
        } else if (inProgressTasks > 0 || completedTasks > 0) {
            // 有任务在执行或已完成
            return "in_progress";
        } else if (pausedTasks > 0) {
            // 有任务暂停
            return "in_progress"; // 暂停也算在执行中
        } else {
            // 所有任务都待执行
            return "released";
        }
    }

    /**
     * 更新工单时间戳
     */
    private void updateOrderTimestamps(ProductionOrder order, String newStatus) {
        if ("in_progress".equals(newStatus) && order.getActualStartTime() == null) {
            order.setActualStartTime(LocalDateTime.now());
        }
        if ("completed".equals(newStatus)) {
            order.setActualEndTime(LocalDateTime.now());
        }
    }

    /**
     * 更新工单完成数量
     */
    /**
     * 更新工单完成数量 - 使用累计值
     */
    private void updateOrderCompletedQuantity(ProductionOrder order, List<ProductionTask> tasks) {
        if (tasks.isEmpty()) {
            order.setCompletedQuantity(0);
            log.debug("工单没有任务，设置完成数量为0");
            return;
        }

        // ✅ 使用所有任务完成数量的累计值
        Integer totalCompleted = tasks.stream()
                .mapToInt(ProductionTask::getCompletedQuantity)
                .sum();

        // 记录更新前的完成数量，用于日志
        Integer oldCompletedQuantity = order.getCompletedQuantity();

        // 更新工单完成数量，但不能超过计划数量
        Integer finalCompletedQuantity = Math.min(totalCompleted, order.getPlannedQuantity());
        order.setCompletedQuantity(finalCompletedQuantity);

        log.debug("更新工单完成数量: 工单 {}, 原完成数量: {}, 新完成数量: {}, 累计完成: {}",
                order.getOrderCode(), oldCompletedQuantity, finalCompletedQuantity, totalCompleted);
    }

    /**
     * 计算工单进度
     */
    /**
     * 计算工单进度
     */
    private Double calculateOrderProgress(List<ProductionTask> tasks) {
        if (tasks.isEmpty()) {
            return 0.0;
        }

        // ✅ 使用累计完成数量计算进度
        Integer totalCompleted = tasks.stream()
                .mapToInt(ProductionTask::getCompletedQuantity)
                .sum();

        Integer totalPlanned = tasks.stream()
                .mapToInt(ProductionTask::getPlannedQuantity)
                .sum();

        if (totalPlanned == 0) {
            return 0.0;
        }

        double progress = (double) totalCompleted / totalPlanned * 100;
        return Math.round(progress * 100.0) / 100.0; // 保留两位小数
    }
}
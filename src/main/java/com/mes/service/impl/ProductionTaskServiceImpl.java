package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.ProductionOrder;
import com.mes.entity.ProductionTask;
import com.mes.mapper.ProductionTaskMapper;
import com.mes.service.ProductionOrderService;
import com.mes.service.ProductionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductionTaskServiceImpl extends ServiceImpl<ProductionTaskMapper, ProductionTask>
        implements ProductionTaskService {
    @Autowired
    private ProductionOrderService productionOrderService;

    @Override
    @Transactional
    public boolean createTasksFromOrder(Long orderId) {
        try {
            // 根据orderId查询工单，获取产品信息
            ProductionOrder order = productionOrderService.getById(orderId);
            if (order == null) {
                throw new RuntimeException("工单不存在，orderId: " + orderId);
            }

            // 创建生产任务
            ProductionTask task = new ProductionTask();
            task.setTaskCode("TK" + System.currentTimeMillis());
            task.setOrderId(orderId);
            task.setProductId(order.getProductId());
            task.setResourceId(1L);
            task.setOperationSequence(1);
            task.setOperationName("加工工序");
            task.setPlannedQuantity(order.getPlannedQuantity());
            task.setCompletedQuantity(0);
            task.setQualifiedQuantity(0);
            task.setActualTime(0.0);
            task.setStatus("pending");
            task.setQualityStatus("pending");
            task.setCreateTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());

            return save(task);

        } catch (Exception e) {

            throw new RuntimeException("创建生产任务失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean startTask(Long taskId, String operatorId) {
        ProductionTask task = getById(taskId);
        if (task != null && "pending".equals(task.getStatus())) {
            task.setStatus("in_progress");
            task.setOperatorId(operatorId);
            task.setActualStartTime(LocalDateTime.now());
            return updateById(task);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean completeTask(Long taskId, Integer completedQuantity, Integer qualifiedQuantity) {
        ProductionTask task = getById(taskId);
        if (task != null && "in_progress".equals(task.getStatus())) {
            task.setStatus("completed");
            task.setCompletedQuantity(completedQuantity);
            task.setQualifiedQuantity(qualifiedQuantity);
            task.setActualEndTime(LocalDateTime.now());

            // 计算实际工时
            if (task.getActualStartTime() != null) {
                long hours = java.time.Duration.between(task.getActualStartTime(), LocalDateTime.now()).toHours();
                task.setActualTime((double) hours);
            }

            return updateById(task);
        }
        return false;
    }

    @Override
    public List<ProductionTask> getTasksByResource(Long resourceId) {
        return baseMapper.selectByResourceId(resourceId);
    }

    @Override
    public List<ProductionTask> getPendingTasks(Long resourceId) {
        return baseMapper.selectPendingTasksByResource(resourceId);
    }

    @Override
    @Transactional
    public boolean updateTaskProgress(Long taskId, Integer completedQuantity, Integer qualifiedQuantity, Double actualTime) {
        return baseMapper.updateTaskProgress(taskId, completedQuantity, qualifiedQuantity, actualTime) > 0;
    }

    @Override
    public IPage<ProductionTask> getTasksByCondition(Page<ProductionTask> page, String taskCode, Long orderId, Long resourceId, String status) {
        LambdaQueryWrapper<ProductionTask> queryWrapper = new LambdaQueryWrapper<>();

        // 构建查询条件
        if (taskCode != null && !taskCode.trim().isEmpty()) {
            queryWrapper.like(ProductionTask::getTaskCode, taskCode);
        }
        if (orderId != null) {
            queryWrapper.eq(ProductionTask::getOrderId, orderId);
        }
        if (resourceId != null) {
            queryWrapper.eq(ProductionTask::getResourceId, resourceId);
        }
        if (status != null && !status.trim().isEmpty()) {
            queryWrapper.eq(ProductionTask::getStatus, status);
        }

        // 设置排序规则：按工序顺序和计划开始时间排序
        queryWrapper.orderByAsc(ProductionTask::getOperationSequence)
                .orderByAsc(ProductionTask::getPlannedStartTime)
                .orderByDesc(ProductionTask::getCreateTime);

        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<ProductionTask> getTasksByOrder(Long orderId) {
        LambdaQueryWrapper<ProductionTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductionTask::getOrderId, orderId)
                .orderByAsc(ProductionTask::getOperationSequence)
                .orderByAsc(ProductionTask::getPlannedStartTime);
        return baseMapper.selectList(queryWrapper);
    }


}
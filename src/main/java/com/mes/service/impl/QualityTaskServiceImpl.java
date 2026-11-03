package com.mes.service.impl;// QualityTaskServiceImpl.java - 实现


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.mes.common.BusinessException;
import com.mes.common.PageResult;


import com.mes.dto.TaskQueryDTO;
import com.mes.dto.TaskSubmitDTO;
import com.mes.dto.TaskAssignDTO;
import com.mes.entity.QualityTask;
import com.mes.entity.QualityRecord;
import com.mes.entity.DefectDetail;
import com.mes.mapper.QualityTaskMapper;
import com.mes.mapper.QualityRecordMapper;
import com.mes.mapper.DefectDetailMapper;
import com.mes.service.QualityTaskService;
import com.mes.vo.TaskDetailVO;
import com.mes.vo.TaskStatisticVO;
import com.mes.vo.TaskVO;
import com.mes.vo.RealTimeStatisticVO;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QualityTaskServiceImpl implements QualityTaskService {

    private final QualityTaskMapper taskMapper;
    private final QualityRecordMapper recordMapper;
    private final DefectDetailMapper defectDetailMapper;


    @Override
    public PageResult<TaskVO> getTaskList(TaskQueryDTO query) {
        try {
            // 验证查询参数
            validateQuery(query);

            // 构建分页查询
            Page<QualityTask> page = new Page<>(query.getPageNum(), query.getPageSize());
            IPage<QualityTask> taskPage = taskMapper.selectPageList(page, query);

            // 转换为VO列表
            List<TaskVO> voList = taskPage.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

            // 构建分页结果
            return PageResult.of(voList, taskPage.getTotal(),
                    query.getPageNum(), query.getPageSize());

        } catch (Exception e) {
            log.error("查询检验任务列表失败", e);
            throw new BusinessException("查询检验任务列表失败: " + e.getMessage());
        }
    }

    @Override
    public TaskDetailVO getTaskDetail(Long id) {
        try {
            QualityTask task = taskMapper.selectById(id);
            if (task == null) {
                throw new BusinessException("检验任务不存在");
            }

            TaskDetailVO detail = new TaskDetailVO();
            BeanUtils.copyProperties(task, detail);

            // 查询检验记录
            List<QualityRecord> records = recordMapper.selectByTaskId(id);
            detail.setRecords(records);

            // 查询缺陷明细
            List<DefectDetail> defects = defectDetailMapper.selectByTaskId(id);
            detail.setDefects(defects);

            // 计算合格率
            if (task.getQuantityChecked() != null && task.getQuantityChecked() > 0) {
                double passRate = (double) task.getQuantityQualified() / task.getQuantityChecked() * 100;
                detail.setPassRate(passRate);
            }

            return detail;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取检验任务详情失败，ID: {}", id, e);
            throw new BusinessException("获取检验任务详情失败: " + e.getMessage());
        }
    }

    @Override
    public Long createTask(QualityTask task) {
        try {
            validateTask(task);

            // 生成任务编号
            if (!StringUtils.hasText(task.getTaskNo())) {
                task.setTaskNo(generateTaskNo());
            }

            // 设置默认值
            if (task.getStatus() == null) {
                task.setStatus("PENDING");
            }
            if (task.getCreateTime() == null) {
                task.setCreateTime(LocalDateTime.now());
            }
            task.setUpdateTime(LocalDateTime.now());
            task.setDeleted(0);

            taskMapper.insert(task);

            log.info("创建检验任务成功，任务编号: {}", task.getTaskNo());
            return task.getId();

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建检验任务失败", e);
            throw new BusinessException("创建检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void updateTask(QualityTask task) {
        try {
            validateTask(task);

            if (task.getId() == null) {
                throw new ValidationException("任务ID不能为空");
            }

            QualityTask existingTask = taskMapper.selectById(task.getId());
            if (existingTask == null) {
                throw new BusinessException("检验任务不存在");
            }

            // 已提交的任务不能修改
            if ("COMPLETED".equals(existingTask.getStatus()) ||
                    "SUBMITTED".equals(existingTask.getStatus())) {
                throw new BusinessException("已提交的任务不能修改");
            }

            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);

            log.info("更新检验任务成功，任务ID: {}", task.getId());

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新检验任务失败，ID: {}", task.getId(), e);
            throw new BusinessException("更新检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteTask(Long id) {
        try {
            QualityTask task = taskMapper.selectById(id);
            if (task == null) {
                throw new BusinessException("检验任务不存在");
            }

            // 已开始的任务不能删除
            if (!"PENDING".equals(task.getStatus())) {
                throw new BusinessException("已开始的任务不能删除");
            }

            // 逻辑删除
            task.setDeleted(1);
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);

            log.info("删除检验任务成功，任务ID: {}", id);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除检验任务失败，ID: {}", id, e);
            throw new BusinessException("删除检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        try {
            if (CollectionUtils.isEmpty(ids)) {
                throw new ValidationException("请选择要删除的任务");
            }

            // 检查每个任务是否可删除
            List<QualityTask> tasks = taskMapper.selectBatchIds(ids);
            for (QualityTask task : tasks) {
                if (!"PENDING".equals(task.getStatus())) {
                    throw new BusinessException("任务 " + task.getTaskNo() + " 已开始，不能删除");
                }
            }

            // 批量逻辑删除
            taskMapper.deleteBatchByIds(ids);

            log.info("批量删除检验任务成功，数量: {}", ids.size());

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量删除检验任务失败", e);
            throw new BusinessException("批量删除检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void startTask(Long taskId, String inspector) {
        try {
            if (!StringUtils.hasText(inspector)) {
                throw new ValidationException("检验员不能为空");
            }

            QualityTask task = taskMapper.selectById(taskId);
            if (task == null) {
                throw new BusinessException("检验任务不存在");
            }

            // 只能开始待检任务
            if (!"PENDING".equals(task.getStatus())) {
                throw new BusinessException("只有待检状态的任务可以开始");
            }

            // 更新任务状态
            task.setInspector(inspector);
            task.setStatus("INSPECTING");
            task.setInspectionDate(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);

            log.info("开始检验任务成功，任务ID: {}，检验员: {}", taskId, inspector);

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("开始检验任务失败，ID: {}", taskId, e);
            throw new BusinessException("开始检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void submitTask(TaskSubmitDTO dto) {
        try {
            validateSubmitDTO(dto);

            QualityTask task = taskMapper.selectById(dto.getTaskId());
            if (task == null) {
                throw new BusinessException("检验任务不存在");
            }

            // 只能提交检验中的任务
            if (!"INSPECTING".equals(task.getStatus())) {
                throw new BusinessException("只有检验中的任务可以提交");
            }

            // 验证数据一致性
            if (dto.getQuantityChecked() != null) {
                if (dto.getQuantityQualified() == null) dto.setQuantityQualified(0);
                if (dto.getQuantityDefective() == null) dto.setQuantityDefective(0);

                if (!dto.getQuantityChecked().equals(dto.getQuantityQualified() + dto.getQuantityDefective())) {
                    throw new ValidationException("检验数量必须等于合格数量加缺陷数量");
                }
            }

            // 更新任务信息
            task.setQuantityChecked(dto.getQuantityChecked());
            task.setQuantityQualified(dto.getQuantityQualified());
            task.setQuantityDefective(dto.getQuantityDefective());
            task.setResult(dto.getResult());
            task.setStatus("COMPLETED");
            task.setCompleteTime(LocalDateTime.now());
            task.setRemark(dto.getRemark());
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);

            // 保存检验记录
            if (!CollectionUtils.isEmpty(dto.getRecords())) {
                saveQualityRecords(dto.getTaskId(), dto.getRecords());
            }

            // 保存缺陷明细
            if (!CollectionUtils.isEmpty(dto.getDefects())) {
                saveDefectDetails(dto.getTaskId(), dto.getDefects());
            }

            log.info("提交检验任务成功，任务ID: {}，结果: {}", dto.getTaskId(), dto.getResult());

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("提交检验任务失败，任务ID: {}", dto.getTaskId(), e);
            throw new BusinessException("提交检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void assignTask(TaskAssignDTO dto) {
        try {
            validateAssignDTO(dto);

            // 批量更新任务
            taskMapper.updateBatchStatus(dto.getTaskIds(), "PENDING",
                    dto.getInspector(), LocalDateTime.now());

            log.info("分配检验任务成功，任务数量: {}，检验员: {}",
                    dto.getTaskIds().size(), dto.getInspector());

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("分配检验任务失败", e);
            throw new BusinessException("分配检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void cancelTask(Long taskId, String reason) {
        try {
            if (!StringUtils.hasText(reason)) {
                throw new ValidationException("取消原因不能为空");
            }

            QualityTask task = taskMapper.selectById(taskId);
            if (task == null) {
                throw new BusinessException("检验任务不存在");
            }

            // 只能取消待检或检验中的任务
            if (!"PENDING".equals(task.getStatus()) && !"INSPECTING".equals(task.getStatus())) {
                throw new BusinessException("当前状态的任务不能取消");
            }

            task.setStatus("CANCELLED");
            task.setRemark(reason);
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);

            log.info("取消检验任务成功，任务ID: {}", taskId);

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("取消检验任务失败，ID: {}", taskId, e);
            throw new BusinessException("取消检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void suspendTask(Long taskId, String reason) {
        try {
            QualityTask task = taskMapper.selectById(taskId);
            if (task == null) {
                throw new BusinessException("检验任务不存在");
            }

            // 只能暂停检验中的任务
            if (!"INSPECTING".equals(task.getStatus())) {
                throw new BusinessException("只有检验中的任务可以暂停");
            }

            task.setStatus("SUSPENDED");
            task.setRemark(reason);
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);

            log.info("暂停检验任务成功，任务ID: {}", taskId);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("暂停检验任务失败，ID: {}", taskId, e);
            throw new BusinessException("暂停检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void resumeTask(Long taskId) {
        try {
            QualityTask task = taskMapper.selectById(taskId);
            if (task == null) {
                throw new BusinessException("检验任务不存在");
            }

            // 只能恢复暂停的任务
            if (!"SUSPENDED".equals(task.getStatus())) {
                throw new BusinessException("只有暂停的任务可以恢复");
            }

            task.setStatus("INSPECTING");
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);

            log.info("恢复检验任务成功，任务ID: {}", taskId);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("恢复检验任务失败，ID: {}", taskId, e);
            throw new BusinessException("恢复检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void completeTask(Long taskId) {
        try {
            QualityTask task = taskMapper.selectById(taskId);
            if (task == null) {
                throw new BusinessException("检验任务不存在");
            }

            // 只能完成检验中的任务
            if (!"INSPECTING".equals(task.getStatus())) {
                throw new BusinessException("只有检验中的任务可以完成");
            }

            task.setStatus("COMPLETED");
            task.setCompleteTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            taskMapper.updateById(task);

            log.info("完成检验任务成功，任务ID: {}", taskId);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("完成检验任务失败，ID: {}", taskId, e);
            throw new BusinessException("完成检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void assignBatch(List<Long> taskIds, String inspector) {
        try {
            if (CollectionUtils.isEmpty(taskIds)) {
                throw new ValidationException("请选择要分配的任务");
            }

            if (!StringUtils.hasText(inspector)) {
                throw new ValidationException("检验员不能为空");
            }

            TaskAssignDTO dto = new TaskAssignDTO();
            dto.setTaskIds(taskIds);
            dto.setInspector(inspector);
            dto.setAssignBy("system");

            assignTask(dto);

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量分配检验任务失败", e);
            throw new BusinessException("批量分配检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void startBatch(List<Long> taskIds, String inspector) {
        try {
            if (CollectionUtils.isEmpty(taskIds)) {
                throw new ValidationException("请选择要开始的任务");
            }

            if (!StringUtils.hasText(inspector)) {
                throw new ValidationException("检验员不能为空");
            }

            for (Long taskId : taskIds) {
                try {
                    startTask(taskId, inspector);
                } catch (Exception e) {
                    log.error("批量开始任务失败，任务ID: {}", taskId, e);
                }
            }

            log.info("批量开始检验任务成功，数量: {}", taskIds.size());

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量开始检验任务失败", e);
            throw new BusinessException("批量开始检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public void submitBatch(List<TaskSubmitDTO> dtos) {
        try {
            if (CollectionUtils.isEmpty(dtos)) {
                throw new ValidationException("请选择要提交的任务");
            }

            for (TaskSubmitDTO dto : dtos) {
                try {
                    submitTask(dto);
                } catch (Exception e) {
                    log.error("批量提交任务失败，任务ID: {}", dto.getTaskId(), e);
                }
            }

            log.info("批量提交检验任务成功，数量: {}", dtos.size());

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量提交检验任务失败", e);
            throw new BusinessException("批量提交检验任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<TaskVO> getPendingTasks(String inspector) {
        try {
            List<QualityTask> tasks = taskMapper.selectPendingTasks(inspector);
            return tasks.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取待检任务失败", e);
            throw new BusinessException("获取待检任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<TaskVO> getInspectingTasks(String inspector) {
        try {
            List<QualityTask> tasks = taskMapper.selectInspectingTasks(inspector);
            return tasks.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取检验中任务失败", e);
            throw new BusinessException("获取检验中任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<TaskVO> getMyTasks(String inspector) {
        try {
            if (!StringUtils.hasText(inspector)) {
                throw new ValidationException("检验员不能为空");
            }

            TaskQueryDTO query = new TaskQueryDTO();
            query.setInspector(inspector);
            query.setStatus("PENDING,INSPECTING");
            query.setPageSize(100);

            Page<QualityTask> page = new Page<>(1, query.getPageSize());
            IPage<QualityTask> taskPage = taskMapper.selectPageList(page, query);

            return taskPage.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (ValidationException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取我的任务失败", e);
            throw new BusinessException("获取我的任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<TaskVO> getUrgentTasks() {
        try {
            TaskQueryDTO query = new TaskQueryDTO();
            query.setUrgent(true);
            query.setStatus("PENDING,INSPECTING");
            query.setPageSize(50);

            Page<QualityTask> page = new Page<>(1, query.getPageSize());
            IPage<QualityTask> taskPage = taskMapper.selectPageList(page, query);

            return taskPage.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取紧急任务失败", e);
            throw new BusinessException("获取紧急任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<TaskVO> getTimeoutTasks() {
        try {
            List<QualityTask> tasks = taskMapper.selectTimeoutTasks(60);
            return tasks.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取超时任务失败", e);
            throw new BusinessException("获取超时任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<TaskVO> getTasksByPlan(Long planId) {
        try {
            List<QualityTask> tasks = taskMapper.selectByPlanId(planId);
            return tasks.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取计划任务失败，计划ID: {}", planId, e);
            throw new BusinessException("获取计划任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<TaskVO> getTasksByProduct(String productCode) {
        try {
            TaskQueryDTO query = new TaskQueryDTO();
            query.setProductCode(productCode);
            query.setPageSize(100);

            Page<QualityTask> page = new Page<>(1, query.getPageSize());
            IPage<QualityTask> taskPage = taskMapper.selectPageList(page, query);

            return taskPage.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("获取产品任务失败，产品编码: {}", productCode, e);
            throw new BusinessException("获取产品任务失败: " + e.getMessage());
        }
    }

    @Override
    public TaskStatisticVO getTaskStatistics(TaskQueryDTO query) {
        try {
            TaskStatisticVO statistic = new TaskStatisticVO();

            // 基础统计
            Map<String, Object> basicStats = taskMapper.getBasicStatistics(query);
            statistic.setBasicStats(basicStats);

            // 状态分布
            Map<String, Long> statusCount = taskMapper.countByStatus(query);
            statistic.setStatusCount(statusCount);

            // 结果分布
            Map<String, Long> resultCount = taskMapper.countByResult(query);
            statistic.setResultCount(resultCount);

            // 合格率趋势
            List<Map<String, Object>> passRateTrend = taskMapper.getPassRateTrend(query);
            statistic.setPassRateTrend(passRateTrend);

            // 产品统计
            List<Map<String, Object>> productStats = taskMapper.getProductStatistics(query);
            statistic.setProductStats(productStats);

            // 检验员统计
            List<Map<String, Object>> inspectorStats = taskMapper.getInspectorStatistics(query);
            statistic.setInspectorStats(inspectorStats);

            return statistic;

        } catch (Exception e) {
            log.error("获取任务统计失败", e);
            throw new BusinessException("获取任务统计失败: " + e.getMessage());
        }
    }

    @Override
    public RealTimeStatisticVO getRealTimeStatistic() {
        try {
            RealTimeStatisticVO statistic = new RealTimeStatisticVO();

            // 今日统计
            String today = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_DATE);
            Map<String, Object> todayStats = taskMapper.getTodayStatistics(today);
            statistic.setTodayStats(todayStats);

            // 实时任务
            statistic.setPendingTasks(getPendingTasks(null).size());
            statistic.setInspectingTasks(getInspectingTasks(null).size());
            statistic.setUrgentTasks(getUrgentTasks().size());
            statistic.setTimeoutTasks(getTimeoutTasks().size());

            // 缺陷统计
            List<Map<String, Object>> defectStats = defectDetailMapper.getTodayDefectStatistics(today);
            statistic.setDefectStats(defectStats);

            return statistic;

        } catch (Exception e) {
            log.error("获取实时统计失败", e);
            throw new BusinessException("获取实时统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getTaskCountByStatus() {
        try {
            return taskMapper.countByStatus(null);
        } catch (Exception e) {
            log.error("获取任务状态统计失败", e);
            throw new BusinessException("获取任务状态统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getTaskCountByResult() {
        try {
            return taskMapper.countByResult(null);
        } catch (Exception e) {
            log.error("获取任务结果统计失败", e);
            throw new BusinessException("获取任务结果统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getTaskCountByInspector() {
        try {
            return taskMapper.countByInspector(null);
        } catch (Exception e) {
            log.error("获取检验员任务统计失败", e);
            throw new BusinessException("获取检验员任务统计失败: " + e.getMessage());
        }
    }

    @Override
    public void checkAndAlertTimeoutTasks() {
        try {
            List<TaskVO> timeoutTasks = getTimeoutTasks();
            if (!CollectionUtils.isEmpty(timeoutTasks)) {
                for (TaskVO task : timeoutTasks) {
                    // TODO: 发送超时预警
                    log.warn("检测到超时任务，任务ID: {}，任务编号: {}",
                            task.getId(), task.getTaskNo());
                }
            }
        } catch (Exception e) {
            log.error("检查超时任务失败", e);
        }
    }

    @Override
    public Map<String, Object> getTaskProgress(Long taskId) {
        try {
            Map<String, Object> progress = new HashMap<>();

            QualityTask task = taskMapper.selectById(taskId);
            if (task == null) {
                return progress;
            }

            // 计算任务进度
            if ("COMPLETED".equals(task.getStatus())) {
                progress.put("progress", 100);
                progress.put("status", "已完成");
            } else if ("INSPECTING".equals(task.getStatus())) {
                // 根据检验记录计算进度
                List<QualityRecord> records = recordMapper.selectByTaskId(taskId);
                if (!CollectionUtils.isEmpty(records)) {
                    long totalItems = records.size();
                    long completedItems = records.stream()
                            .filter(r -> r.getResult() != null)
                            .count();
                    int progressPercent = (int) ((double) completedItems / totalItems * 100);
                    progress.put("progress", progressPercent);
                    progress.put("completedItems", completedItems);
                    progress.put("totalItems", totalItems);
                } else {
                    progress.put("progress", 0);
                }
                progress.put("status", "检验中");
            } else {
                progress.put("progress", 0);
                progress.put("status", "待开始");
            }

            return progress;

        } catch (Exception e) {
            log.error("获取任务进度失败，任务ID: {}", taskId, e);
            throw new BusinessException("获取任务进度失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getTaskTrend(TaskQueryDTO query) {
        try {
            return taskMapper.getDailyTrend(query);
        } catch (Exception e) {
            log.error("获取任务趋势失败", e);
            throw new BusinessException("获取任务趋势失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> exportTasks(TaskQueryDTO query) {
        try {
            return taskMapper.exportByQuery(query);
        } catch (Exception e) {
            log.error("导出任务失败", e);
            throw new BusinessException("导出任务失败: " + e.getMessage());
        }
    }

    @Override
    public byte[] generateTaskReport(TaskQueryDTO query) {
        try {
            // TODO: 实现生成PDF或Excel报告
            // 这里可以集成报表生成工具
            log.info("生成任务报告，查询条件: {}", query);
            return new byte[0];

        } catch (Exception e) {
            log.error("生成任务报告失败", e);
            throw new BusinessException("生成任务报告失败: " + e.getMessage());
        }
    }

    // ============ 私有方法 ============

    private TaskVO convertToVO(QualityTask task) {
        TaskVO vo = new TaskVO();
        BeanUtils.copyProperties(task, vo);

        // 计算是否超时
        if ("PENDING".equals(task.getStatus()) || "INSPECTING".equals(task.getStatus())) {
            long minutes = ChronoUnit.MINUTES.between(task.getCreateTime(), LocalDateTime.now());
            vo.setTimeout(minutes > 60);
            vo.setTimeoutMinutes(minutes);
        }

        // 计算合格率
        if (task.getQuantityChecked() != null && task.getQuantityChecked() > 0) {
            double passRate = (double) task.getQuantityQualified() / task.getQuantityChecked() * 100;
            vo.setPassRate(passRate);
        }

        return vo;
    }

    private String generateTaskNo() {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateStr = LocalDateTime.now().format(formatter);
        String random = String.format("%03d", new Random().nextInt(1000));
        return "QT" + dateStr + random;
    }

    private void validateQuery(TaskQueryDTO query) {
        if (query == null) {
            throw new ValidationException("查询参数不能为空");
        }

        if (query.getPageNum() == null || query.getPageNum() < 1) {
            query.setPageNum(1);
        }

        if (query.getPageSize() == null || query.getPageSize() < 1) {
            query.setPageSize(20);
        }
    }

    private void validateTask(QualityTask task) {
        if (task == null) {
            throw new ValidationException("任务信息不能为空");
        }

        if (!StringUtils.hasText(task.getProductCode())) {
            throw new ValidationException("产品编码不能为空");
        }
    }

    private void validateSubmitDTO(TaskSubmitDTO dto) {
        if (dto == null) {
            throw new ValidationException("提交参数不能为空");
        }

        if (dto.getTaskId() == null) {
            throw new ValidationException("任务ID不能为空");
        }

        if (!StringUtils.hasText(dto.getResult())) {
            throw new ValidationException("检验结果不能为空");
        }
    }

    private void validateAssignDTO(TaskAssignDTO dto) {
        if (dto == null) {
            throw new ValidationException("分配参数不能为空");
        }

        if (CollectionUtils.isEmpty(dto.getTaskIds())) {
            throw new ValidationException("请选择要分配的任务");
        }

        if (!StringUtils.hasText(dto.getInspector())) {
            throw new ValidationException("检验员不能为空");
        }
    }

    private void saveQualityRecords(Long taskId, List<com.mes.dto.QualityRecordDTO> recordDTOs) {
        List<QualityRecord> records = new ArrayList<>();

        for (com.mes.dto.QualityRecordDTO dto : recordDTOs) {
            QualityRecord record = new QualityRecord();
            BeanUtils.copyProperties(dto, record);

            record.setTaskId(taskId);
            record.setCreateTime(LocalDateTime.now());
            record.setDeleted(0);

            records.add(record);
        }

        if (!records.isEmpty()) {
            recordMapper.insertBatch(records);
        }
    }

    private void saveDefectDetails(Long taskId, List<com.mes.dto.DefectDetailDTO> defectDTOs) {
        List<DefectDetail> defects = new ArrayList<>();

        for (com.mes.dto.DefectDetailDTO dto : defectDTOs) {
            DefectDetail defect = new DefectDetail();
            BeanUtils.copyProperties(dto, defect);

            defect.setTaskId(taskId);
            defect.setCreateTime(LocalDateTime.now());
            defect.setDeleted(0);

            defects.add(defect);
        }

        if (!defects.isEmpty()) {
            defectDetailMapper.insertBatch(defects);
        }
    }
}
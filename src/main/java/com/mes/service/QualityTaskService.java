package com.mes.service;

import com.mes.common.PageResult;
import com.mes.dto.TaskAssignDTO;
import com.mes.dto.TaskQueryDTO;
import com.mes.dto.TaskSubmitDTO;
import com.mes.entity.QualityTask;
import com.mes.vo.RealTimeStatisticVO;
import com.mes.vo.TaskDetailVO;
import com.mes.vo.TaskStatisticVO;
import com.mes.vo.TaskVO;

import java.util.List;
import java.util.Map;

public interface QualityTaskService {

    // ============ 任务管理 ============
    PageResult<TaskVO> getTaskList(TaskQueryDTO query);
    TaskDetailVO getTaskDetail(Long id);
    Long createTask(QualityTask task);
    void updateTask(QualityTask task);
    void deleteTask(Long id);
    void deleteBatch(List<Long> ids);

    // ============ 任务流程 ============
    void startTask(Long taskId, String inspector);
    void submitTask(TaskSubmitDTO dto);
    void assignTask(TaskAssignDTO dto);
    void cancelTask(Long taskId, String reason);
    void suspendTask(Long taskId, String reason);
    void resumeTask(Long taskId);
    void completeTask(Long taskId);

    // ============ 批量操作 ============
    void assignBatch(List<Long> taskIds, String inspector);
    void startBatch(List<Long> taskIds, String inspector);
    void submitBatch(List<TaskSubmitDTO> dtos);

    // ============ 查询接口 ============
    List<TaskVO> getPendingTasks(String inspector);
    List<TaskVO> getInspectingTasks(String inspector);
    List<TaskVO> getMyTasks(String inspector);
    List<TaskVO> getUrgentTasks();
    List<TaskVO> getTimeoutTasks();
    List<TaskVO> getTasksByPlan(Long planId);
    List<TaskVO> getTasksByProduct(String productCode);

    // ============ 统计分析 ============
    TaskStatisticVO getTaskStatistics(TaskQueryDTO query);
    RealTimeStatisticVO getRealTimeStatistic();
    Map<String, Long> getTaskCountByStatus();
    Map<String, Long> getTaskCountByResult();
    Map<String, Long> getTaskCountByInspector();

    // ============ 监控提醒 ============
    void checkAndAlertTimeoutTasks();
    Map<String, Object> getTaskProgress(Long taskId);
    List<Map<String, Object>> getTaskTrend(TaskQueryDTO query);

    // ============ 报表导出 ============
    List<Map<String, Object>> exportTasks(TaskQueryDTO query);
    byte[] generateTaskReport(TaskQueryDTO query);
}
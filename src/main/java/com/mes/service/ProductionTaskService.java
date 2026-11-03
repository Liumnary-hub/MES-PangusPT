package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ProductionTask;
import java.util.List;

public interface ProductionTaskService extends IService<ProductionTask> {

    /**
     * 根据工单创建生产任务
     */
    boolean createTasksFromOrder(Long orderId);

    /**
     * 开始任务
     */
    boolean startTask(Long taskId, String operatorId);

    /**
     * 完成任务
     */
    boolean completeTask(Long taskId, Integer completedQuantity, Integer qualifiedQuantity);

    /**
     * 查询工位任务
     */
    List<ProductionTask> getTasksByResource(Long resourceId);

    /**
     * 查询待执行任务
     */
    List<ProductionTask> getPendingTasks(Long resourceId);

    /**
     * 更新任务进度
     */
    boolean updateTaskProgress(Long taskId, Integer completedQuantity, Integer qualifiedQuantity, Double actualTime);

    /**
     * 分页查询任务
     */

    IPage<ProductionTask> getTasksByCondition(Page<ProductionTask> page, String taskCode, Long orderId, Long resourceId, String status);

    List<ProductionTask> getTasksByOrder(Long orderId);

    /**
     * 暂停任务
     */
//    boolean pauseTask(Long taskId);
//
//    /**
//     * 恢复任务
//     */
//    boolean resumeTask(Long taskId);
//
//    /**
//     * 更新任务状态并同步工单状态
//     */
//    boolean updateTaskStatusWithOrderSync(Long taskId, String status);
//
//    /**
//     * 根据工单ID和状态查询任务
//     */
//    List<ProductionTask> getTasksByOrderAndStatus(Long orderId, String status);
}
package com.mes.service;

import com.mes.dto.ProductionOrderDetail;

//此接口类用于实现Order与Task的绑定
public interface ProductionCoordinatorService {
    /**
     * 完成计划 - 将工单从草稿状态改为已计划
     */
    boolean planOrder(Long orderId);

    /**
     * 下发工单并生成任务
     */
    boolean releaseOrderWithTasks(Long orderId);

    /**
     * 取消工单
     */
    boolean cancelOrder(Long orderId);

    /**
     * 开始任务并同步工单状态
     */
    boolean startTaskWithOrderSync(Long taskId, String operatorId, String operatorName);

    /**
     * 完成任务并同步工单状态
     */
    boolean completeTaskWithOrderSync(Long taskId, Integer completedQuantity, Integer qualifiedQuantity);

    /**
     * 更新任务进度并同步工单状态
     */
    boolean updateTaskProgressWithOrderSync(Long taskId, Integer completedQuantity, Integer qualifiedQuantity, Double actualTime);

    /**
     * 暂停任务
     */
    boolean pauseTask(Long taskId);

    /**
     * 恢复任务
     */
    boolean resumeTask(Long taskId);

    /**
     * 自动同步工单状态（基于任务状态）
     */
    void syncOrderStatus(Long orderId);

    /**
     * 获取工单详情（包含任务列表和进度）
     */
    ProductionOrderDetail getOrderDetail(Long orderId);

    /**
     * 获取工单进度
     */
    Double getOrderProgress(Long orderId);
}

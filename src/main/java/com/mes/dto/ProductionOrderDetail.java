package com.mes.dto;

import com.mes.entity.ProductionOrder;
import com.mes.entity.ProductionTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 工单详情数据传输对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductionOrderDetail extends ProductionOrder {

    /**
     * 关联的任务列表
     */
    private List<ProductionTask> tasks;

    /**
     * 进度百分比
     */
    private Double progress;

    /**
     * 任务状态统计
     */
    private Map<String, Long> taskStatusCount;

    /**
     * 资源使用情况
     */
    private Map<String, Object> resourceUsage;
}
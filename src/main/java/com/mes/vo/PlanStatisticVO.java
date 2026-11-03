package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PlanStatisticVO {
    private Long totalCount;            // 计划总数
    private Long draftCount;            // 草稿数量
    private Long approvedCount;         // 已审批数量
    private Long inProgressCount;       // 执行中数量
    private Long completedCount;        // 已完成数量
    private Long cancelledCount;        // 已取消数量
    private Double executionRate;       // 执行率
    private Double onTimeRate;          // 按时完成率

    private Map<String, Long> statusCount;          // 状态分布
    private List<Map<String, Object>> monthlyTrend; // 月度趋势
    private List<Map<String, Object>> productStats; // 产品统计
    private List<Map<String, Object>> typeStats;    // 类型统计
}

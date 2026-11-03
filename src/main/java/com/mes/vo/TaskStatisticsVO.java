package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskStatisticsVO {
    private Long totalTasks;           // 总任务数
    private Long completedTasks;       // 完成任务数
    private Long qualifiedTasks;       // 合格任务数
    private Long rejectedTasks;        // 不合格任务数
    private Double overallPassRate;    // 总体合格率
    private Double averagePassRate;    // 平均合格率

    // 趋势分析
    private List<Map<String, Object>> dailyTrend;          // 每日趋势
    private List<Map<String, Object>> weeklyTrend;         // 每周趋势
    private List<Map<String, Object>> monthlyTrend;        // 每月趋势

    // 维度统计
    private List<Map<String, Object>> productStats;        // 产品维度统计
    private List<Map<String, Object>> inspectorStats;      // 检验员维度统计
    private List<Map<String, Object>> workstationStats;    // 工位维度统计
    private List<Map<String, Object>> inspectionTypeStats; // 检验类型维度统计

    // 缺陷分析
    private Map<String, Long> defectTypeStats;             // 缺陷类型统计
    private Map<String, Long> defectSeverityStats;         // 缺陷严重程度统计
    private List<Map<String, Object>> defectTrend;         // 缺陷趋势

    // 绩效指标
    private Double averageInspectionTime;   // 平均检验时长
    private Double onTimeRate;              // 按时完成率
    private Double firstPassYield;          // 一次通过率
    private Double reworkRate;              // 返工率
}
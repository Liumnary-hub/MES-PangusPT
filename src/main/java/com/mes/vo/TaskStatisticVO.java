package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 任务统计VO
 */
@Data
public class TaskStatisticVO {
    // ============ 基础统计 ============
    private Map<String, Object> basicStats;                // 基础统计数据
    private Map<String, Long> statusCount;                // 状态分布统计
    private Map<String, Long> resultCount;                // 结果分布统计
    private List<Map<String, Object>> passRateTrend;      // 合格率趋势
    private List<Map<String, Object>> productStats;       // 产品统计
    private List<Map<String, Object>> inspectorStats;     // 检验员统计

    // ============ 扩展统计 ============
    private Long totalTasks;                // 总任务数
    private Long completedTasks;            // 完成任务数
    private Long pendingTasks;              // 待检任务数
    private Long inspectingTasks;           // 检验中任务数
    private Long cancelledTasks;            // 取消任务数
    private Long qualifiedTasks;            // 合格任务数
    private Long rejectedTasks;             // 不合格任务数
    private Double overallPassRate;         // 总体合格率

    // ============ 分布统计 ============
    private Map<String, Long> statusDistribution;       // 状态分布（兼容原属性）
    private Map<String, Long> resultDistribution;       // 结果分布（兼容原属性）
    private Map<String, Long> inspectionTypeDistribution; // 检验类型分布

    // ============ 趋势分析 ============
    private List<Map<String, Object>> dailyTrend;        // 每日趋势
    private List<Map<String, Object>> weeklyTrend;       // 每周趋势
    private List<Map<String, Object>> monthlyTrend;      // 每月趋势

    // ============ 维度统计 ============
    private List<Map<String, Object>> productStatistics;       // 产品维度统计（兼容原属性）
    private List<Map<String, Object>> inspectorStatistics;     // 检验员维度统计（兼容原属性）
    private List<Map<String, Object>> workstationStatistics;   // 工位维度统计
    private List<Map<String, Object>> standardStatistics;      // 标准维度统计

    // ============ 缺陷分析 ============
    private Long totalDefects;              // 总缺陷数
    private Map<String, Long> defectTypeDistribution;   // 缺陷类型分布
    private Map<String, Long> defectSeverityDistribution; // 缺陷严重程度分布
    private List<Map<String, Object>> topDefectProducts;   // TOP缺陷产品

    // ============ 绩效指标 ============
    private Double averageInspectionTime;   // 平均检验时长（分钟）
    private Double averagePassRate;         // 平均合格率
    private Double onTimeCompletionRate;    // 按时完成率
    private Double firstPassYield;          // 一次通过率
    private Double defectRate;              // 缺陷率（DPMO）

    // ============ 时间统计 ============
    private Integer totalInspectionMinutes; // 总检验时长（分钟）
    private Double averageDailyTasks;       // 日均任务数
    private Double averageDailyDefects;     // 日均缺陷数

    // ============ Getter/Setter（Lombok会自动生成，这里只是为了说明）============

    public void setBasicStats(Map<String, Object> basicStats) {
        this.basicStats = basicStats;

        // 可以从basicStats中提取具体数值到扩展统计字段
        if (basicStats != null) {
            this.totalTasks = basicStats.get("totalTasks") != null ?
                    ((Number) basicStats.get("totalTasks")).longValue() : null;
            this.completedTasks = basicStats.get("completedTasks") != null ?
                    ((Number) basicStats.get("completedTasks")).longValue() : null;
            this.pendingTasks = basicStats.get("pendingTasks") != null ?
                    ((Number) basicStats.get("pendingTasks")).longValue() : null;
            this.inspectingTasks = basicStats.get("inspectingTasks") != null ?
                    ((Number) basicStats.get("inspectingTasks")).longValue() : null;
            this.overallPassRate = basicStats.get("overallPassRate") != null ?
                    ((Number) basicStats.get("overallPassRate")).doubleValue() : null;
        }
    }

    public void setStatusCount(Map<String, Long> statusCount) {
        this.statusCount = statusCount;
        // 同时设置到兼容字段
        this.statusDistribution = statusCount;
    }

    public void setResultCount(Map<String, Long> resultCount) {
        this.resultCount = resultCount;
        // 同时设置到兼容字段
        this.resultDistribution = resultCount;

        // 从结果统计中提取合格/不合格任务数
        if (resultCount != null) {
            this.qualifiedTasks = resultCount.get("合格");
            this.rejectedTasks = resultCount.get("不合格");
        }
    }

    public void setPassRateTrend(List<Map<String, Object>> passRateTrend) {
        this.passRateTrend = passRateTrend;
    }

    public void setProductStats(List<Map<String, Object>> productStats) {
        this.productStats = productStats;
        // 同时设置到兼容字段
        this.productStatistics = productStats;
    }

    public void setInspectorStats(List<Map<String, Object>> inspectorStats) {
        this.inspectorStats = inspectorStats;
        // 同时设置到兼容字段
        this.inspectorStatistics = inspectorStats;
    }
}
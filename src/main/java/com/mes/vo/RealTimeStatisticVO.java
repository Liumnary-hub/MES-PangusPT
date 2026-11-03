package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 实时统计VO
 */
@Data
public class RealTimeStatisticVO {
    // ============ 今日统计（从todayStats中提取） ============
    private Map<String, Object> todayStats;               // 今日统计数据
    private Long todayTotalTasks;           // 今日总任务数
    private Long todayCompletedTasks;       // 今日完成任务数
    private Long todayPendingTasks;         // 今日待检任务数
    private Long todayInspectingTasks;      // 今日检验中任务数
    private Long todayQualifiedTasks;       // 今日合格任务数
    private Long todayRejectedTasks;        // 今日不合格任务数
    private Double todayPassRate;           // 今日合格率
    private Long todayDefects;              // 今日缺陷数

    // ============ 实时任务统计 ============
    private Integer pendingTasks;           // 待检任务数
    private Integer inspectingTasks;        // 检验中任务数
    private Integer urgentTasks;            // 紧急任务数
    private Integer timeoutTasks;           // 超时任务数

    // ============ 当前在线状态 ============
    private Integer onlineInspectors;       // 在线检验员数
    private Integer activeTasks;            // 活跃任务数

    // ============ 实时图表数据 ============
    private List<Map<String, Object>> hourlyTaskTrend;        // 小时任务趋势
    private List<Map<String, Object>> hourlyPassRateTrend;    // 小时合格率趋势
    private List<Map<String, Object>> hourlyDefectTrend;      // 小时缺陷趋势

    // ============ 实时分布 ============
    private List<Map<String, Object>> realTimeStatusDistribution;      // 实时状态分布
    private List<Map<String, Object>> realTimeInspectorWorkload;       // 实时检验员工作量
    private List<Map<String, Object>> realTimeProductQuality;          // 实时产品质量

    // ============ 缺陷统计 ============
    private List<Map<String, Object>> defectStats;            // 缺陷统计

    // ============ TOP统计 ============
    private List<Map<String, Object>> topDefectsToday;        // 今日TOP缺陷
    private List<Map<String, Object>> topInspectorsToday;     // 今日TOP检验员
    private List<Map<String, Object>> topProductsToday;       // 今日TOP产品
    private List<Map<String, Object>> topWorkstationsToday;   // 今日TOP工位

    // ============ 预警信息 ============
    private List<Map<String, Object>> recentAlerts;           // 近期预警
    private Integer unhandledAlerts;          // 未处理预警数
    private Integer highPriorityAlerts;       // 高优先级预警数

    // ============ 实时指标 ============
    private Double realTimePassRate;          // 实时合格率（最近1小时）
    private Double realTimeDefectRate;        // 实时缺陷率（最近1小时）
    private Double realTimeCompletionRate;    // 实时完成率（最近1小时）
    private Double realTimeOnTimeRate;        // 实时按时完成率（最近1小时）

    // ============ 工作负载 ============
    private Map<String, Object> currentWorkload;  // 当前工作负载
    private Map<String, Object> inspectorStatus;  // 检验员状态

    // ============ 时间点数据 ============
    private String updateTime;                // 更新时间
    private Integer dataFreshnessMinutes;     // 数据新鲜度（分钟）

    // ============ 自定义Setter方法 ============

    /**
     * 设置今日统计数据
     */
    public void setTodayStats(Map<String, Object> todayStats) {
        this.todayStats = todayStats;

        // 从todayStats中提取具体数值到对应的字段
        if (todayStats != null) {
            this.todayTotalTasks = getLongValue(todayStats, "total_count");
            this.todayCompletedTasks = getLongValue(todayStats, "completed_count");
            this.todayPendingTasks = getLongValue(todayStats, "pending_count");
            this.todayInspectingTasks = getLongValue(todayStats, "inspecting_count");
            this.todayQualifiedTasks = getLongValue(todayStats, "qualified_quantity");
            this.todayRejectedTasks = getLongValue(todayStats, "defective_quantity");
            this.todayPassRate = getDoubleValue(todayStats, "pass_rate");
            this.todayDefects = getLongValue(todayStats, "defect_count");
        }
    }

    /**
     * 设置缺陷统计
     */
    public void setDefectStats(List<Map<String, Object>> defectStats) {
        this.defectStats = defectStats;

        // 可以从defectStats中计算今日缺陷总数
        if (defectStats != null) {
            Long totalDefects = defectStats.stream()
                    .mapToLong(map -> getLongValue(map, "defect_count"))
                    .sum();
            this.todayDefects = totalDefects;
        }
    }

    /**
     * 设置待检任务数
     */
    public void setPendingTasks(Integer pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    /**
     * 设置检验中任务数
     */
    public void setInspectingTasks(Integer inspectingTasks) {
        this.inspectingTasks = inspectingTasks;
    }

    /**
     * 设置紧急任务数
     */
    public void setUrgentTasks(Integer urgentTasks) {
        this.urgentTasks = urgentTasks;
    }

    /**
     * 设置超时任务数
     */
    public void setTimeoutTasks(Integer timeoutTasks) {
        this.timeoutTasks = timeoutTasks;
    }

    // ============ 工具方法 ============

    private Long getLongValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return 0L;
        }
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        return 0L;
    }

    private Double getDoubleValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return 0.0;
        }
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }
}
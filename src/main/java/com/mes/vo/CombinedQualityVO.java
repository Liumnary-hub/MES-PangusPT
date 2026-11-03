package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CombinedQualityVO {
    // 今日概况
    private TaskRealTimeStatistic todayOverview;      // 今日概况

    // 实时数据
    private List<TaskVO> pendingTasks;               // 待检任务
    private List<TaskVO> inspectingTasks;            // 检验中任务
    private List<QualityAlertVO> recentAlerts;       // 近期预警

    // 统计信息
    private QualityDashboardVO dashboard;            // 看板数据
    private TaskStatisticsVO taskStatistics;         // 任务统计
    private DefectStatisticVO defectStatistics;      // 缺陷统计
    private AlertStatisticVO alertStatistics;        // 预警统计

    // 趋势分析
    private List<Map<String, Object>> qualityTrend;  // 质量趋势
    private List<Map<String, Object>> defectTrend;   // 缺陷趋势
    private List<Map<String, Object>> passRateTrend; // 合格率趋势

    // 关键指标
    private Map<String, Object> kpis;                // 关键绩效指标
}

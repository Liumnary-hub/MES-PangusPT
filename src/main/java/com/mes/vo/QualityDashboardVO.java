package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QualityDashboardVO {
    // 关键指标
    private Long totalTasksToday;      // 今日总任务
    private Long completedTasksToday;  // 今日完成任务
    private Long pendingTasksToday;    // 今日待检任务
    private Double passRateToday;      // 今日合格率
    private Long defectsToday;         // 今日缺陷数
    private Double defectRateToday;    // 今日缺陷率

    // 趋势分析
    private List<Map<String, Object>> passRateTrend;       // 合格率趋势
    private List<Map<String, Object>> taskCompletionTrend; // 任务完成趋势
    private List<Map<String, Object>> defectTrend;         // 缺陷趋势

    // 分布分析
    private List<Map<String, Object>> defectTypeDistribution;    // 缺陷类型分布
    private List<Map<String, Object>> productPassRateRank;       // 产品合格率排行
    private List<Map<String, Object>> inspectorPerformance;      // 检验员绩效
    private List<Map<String, Object>> workstationQuality;        // 工位质量

    // TOP分析
    private List<Map<String, Object>> topDefects;          // TOP缺陷
    private List<Map<String, Object>> topDefectProducts;   // TOP缺陷产品
    private List<Map<String, Object>> topQualityIssues;    // TOP质量问题

    // 预警信息
    private List<QualityAlertVO> recentAlerts;             // 近期预警
    private Long unhandledAlerts;                          // 未处理预警

    // 统计信息
    private Map<String, Object> basicStats;                // 基础统计
}

package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AlertStatisticVO {
    private Long totalAlerts;           // 总预警数
    private Long unreadAlerts;          // 未读预警数
    private Long unhandledAlerts;       // 未处理预警数
    private Long urgentAlerts;          // 紧急预警数
    private Long importantAlerts;       // 重要预警数
    private Long generalAlerts;         // 一般预警数

    // 分布分析
    private Map<String, Long> typeDistribution;     // 类型分布
    private Map<String, Long> levelDistribution;    // 级别分布
    private Map<String, Long> statusDistribution;   // 状态分布

    // 趋势分析
    private List<Map<String, Object>> dailyAlertTrend;   // 每日预警趋势
    private List<Map<String, Object>> weeklyAlertTrend;  // 每周预警趋势
    private List<Map<String, Object>> monthlyAlertTrend; // 每月预警趋势

    // TOP分析
    private List<Map<String, Object>> topAlertProducts;    // TOP预警产品
    private List<Map<String, Object>> topAlertWorkstations; // TOP预警工位
    private List<Map<String, Object>> topAlertInspectors;  // TOP预警检验员
}

package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DefectStatisticVO {
    private Long totalDefects;         // 总缺陷数
    private Long openDefects;          // 待处理缺陷
    private Long inProgressDefects;    // 处理中缺陷
    private Long closedDefects;        // 已关闭缺陷
    private Double defectRate;         // 缺陷率

    // 缺陷分布
    private Map<String, Long> typeDistribution;        // 类型分布
    private Map<String, Long> severityDistribution;    // 严重程度分布
    private Map<String, Long> statusDistribution;      // 状态分布

    // 趋势分析
    private List<Map<String, Object>> dailyDefectTrend;    // 每日缺陷趋势
    private List<Map<String, Object>> weeklyDefectTrend;   // 每周缺陷趋势
    private List<Map<String, Object>> monthlyDefectTrend;  // 每月缺陷趋势

    // TOP分析
    private List<Map<String, Object>> topDefectTypes;      // TOP缺陷类型
    private List<Map<String, Object>> topDefectProducts;   // TOP缺陷产品
    private List<Map<String, Object>> topDefectWorkstations; // TOP缺陷工位
    private List<Map<String, Object>> topDefectInspectors; // TOP缺陷检验员
}

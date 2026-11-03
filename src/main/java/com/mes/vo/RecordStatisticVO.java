package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RecordStatisticVO {
    private Long totalRecords;          // 总记录数
    private Long qualifiedRecords;      // 合格记录数
    private Long defectiveRecords;      // 不合格记录数
    private Double passRate;            // 合格率

    // 趋势分析
    private List<Map<String, Object>> hourlyTrend;      // 小时趋势
    private List<Map<String, Object>> dailyTrend;       // 每日趋势

    // 项目分析
    private List<Map<String, Object>> itemStats;        // 检验项目统计
    private List<Map<String, Object>> itemPassRate;     // 项目合格率
    private List<Map<String, Object>> itemDefectRate;   // 项目缺陷率
}

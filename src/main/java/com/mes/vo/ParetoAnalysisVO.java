package com.mes.vo;// ParetoAnalysisVO.java - 帕累托分析VO

import lombok.Data;

import java.util.List;

@Data
public class ParetoAnalysisVO {
    private List<ParetoItem> items;    // 帕累托分析项
    private Long totalDefects;         // 总缺陷数
    private Integer vitalFewCount;     // 关键缺陷数量
    private Double vitalFewPercentage; // 关键缺陷百分比

    // 分析结果
    private List<String> vitalFewDefects;   // 关键缺陷列表
    private List<String> trivialManyDefects;// 次要缺陷列表
    private String improvementSuggestion;   // 改进建议
}
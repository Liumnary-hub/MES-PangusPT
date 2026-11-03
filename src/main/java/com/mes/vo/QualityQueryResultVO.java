package com.mes.vo;// QualityQueryResultVO.java - 质量查询结果VO

import lombok.Data;

import java.util.List;

@Data
public class QualityQueryResultVO<T> {
    private List<T> data;               // 查询数据
    private Object summary;             // 汇总信息
    private Object statistics;          // 统计信息
    private Object trend;               // 趋势信息
    private Object analysis;            // 分析结果
    private Object warning;             // 预警信息
}

// CombinedQualityVO.java - 综合质量信息VO



package com.mes.vo;

import lombok.Data;

@Data
public class SPCDataPoint {
    private Integer subgroupNo;        // 子组编号
    private Integer sampleSize;        // 样本数量
    private Double average;            // 平均值
    private Double range;              // 极差
    private Double standardDeviation;  // 标准差

    // 控制线
    private Double centerLine;         // 中心线
    private Double upperControlLimit;  // 上控制限
    private Double lowerControlLimit;  // 下控制限
    private Double rangeUCL;           // 极差上控制限
    private Double rangeLCL;           // 极差下控制限

    // 规格限
    private Double upperSpecLimit;     // 上规格限
    private Double lowerSpecLimit;     // 下规格限
    private Double targetValue;        // 目标值
}

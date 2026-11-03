package com.mes.vo;

import lombok.Data;

@Data
public class ParetoItem {
    private String defectCode;         // 缺陷编码
    private String defectName;         // 缺陷名称
    private String defectType;         // 缺陷类型
    private Long count;                // 数量
    private Double percentage;         // 百分比
    private Double cumulativePercentage; // 累计百分比
    private Boolean vitalFew;          // 是否关键缺陷（前80%）
}

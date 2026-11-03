package com.mes.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialConsumptionStatVO {
    private String materialCode;
    private String materialName;
    private String unit;
    private BigDecimal totalConsumption; // 总消耗量
    private BigDecimal monthConsumption; // 月消耗量
    private BigDecimal avgDailyConsumption; // 日均消耗量
    private Integer consumptionDays;     // 消耗天数
}

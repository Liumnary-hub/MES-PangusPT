package com.mes.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryStatVO {
    private String materialType;
    private String materialTypeDesc;
    private Integer materialCount;      // 物料种类数
    private BigDecimal totalQuantity;   // 总库存数量
    private BigDecimal totalValue;      // 总库存价值（估算）
    private BigDecimal availableQuantity; // 可用库存数量
}

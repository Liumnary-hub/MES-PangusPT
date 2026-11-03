package com.mes.vo;

import lombok.Data;

import java.math.BigDecimal;

// 库存预警VO
@Data
public class InventoryAlertVO {
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String materialType;
    private String unit;
    private BigDecimal currentQuantity;
    private BigDecimal safetyStock;
    private BigDecimal minStock;
    private BigDecimal maxStock;
    private String alertType; // BELOW_SAFETY-低于安全库存, BELOW_MIN-低于最小库存, EXCEED_MAX-超过最大库存
    private String alertDesc;
    private BigDecimal gapQuantity; // 差距数量
}

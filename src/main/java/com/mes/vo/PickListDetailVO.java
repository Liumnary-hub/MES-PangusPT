package com.mes.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PickListDetailVO {
    private Long id;
    private Long pickListId;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String materialType;
    private String specification;
    private String unit;
    private Long locationId;
    private String locationCode;
    private String locationName;
    private String batchNumber;
    private BigDecimal requiredQuantity;
    private BigDecimal pickedQuantity;
    private String status;
    private String statusDesc;
    private String remark;

    // 库存信息
    private BigDecimal availableQuantity; // 可用库存
    private Boolean stockEnough;         // 库存是否充足
}


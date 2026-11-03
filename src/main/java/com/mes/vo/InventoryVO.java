package com.mes.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InventoryVO {
    private Long id;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String materialType;
    private String specification;
    private String unit;
    private Long locationId;
    private String locationCode;
    private String locationName;
    private String warehouseCode;
    private String warehouseName;
    private BigDecimal currentQuantity;
    private BigDecimal lockedQuantity;
    private BigDecimal availableQuantity;
    private String batchNumber;
    private String supplierBatch;
    private LocalDate productionDate;
    private LocalDate expiryDate;
    private String qualityStatus;
    private String qualityStatusDesc;
    private LocalDateTime lastUpdateTime;

    // 扩展字段
    private Integer daysToExpiry; // 距离过期天数
    private Boolean isExpired;

    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;// 是否已过期
}
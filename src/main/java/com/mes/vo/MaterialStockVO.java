package com.mes.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 物料库存视图对象
 */
@Data
public class MaterialStockVO {

    private Long materialId;

    private String materialCode;

    private String materialName;

    private String materialType;

    private String specification;

    private String unit;

    /** 总库存 */
    private BigDecimal totalQuantity;

    /** 可用库存 */
    private BigDecimal availableQuantity;

    /** 锁定库存 */
    private BigDecimal lockedQuantity;

    /** 安全库存 */
    private BigDecimal safetyStock;

    /** 最大库存 */
    private BigDecimal maxStock;

    /** 最小库存 */
    private BigDecimal minStock;

    /** 库存状态 */
    private String stockStatus;

    /** 库存状态描述 */
    private String stockStatusDesc;

    /** 是否低于安全库存 */
    private Boolean belowSafetyStock;

    /** 是否超过最大库存 */
    private Boolean exceedMaxStock;
}
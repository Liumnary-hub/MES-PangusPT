package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物料数据传输对象
 * 用于接收前端传入的数据
 */
@Data
public class MaterialDTO {

    /** 物料编码 */
    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    /** 物料名称 */
    @NotBlank(message = "物料名称不能为空")
    private String materialName;

    /** 物料类型: RAW-原材料, SEMI-半成品, PROD-成品, TOOL-工装, CONSUMABLE-消耗品 */
    @NotBlank(message = "物料类型不能为空")
    private String materialType;

    /** 规格型号 */
    private String specification;

    /** 计量单位 */
    @NotBlank(message = "计量单位不能为空")
    private String unit;

    /** 安全库存 */
    private BigDecimal safetyStock;

    /** 最大库存 */
    private BigDecimal maxStock;

    /** 最小库存 */
    private BigDecimal minStock;

    /** 状态: active-启用, inactive-停用 */
    private String status;

    /** 默认供应商编码 */
    private String supplierCode;

    /** 存储条件 */
    private String storageCondition;

    /** 保质期(天) */
    private Integer shelfLife;
}
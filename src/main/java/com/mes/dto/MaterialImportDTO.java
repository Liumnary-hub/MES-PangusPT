package com.mes.dto;

import lombok.Data;

/**
 * 物料导入数据传输对象
 */
@Data
public class MaterialImportDTO {

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 物料类型 */
    private String materialType;

    /** 规格型号 */
    private String specification;

    /** 计量单位 */
    private String unit;

    /** 安全库存 */
    private String safetyStock;

    /** 最大库存 */
    private String maxStock;

    /** 最小库存 */
    private String minStock;

    /** 状态 */
    private String status;

    /** 默认供应商编码 */
    private String supplierCode;

    /** 存储条件 */
    private String storageCondition;

    /** 保质期(天) */
    private String shelfLife;

    /** 导入结果 */
    private String importResult;

    /** 失败原因 */
    private String failureReason;
}
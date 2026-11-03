package com.mes.dto;

import lombok.Data;

/**
 * 物料查询参数
 */
@Data
public class MaterialQueryDTO {

    /** 页码 */
    private Integer pageNum = 1;

    /** 页大小 */
    private Integer pageSize = 10;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 物料类型 */
    private String materialType;

    /** 规格型号 */
    private String specification;

    /** 状态 */
    private String status;

    /** 供应商编码 */
    private String supplierCode;
}
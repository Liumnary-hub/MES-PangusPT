package com.mes.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BomDetailVO {
    private Long id;
    private Long bomId;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String materialType;
    private String specification;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal lossRate;
    private Integer sequence;
    private String processStep;
    private Boolean isKeyMaterial;
    private String remark;
}
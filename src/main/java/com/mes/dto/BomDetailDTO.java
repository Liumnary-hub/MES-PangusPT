package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BomDetailDTO {
    @NotNull(message = "物料ID不能为空")
    private Long materialId;

    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    @NotNull(message = "数量不能为空")
    private BigDecimal quantity;

    @NotBlank(message = "单位不能为空")
    private String unit;

    private BigDecimal lossRate;
    private Integer sequence;
    private String processStep;
    private Boolean isKeyMaterial;
    private String remark;
}

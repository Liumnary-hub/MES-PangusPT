package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PickListItemDTO {
    @NotNull(message = "物料ID不能为空")
    private Long materialId;

    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    @NotNull(message = "需求数量不能为空")
    private BigDecimal requiredQuantity;

    private String batchNumber;
    private String remark;
}
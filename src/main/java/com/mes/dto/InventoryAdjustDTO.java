package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

// 库存调整DTO
@Data
public class InventoryAdjustDTO {
    @NotNull(message = "库存ID不能为空")
    private Long inventoryId;

    @NotNull(message = "调整数量不能为空")
    private BigDecimal adjustQuantity;

    @NotBlank(message = "调整原因不能为空")
    private String reason;

    private String batchNumber;
    @NotNull
    private BigDecimal quantity;

}
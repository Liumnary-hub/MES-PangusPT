package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

// 库存转移DTO
@Data
public class InventoryTransferDTO {
    @NotNull(message = "源库位ID不能为空")
    private Long fromLocationId;

    @NotNull(message = "目标库位ID不能为空")
    private Long toLocationId;

    @NotNull(message = "物料ID不能为空")
    private Long materialId;

    @NotNull(message = "转移数量不能为空")
    private BigDecimal quantity;

    private String batchNumber;

    @NotBlank(message = "转移原因不能为空")
    private String reason;
    @NotNull
    private Long fromInventoryId;
}
package com.mes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

// 领料单执行明细DTO
@Data
public class PickListExecuteItemDTO {
    @NotNull(message = "领料单明细ID不能为空")
    private Long pickListDetailId;

    @NotNull(message = "实拣数量不能为空")
    private BigDecimal pickedQuantity;

    private String batchNumber;
    private String locationCode;
}
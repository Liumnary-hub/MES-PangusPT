package com.mes.dto;// 仓库库位DTO


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WarehouseLocationDTO {
    @NotBlank(message = "库位编码不能为空")
    private String locationCode;

    @NotBlank(message = "库位名称不能为空")
    private String locationName;

    @NotBlank(message = "仓库编码不能为空")
    private String warehouseCode;

    @NotBlank(message = "仓库名称不能为空")
    private String warehouseName;

    @NotBlank(message = "库位类型不能为空")
    private String locationType;

    private BigDecimal capacity;
    private String status;
    private Long id;
}
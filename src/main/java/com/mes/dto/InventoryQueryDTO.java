package com.mes.dto;

import lombok.Data;

@Data
public class InventoryQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String materialCode;
    private String materialName;
    private String locationCode;
    private String warehouseCode;
    private String batchNumber;
    private String qualityStatus;
}
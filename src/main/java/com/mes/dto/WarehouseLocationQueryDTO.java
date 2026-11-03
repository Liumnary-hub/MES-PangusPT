package com.mes.dto;

import lombok.Data;

@Data
public class WarehouseLocationQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String locationCode;
    private String locationName;
    private String warehouseCode;
    private String locationType;
    private String status;
}

package com.mes.dto;

import lombok.Data;

// BOM 查询参数
@Data
public class BomQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String bomCode;
    private String productCode;
    private String productName;
    private String status;
    private String version;
}
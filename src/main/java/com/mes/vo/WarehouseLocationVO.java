package com.mes.vo;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WarehouseLocationVO {
    private Long id;
    private String locationCode;
    private String locationName;
    private String warehouseCode;
    private String warehouseName;
    private String locationType;
    private String locationTypeDesc;
    private BigDecimal capacity;
    private BigDecimal currentUsage;
    private String status;
    private String statusDesc;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;

    // 使用率
    private BigDecimal usageRate;
    private String usageStatus; // 空闲、正常、拥挤、已满
}
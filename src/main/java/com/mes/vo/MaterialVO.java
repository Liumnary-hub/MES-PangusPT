package com.mes.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaterialVO {
    private Long id;
    private String materialCode;
    private String materialName;
    private String materialType;
    private String specification;
    private String unit;

    // 改为 BigDecimal 类型
    private BigDecimal safetyStock;
    private BigDecimal maxStock;
    private BigDecimal minStock;

    private String status;
    private String supplierCode;
    private String storageCondition;
    private Integer shelfLife;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;

    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    // 状态描述
    public String getStatusDesc() {
        if (status == null) return "";
        switch (status) {
            case "active": return "启用";
            case "inactive": return "停用";
            default: return status;
        }
    }

    // 类型描述
    public String getMaterialTypeDesc() {
        if (materialType == null) return "";
        switch (materialType) {
            case "RAW": return "原材料";
            case "SEMI": return "半成品";
            case "PROD": return "成品";
            case "TOOL": return "工装";
            case "CONSUMABLE": return "消耗品";
            default: return materialType;
        }
    }
}
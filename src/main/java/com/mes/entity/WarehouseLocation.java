package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mes_warehouse_location")
public class WarehouseLocation {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("location_code")
    private String locationCode;

    // 修复：添加 location_name 字段映射
    @TableField("location_name")
    private String locationName;

    @TableField("warehouse_code")
    private String warehouseCode;

    @TableField("warehouse_name")
    private String warehouseName;

    @TableField("location_type")
    private String locationType;

    private BigDecimal capacity;

    @TableField("current_usage")
    private BigDecimal currentUsage;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    // 修复：逻辑删除字段
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
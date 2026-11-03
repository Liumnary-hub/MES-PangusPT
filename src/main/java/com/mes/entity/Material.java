package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mes_material")
public class Material {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("material_code")
    private String materialCode;

    @TableField("material_name")
    private String materialName;

    @TableField("material_type")
    private String materialType;

    private String specification;

    private String unit;

    // 修复：使用正确的字段名和映射
    @TableField("safety_stock")
    private BigDecimal safetyStock;

    // 添加缺失的 max_stock 字段
    @TableField("max_stock")
    private BigDecimal maxStock;

    @TableField("min_stock")
    private BigDecimal minStock;

    private String status;

    @TableField("supplier_code")
    private String supplierCode;

    @TableField("storage_condition")
    private String storageCondition;

    // 修复：shelf_life 应该是 Integer 类型
    @TableField("shelf_life")
    private Integer shelfLife;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    // 修复：字段名和类型修正
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
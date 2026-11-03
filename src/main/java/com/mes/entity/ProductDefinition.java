package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mes_product_definition")
public class ProductDefinition {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("product_code")
    private String productCode;       // 产品编码

    @TableField("product_name")
    private String productName;       // 产品名称

    @TableField("product_type")
    private String productType;       // 产品类型: finished-成品, semi-finished-半成品, raw-material-原材料

    @TableField("specification")
    private String specification;     // 规格型号

    @TableField("version")
    private String version;           // 版本号

    @TableField("unit")
    private String unit;              // 计量单位

    @TableField("status")
    private String status;            // 状态: active-启用, inactive-停用

    @TableField("description")
    private String description;       // 产品描述

    @TableField("technical_parameters")
    private String technicalParameters; // 技术参数

    @TableField("material")
    private String material;          // 材质

    @TableField("weight")
    private Double weight;            // 重量

    @TableField("safety_stock")
    private Integer safetyStock;      // 安全库存

    @TableField("process_flow")
    private String processFlow;       // 工艺路线

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("create_user")
    private String createUser;

    @TableField("update_user")
    private String updateUser;

    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}

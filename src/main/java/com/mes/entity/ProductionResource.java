package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mes_production_resource")
public class ProductionResource {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("resource_code")
    private String resourceCode;      // 资源编码

    @TableField("resource_name")
    private String resourceName;      // 资源名称

    @TableField("resource_type")
    private String resourceType;      // 资源类型: equipment-设备, tool-工具, station-工位, personnel-人员

    @TableField("specification")
    private String specification;     // 规格型号

    @TableField("capacity")
    private String capacity;          // 生产能力

    @TableField("department")
    private String department;        // 所属部门

    @TableField("status")
    private String status;            // 状态: active-启用, inactive-停用

    @TableField("description")
    private String description;       // 描述

    @TableField("technical_parameters")
    private String technicalParameters; // 技术参数

    @TableField("purchase_date")
    private LocalDateTime purchaseDate; // 购置日期

    @TableField("manufacturer")
    private String manufacturer;      // 制造商

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

    // 在现有Resource基础上添加这些字段
    private Double efficiency;            // 效率系数（0-1）
    private String availability;          // 可用性状态：AVAILABLE-可用, MAINTENANCE-维修中, INACTIVE-停用
}

package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("mes_process_route")
public class ProcessRoute {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("route_code")
    private String routeCode;         // 工艺路线编码

    @TableField("route_name")
    private String routeName;         // 工艺路线名称

    @TableField("product_id")
    private Long productId;           // 产品ID

    @TableField("version")
    private String version;           // 版本

    @TableField("status")
    private String status;            // 状态: active-启用, inactive-停用

    @TableField("description")
    private String description;       // 描述

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
package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("mes_process_step")
public class ProcessStep {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("step_code")
    private String stepCode;          // 工序编码

    @TableField("step_name")
    private String stepName;          // 工序名称

    @TableField("route_id")
    private Long routeId;             // 所属工艺路线ID

    @TableField("sequence")
    private Integer sequence;         // 工序顺序

    @TableField("resource_type")
    private String resourceType;      // 所需资源类型

    @TableField("standard_time")
    private Double standardTime;      // 标准工时(小时)

    @TableField("setup_time")
    private Double setupTime;         // 准备时间(小时)

    @TableField("description")
    private String description;       // 工序描述

    @TableField("quality_standard")
    private String qualityStandard;   // 质量标准

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
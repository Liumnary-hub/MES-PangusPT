package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("mes_production_data")
public class ProductionData {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_id")
    private Long taskId;              // 生产任务ID

    @TableField("data_type")
    private String dataType;          // 数据类型: quantity-产量, quality-质量, time-时间, equipment-设备参数

    @TableField("data_value")
    private String dataValue;         // 数据值

    @TableField("collection_time")
    private LocalDateTime collectionTime; // 采集时间

    @TableField("operator_id")
    private String operatorId;        // 操作员

    @TableField("remark")
    private String remark;            // 备注

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
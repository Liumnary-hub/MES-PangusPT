package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 领料单实体
 */
@Data
@TableName("mes_pick_list")
public class PickList {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 领料单号 */
    private String pickListCode;

    /** 工单ID */
    private Long workOrderId;

    /** 工单编号 */
    private String workOrderCode;

    /** 生产任务ID */
    private Long taskId;

    /** 任务编号 */
    private String taskCode;

    /** 状态: pending-待领料, picking-领料中, completed-已完成, cancelled-已取消 */
    private String status;

    /** 领料员ID */
    private String pickerId;

    /** 领料员姓名 */
    private String pickerName;

    /** 领料时间 */
    private LocalDateTime pickTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableLogic
    private Integer isDeleted;
}
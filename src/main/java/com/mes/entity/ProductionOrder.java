//package com.mes.entity;
//
//import com.baomidou.mybatisplus.annotation.*;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//import java.time.LocalDateTime;
//
//@Data
//@TableName("mes_production_order")
//public class ProductionOrder {
//    @TableId(type = IdType.AUTO)
//    private Long id;
//
//    @TableField("order_code")
//    private String orderCode;         // 工单编号
//
//    @TableField("product_id")
//    private Long productId;           // 产品ID
//
//    @TableField("product_code")
//    private String productCode;       // 产品编码（冗余字段）
//
//    @TableField("planned_quantity")
//    private Integer plannedQuantity;  // 计划数量
//
//    @TableField("completed_quantity")
//    private Integer completedQuantity; // 已完成数量
//
//    @TableField("planned_start_time")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime plannedStartTime; // 计划开始时间
//
//    @TableField("planned_end_time")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime plannedEndTime;   // 计划结束时间
//
//    @TableField("actual_start_time")
//    private LocalDateTime actualStartTime;  // 实际开始时间
//
//    @TableField("actual_end_time")
//    private LocalDateTime actualEndTime;    // 实际结束时间
//
//    @TableField("priority")
//    private Integer priority;         // 优先级 1-10
//
//    @TableField("status")
//    private String status;            // 状态: draft-草稿, planned-已计划, released-已下发, in_progress-进行中, completed-已完成, cancelled-已取消
//
//    @TableField("remark")
//    private String remark;            // 备注
//
//    @TableField(value = "create_time", fill = FieldFill.INSERT)
//    private LocalDateTime createTime;
//
//    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updateTime;
//
//    @TableField("create_user")
//    private String createUser;
//
//    @TableField("update_user")
//    private String updateUser;
//
//    @TableLogic
//    @TableField("is_deleted")
//    private Integer isDeleted;
//}
package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("mes_production_order")
public class ProductionOrder {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("order_code")
    private String orderCode;         // 工单编号

    @TableField("product_id")
    private Long productId;           // 产品ID

    @TableField("product_code")
    private String productCode;       // 产品编码（冗余字段）

    @TableField("planned_quantity")
    private Integer plannedQuantity;  // 计划数量

    @TableField("completed_quantity")
    private Integer completedQuantity = 0; // 已完成数量，默认0

    @TableField("planned_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedStartTime; // 计划开始时间

    @TableField("planned_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedEndTime;   // 计划结束时间

    @TableField("actual_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualStartTime;  // 实际开始时间

    @TableField("actual_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndTime;    // 实际结束时间

    @TableField("priority")
    private Integer priority = 5;     // 优先级 1-10，默认5

    // 状态约束：draft-草稿, planned-已计划, released-已下发, in_progress-进行中, completed-已完成, cancelled-已取消
    @TableField("status")
    private String status = "draft";  // 默认草稿状态

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

    // 在现有Order基础上添加这些字段
    private Integer schedulingPriority;   // 排产优先级（1-10）
    private String schedulingStatus;      // 排产状态：UNSCHEDULED-未排产, SCHEDULED-已排产
    private Integer estimatedDuration;    // 预估总工时（分钟）
    private LocalDateTime dueDate;        // 交货期
    private Integer customerPriority;     // 客户优先级
}
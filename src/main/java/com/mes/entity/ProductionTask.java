//package com.mes.entity;
//
//import com.baomidou.mybatisplus.annotation.*;
//import lombok.Data;
//import java.time.LocalDateTime;
//
//@Data
//@TableName("mes_production_task")
//public class ProductionTask {
//    @TableId(type = IdType.AUTO)
//    private Long id;
//
//    @TableField("task_code")
//    private String taskCode;          // 任务编号
//
//    @TableField("order_id")
//    private Long orderId;             // 所属工单ID
//
//    @TableField("product_id")
//    private Long productId;           // 产品ID
//
//    @TableField("resource_id")
//    private Long resourceId;          // 资源ID（设备/工位）
//
//    @TableField("operation_sequence")
//    private Integer operationSequence; // 工序顺序
//
//    @TableField("operation_name")
//    private String operationName;     // 工序名称
//
//    @TableField("planned_quantity")
//    private Integer plannedQuantity;  // 计划数量
//
//    @TableField("completed_quantity")
//    private Integer completedQuantity; // 完成数量
//
//    @TableField("qualified_quantity")
//    private Integer qualifiedQuantity; // 合格数量
//
//    @TableField("standard_time")
//    private Double standardTime;      // 标准工时(小时)
//
//    @TableField("actual_time")
//    private Double actualTime;        // 实际工时(小时)
//
//    @TableField("planned_start_time")
//    private LocalDateTime plannedStartTime; // 计划开始时间
//
//    @TableField("planned_end_time")
//    private LocalDateTime plannedEndTime;   // 计划结束时间
//
//    @TableField("actual_start_time")
//    private LocalDateTime actualStartTime;  // 实际开始时间
//
//    @TableField("actual_end_time")
//    private LocalDateTime actualEndTime;    // 实际结束时间
//
//    @TableField("operator_id")
//    private String operatorId;        // 操作员ID
//
//    @TableField("status")
//    private String status;            // 状态: pending-待执行, in_progress-执行中, completed-已完成, paused-暂停, cancelled-已取消
//
//    @TableField("quality_status")
//    private String qualityStatus;     // 质量状态: qualified-合格, unqualified-不合格, pending-待检
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
@TableName("mes_production_task")
public class ProductionTask {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_code")
    private String taskCode;          // 任务编号

    @TableField("order_id")
    private Long orderId;             // 所属工单ID

    // 添加工单编码冗余字段，便于查询显示
    @TableField("order_code")
    private String orderCode;         // 工单编号（冗余）

    @TableField("product_id")
    private Long productId;           // 产品ID

    // 添加产品编码冗余字段
    @TableField("product_code")
    private String productCode;       // 产品编码（冗余）

    @TableField("resource_id")
    private Long resourceId;          // 资源ID（设备/工位）

    // 添加资源名称冗余字段
    @TableField("resource_name")
    private String resourceName;      // 资源名称（冗余）

    @TableField("operation_sequence")
    private Integer operationSequence = 1; // 工序顺序，默认1

    @TableField("operation_name")
    private String operationName;     // 工序名称

    @TableField("planned_quantity")
    private Integer plannedQuantity;  // 计划数量

    @TableField("completed_quantity")
    private Integer completedQuantity = 0; // 完成数量，默认0

    @TableField("qualified_quantity")
    private Integer qualifiedQuantity = 0; // 合格数量，默认0

    @TableField("standard_time")
    private Double standardTime;      // 标准工时(小时)

    @TableField("actual_time")
    private Double actualTime = 0.0;  // 实际工时(小时)，默认0

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

    @TableField("operator_id")
    private String operatorId;        // 操作员ID

    // 添加操作员姓名冗余字段
    @TableField("operator_name")
    private String operatorName;      // 操作员姓名

    // 状态约束：pending-待执行, in_progress-执行中, completed-已完成, paused-暂停, cancelled-已取消
    @TableField("status")
    private String status = "pending"; // 默认待执行

    @TableField("quality_status")
    private String qualityStatus = "pending"; // 质量状态: qualified-合格, unqualified-不合格, pending-待检

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

    // 在现有Task基础上添加这些字段
    private Integer setupTime;            // 准备时间（分钟）
    private Integer teardownTime;         // 收尾时间（分钟）

    private Long predecessorTaskId;       // 前序任务ID
}
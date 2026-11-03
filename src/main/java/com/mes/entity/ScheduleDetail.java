package com.mes.entity;

import lombok.Data;

import java.time.LocalDateTime;

// 排产计划明细
@Data
public class ScheduleDetail {
    private Long id;
    private Long scheduleId;          // 排产计划ID
    private Long orderId;             // 工单ID
    private Long taskId;              // 任务ID
    private Long resourceId;          // 资源ID
    private Integer operationSequence;// 工序顺序
    private LocalDateTime plannedStartTime;  // 计划开始时间
    private LocalDateTime plannedEndTime;    // 计划结束时间
    private LocalDateTime actualStartTime;   // 实际开始时间
    private LocalDateTime actualEndTime;     // 实际结束时间
    private Integer duration;         // 工时（分钟）
    private Integer setupTime;        // 准备时间
    private Integer teardownTime;     // 收尾时间
    private String status;            // 状态：PENDING-待排产, SCHEDULED-已排产, IN_PROGRESS-进行中
    private Integer priority;         // 优先级
    private String constraintType;    // 约束类型：EARLIEST_START-最早开始, LATEST_FINISH-最晚完成
    private Long predecessorTaskId;   // 前序任务ID

    // 关联信息（非数据库字段）
    private String orderCode;
    private String taskCode;
    private String resourceName;
    private String operationName;
}
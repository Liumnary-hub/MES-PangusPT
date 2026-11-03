package com.mes.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 主排产计划表
@Data
public class ProductionSchedule {
    private Long id;
    private String scheduleCode;      // 排产计划编号
    private String scheduleName;      // 排产计划名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate scheduleDate;   // 排产日期
    private String scheduleType;      // 排产类型：DAILY-日排产, WEEKLY-周排产
    private String status;            // 状态：DRAFT-草稿, PUBLISHED-已发布, EXECUTING-执行中
    private Integer totalOrders;      // 总工单数
    private Integer totalTasks;       // 总任务数
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedStartTime;  // 计划开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedEndTime;    // 计划结束时间
    private LocalDateTime actualStartTime;   // 实际开始时间
    private LocalDateTime actualEndTime;     // 实际结束时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;

    @TableField("is_deleted")
    private Integer isDeleted;

    // 关联明细
    private List<ScheduleDetail> details;
}
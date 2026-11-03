package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// WorkCalendar.java
@Data
@TableName("mes_work_calendar")
public class WorkCalendar {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("calendar_date")
    private LocalDate calendarDate;

    @TableField("day_type")
    private String dayType;

    @TableField("work_shift_id")
    private Long workShiftId;

    @TableField("capacity_factor")
    private BigDecimal capacityFactor;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("is_deleted")
    private Integer isDeleted;

    // 关联字段
    @TableField(exist = false)
    private String shiftName;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private Boolean isWorkingDay;
}
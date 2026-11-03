package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

// WorkShift.java
@Data
@TableName("mes_work_shift")
public class WorkShift {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("shift_code")
    private String shiftCode;

    @TableField("shift_name")
    private String shiftName;

    @TableField("start_time")
    private LocalTime startTime;

    @TableField("end_time")
    private LocalTime endTime;

    @TableField("break_start_time")
    private LocalTime breakStartTime;

    @TableField("break_end_time")
    private LocalTime breakEndTime;

    private Integer duration;

    @TableField("is_active")
    private Boolean isActive;

    private String description;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("is_deleted")
    private Integer isDeleted;
}
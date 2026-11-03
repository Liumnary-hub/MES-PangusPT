package com.mes.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 故障记录查询DTO
 */
@Data
public class FaultQueryDTO {

    // 基础查询条件
    private String faultNo;
    private Long equipmentId;
    private String equipmentCode;
    private String equipmentName;

    // 故障信息
    private Integer faultType;        // 故障类型：1-机械,2-电气,3-控制,4-软件,5-其他
    private Integer faultLevel;       // 故障等级：1-轻微,2-一般,3-严重,4-重大
    private Integer faultStatus;      // 故障状态：1-已上报,2-处理中,3-已解决,4-已关闭

    // 时间范围查询
    private LocalDateTime occurTimeStart;      // 故障发生时间-开始
    private LocalDateTime occurTimeEnd;        // 故障发生时间-结束
    private LocalDateTime treatmentStartTimeStart;  // 处理开始时间-开始
    private LocalDateTime treatmentStartTimeEnd;    // 处理开始时间-结束
    private LocalDateTime createTimeStart;     // 创建时间-开始
    private LocalDateTime createTimeEnd;       // 创建时间-结束

    // 人员查询
    private String discoverer;      // 发现人
    private String handler;         // 处理人

    // 特殊查询
    private Boolean isRecurring;    // 是否重复故障
    private Boolean isOverdue;      // 是否超期未处理
    private String workshop;        // 所属车间
    private String equipmentType;   // 设备类型

    // 分页参数
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页记录数必须大于0")
    private Integer pageSize = 20;

    private String sortField = "occurTime";
    private String sortOrder = "desc";

    // 关键词搜索
    private String keyword;  // 支持故障单号、故障现象、处理措施的模糊查询

    // 统计模式
    private Boolean statisticsMode = false;
    private String groupBy;  // 统计分组：equipment, type, level, status, month

    // 导出模式
    private Boolean exportMode = false;
    private String exportFormat;  // excel, pdf

    /**
     * 获取分页偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }

    /**
     * 验证并初始化查询参数
     */
    public void validate() {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 20;
        }
        if (pageSize > 1000) {
            pageSize = 1000; // 防止内存溢出
        }
    }

    /**
     * 判断是否为统计查询
     */
    public boolean isStatisticsQuery() {
        return statisticsMode != null && statisticsMode;
    }

    /**
     * 判断是否为导出查询
     */
    public boolean isExportQuery() {
        return exportMode != null && exportMode;
    }
}
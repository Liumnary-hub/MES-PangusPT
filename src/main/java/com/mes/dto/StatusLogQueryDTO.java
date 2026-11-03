package com.mes.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备状态日志查询DTO
 */
@Data
public class StatusLogQueryDTO {

    // 基础查询条件
    private Long equipmentId;
    private String equipmentCode;
    private String equipmentName;

    // 日志信息
    private Integer logType;         // 日志类型：1-状态变更,2-运行参数,3-点检记录,4-报警记录,5-产量记录
    private Integer status;          // 设备状态：1-运行,2-待机,3-维修,4-保养,5-停机

    // 时间范围
    private LocalDateTime recordTimeStart;   // 记录时间-开始
    private LocalDateTime recordTimeEnd;     // 记录时间-结束

    // 报警相关
    private String alarmCode;        // 报警代码
    private Integer alarmLevel;      // 报警级别：1-提示,2-警告,3-严重,4-紧急
    private Integer minAlarmLevel;   // 最小报警级别
    private Integer maxAlarmLevel;   // 最大报警级别
    private String alarmKeyword;     // 报警信息关键词

    // 点检相关
    private Boolean inspectionQualified;  // 点检是否合格
    private String inspector;         // 点检人

    // 产量相关
    private Integer minOutput;        // 最小产量
    private Integer maxOutput;        // 最大产量

    // 其他条件
    private String operator;          // 操作员
    private String shift;             // 班次
    private String workshop;          // 所属车间
    private String equipmentType;     // 设备类型

    // 查询模式
    private Boolean alarmOnly = false;        // 仅查询报警记录
    private Boolean inspectionOnly = false;   // 仅查询点检记录
    private Boolean statusChangeOnly = false; // 仅查询状态变更记录

    // 统计模式
    private Boolean statisticsMode = false;
    private String statisticsType;    // hour, day, week, month
    private Boolean aggregateMode = false;
    private String aggregateField;    // hour, day, equipment, status

    // 导出模式
    private Boolean exportMode = false;
    private String exportFormat;

    // 分页参数
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页记录数必须大于0")
    private Integer pageSize = 20;

    private String sortField = "recordTime";
    private String sortOrder = "desc";

    // 关键词搜索
    private String keyword;

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
        if (pageSize > 5000) {
            pageSize = 5000; // 状态日志可能很多，限制最大查询数
        }

        // 设置默认时间范围（最近7天）
        if (recordTimeStart == null && recordTimeEnd == null) {
            recordTimeEnd = LocalDateTime.now();
            recordTimeStart = recordTimeEnd.minusDays(7);
        }

        // 根据日志类型设置查询模式
        if (logType != null) {
            if (logType == 4) {
                alarmOnly = true;
            } else if (logType == 3) {
                inspectionOnly = true;
            } else if (logType == 1) {
                statusChangeOnly = true;
            }
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
package com.mes.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 维护记录查询DTO
 */
@Data
public class MaintenanceQueryDTO {

    // 基础查询条件
    private String maintenanceNo;
    private Long equipmentId;
    private String equipmentCode;
    private String equipmentName;

    // 维护信息
    private Integer recordType;     // 记录类型：1-保养,2-维修,3-点检,4-校准,5-预防性维护
    private String subType;         // 子类型：日常保养/一级保养/二级保养/大修/紧急维修等
    private Integer status;         // 维护状态：1-已计划,2-进行中,3-已完成,4-已取消,5-延期
    private Integer result;         // 维护结果：1-正常,2-异常,3-待观察

    // 人员信息
    private String maintainer;      // 维护负责人
    private String maintainerId;    // 维护负责人ID
    private String maintenanceTeam; // 维护班组
    private String acceptor;        // 验收人

    // 时间范围查询
    private LocalDateTime planStartTimeStart;  // 计划开始时间-开始
    private LocalDateTime planStartTimeEnd;    // 计划开始时间-结束
    private LocalDateTime actualStartTimeStart; // 实际开始时间-开始
    private LocalDateTime actualStartTimeEnd;   // 实际开始时间-结束
    private LocalDateTime createTimeStart;     // 创建时间-开始
    private LocalDateTime createTimeEnd;       // 创建时间-结束

    // 其他条件
    private String workshop;        // 所属车间
    private String equipmentType;   // 设备类型
    private Boolean isOverdue;      // 是否查询超期维护
    private Boolean needAcceptance; // 是否需要验收

    // 关联条件
    private Long relatedFaultId;    // 关联的故障ID

    // 分页参数
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页记录数必须大于0")
    private Integer pageSize = 20;

    private String sortField = "createTime";
    private String sortOrder = "desc";

    // 关键词搜索
    private String keyword;  // 支持维护单号、维护内容、维护描述的模糊查询

    // 统计模式
    private Boolean statisticsMode = false;
    private String statisticsType;  // daily, weekly, monthly

    // 导出模式
    private Boolean exportMode = false;
    private String exportFormat;

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

        // 设置默认时间范围（最近30天）
        if (planStartTimeStart == null && planStartTimeEnd == null &&
                actualStartTimeStart == null && actualStartTimeEnd == null &&
                createTimeStart == null && createTimeEnd == null) {
            createTimeEnd = LocalDateTime.now();
            createTimeStart = createTimeEnd.minusDays(30);
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

    /**
     * 判断是否需要关联设备信息
     */
    public boolean needEquipmentInfo() {
        return equipmentCode != null || equipmentName != null ||
                workshop != null || equipmentType != null;
    }
}
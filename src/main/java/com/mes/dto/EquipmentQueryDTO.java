package com.mes.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备查询DTO
 */
@Data
public class EquipmentQueryDTO {

    private String equipmentCode;
    private String equipmentName;
    private String equipmentType;
    private String workshop;
    private Integer status;
    private Boolean isCritical;
    private String location;
    private String responsiblePerson;
    private String model;

    // 时间范围查询
    private LocalDateTime purchaseDateStart;
    private LocalDateTime purchaseDateEnd;
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;

    // 保养相关查询
    private Boolean needMaintenance; // 是否查询需要保养的设备
    private LocalDateTime nextMaintenanceStart;
    private LocalDateTime nextMaintenanceEnd;

    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 20;
    private String sortField = "createTime";
    private String sortOrder = "desc";

    // 统计参数
    private Boolean needStatistics = false;

    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}

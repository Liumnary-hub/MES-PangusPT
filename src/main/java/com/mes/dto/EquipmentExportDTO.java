package com.mes.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 设备导出DTO（用于Excel导出）
 */
@Data
public class EquipmentExportDTO {


    private String equipmentCode;


    private String equipmentName;


    private String equipmentType;

    private String status;


    private String workshop;


    private String location;


    private String model;


    private String specification;


    private String responsiblePerson;


    private String contactPhone;


    private String isCritical;


    private Integer maintenanceCycle;


    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd")
    private LocalDateTime lastMaintenanceDate;


    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd")
    private LocalDateTime nextMaintenanceDate;


    private Double totalRunningHours;


    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

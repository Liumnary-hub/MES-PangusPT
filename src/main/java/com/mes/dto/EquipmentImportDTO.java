package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 设备导入DTO（用于Excel导入）
 */
@Data
public class EquipmentImportDTO {


    @NotBlank(message = "设备编码不能为空")
    private String equipmentCode;


    @NotBlank(message = "设备名称不能为空")
    private String equipmentName;


    private String equipmentType;


    private String workshop;


    private String location;


    private String model;


    private String specification;


    private String responsiblePerson;


    private String contactPhone;


    private Integer maintenanceCycle;


    private String isCriticalStr;

    // 转换方法
    public Boolean getIsCritical() {
        return "是".equals(isCriticalStr) || "YES".equalsIgnoreCase(isCriticalStr)
                || "TRUE".equalsIgnoreCase(isCriticalStr) || "1".equals(isCriticalStr);
    }
}

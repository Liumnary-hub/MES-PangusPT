package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备创建/更新DTO
 */
@Data
public class EquipmentDTO {

    private Long equipmentId;

    @NotBlank(message = "设备编码不能为空")
    @Size(min = 1, max = 50, message = "设备编码长度在1-50之间")
    private String equipmentCode;

    @NotBlank(message = "设备名称不能为空")
    @Size(min = 1, max = 100, message = "设备名称长度在1-100之间")
    private String equipmentName;

    @Size(max = 50, message = "设备类型长度不能超过50")
    private String equipmentType;

    @NotNull(message = "设备状态不能为空")
    private Integer status;

    @Size(max = 100, message = "车间名称长度不能超过100")
    private String workshop;

    @Size(max = 100, message = "位置信息长度不能超过100")
    private String location;

    @Size(max = 50, message = "设备型号长度不能超过50")
    private String model;

    @Size(max = 100, message = "设备规格长度不能超过100")
    private String specification;

    @Size(max = 200, message = "供应商名称长度不能超过200")
    private String supplier;

    @Size(max = 200, message = "制造商名称长度不能超过200")
    private String manufacturer;

    private LocalDateTime purchaseDate;
    private LocalDateTime commissioningDate;

    @Size(max = 50, message = "负责人姓名长度不能超过50")
    private String responsiblePerson;

    @Size(max = 50, message = "负责人工号长度不能超过50")
    private String responsiblePersonId;

    @Size(max = 20, message = "联系电话长度不能超过20")
    private String contactPhone;

    private Boolean isCritical = false;

    @NotNull(message = "保养周期不能为空")
    private Integer maintenanceCycle;

    private BigDecimal originalValue;
    private BigDecimal netValue;

    @Size(max = 500, message = "技术参数长度不能超过500")
    private String technicalParams;

    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;

    // 创建人信息（从Token获取，不需要前端传）
    private String createBy;
    private String updateBy;
}


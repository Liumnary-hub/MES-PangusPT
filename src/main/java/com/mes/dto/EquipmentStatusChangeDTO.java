package com.mes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备状态变更DTO
 */
@Data
public class EquipmentStatusChangeDTO {

    @NotNull(message = "设备ID不能为空")
    private Long equipmentId;

    @NotNull(message = "目标状态不能为空")
    private Integer targetStatus;

    @Size(max = 500, message = "变更原因长度不能超过500")
    private String changeReason;

    private LocalDateTime changeTime;
    private String operator;
    private String operatorId;
}

package com.mes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设备二维码DTO
 */
@Data
public class EquipmentQrCodeDTO {

    @NotNull(message = "设备ID不能为空")
    private Long equipmentId;

    private String equipmentCode;
    private String equipmentName;
    private String equipmentType;
    private String workshop;
    private String location;
    private String model;
    private String qrCodeUrl;
    private String qrCodeBase64;

    // 二维码尺寸
    private Integer width = 300;
    private Integer height = 300;

    // 是否包含Logo
    private Boolean withLogo = true;
}

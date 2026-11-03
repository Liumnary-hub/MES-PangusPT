package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class StandardCreateDTO {
    @NotBlank(message = "标准编号不能为空")
    private String standardCode;

    @NotBlank(message = "标准名称不能为空")
    private String standardName;

    @NotBlank(message = "产品编码不能为空")
    private String productCode;

    private String inspectionType;
    private List<InspectionItemDTO> items;
}

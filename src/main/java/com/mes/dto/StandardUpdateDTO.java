package com.mes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

@Data
@Schema(description = "检验标准更新参数")
public class StandardUpdateDTO {

    @Schema(description = "标准ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "标准ID不能为空")
    private Long id;

    @Schema(description = "标准名称")
    private String standardName;

    @Schema(description = "检验类型: IQC, IPQC, FQC, OQC")
    private String inspectionType;

    @Schema(description = "检验水平: I, II, III")
    private String inspectionLevel;

    @Schema(description = "AQL水平: 0.65, 1.0, 1.5, 2.5, 4.0")
    private String aqlLevel;

    @Schema(description = "接收标准")
    private String acceptanceCriteria;

    @Schema(description = "状态: DRAFT, ACTIVE, INACTIVE")
    private String status;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "检验项目列表")
    private List<InspectionItemDTO> items;

    @Schema(description = "更新人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "更新人不能为空")
    private String updateBy;
}
package com.mes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
@Schema(description = "缺陷明细参数")
public class DefectDetailDTO {

    @Schema(description = "检验记录ID")
    private Long recordId;

    @Schema(description = "缺陷代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "缺陷代码不能为空")
    private String defectCode;

    @Schema(description = "缺陷名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "缺陷名称不能为空")
    private String defectName;

    @Schema(description = "缺陷数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "缺陷数量不能为空")
    @Min(value = 1, message = "缺陷数量最小为1")
    private Integer defectQuantity = 1;

    @Schema(description = "缺陷位置")
    private String defectLocation;

    @Schema(description = "严重程度: MINOR, MAJOR, CRITICAL, FATAL")
    private String severity;

    @Schema(description = "缺陷描述")
    private String description;

    @Schema(description = "原因分析")
    private String causeAnalysis;

    @Schema(description = "临时措施")
    private String temporaryAction;

    @Schema(description = "永久措施")
    private String permanentAction;

    @Schema(description = "责任人")
    private String responsiblePerson;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "图片路径（JSON数组）")
    private String images;
}
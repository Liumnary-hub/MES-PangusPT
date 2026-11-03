package com.mes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
@Schema(description = "缺陷代码参数")
public class DefectCodeDTO {

    @Schema(description = "缺陷代码ID（更新时使用）")
    private Long id;

    @Schema(description = "缺陷代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "缺陷代码不能为空")
    @Size(min = 2, max = 50, message = "缺陷代码长度在2-50个字符")
    private String defectCode;

    @Schema(description = "缺陷名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "缺陷名称不能为空")
    @Size(min = 2, max = 100, message = "缺陷名称长度在2-100个字符")
    private String defectName;

    @Schema(description = "缺陷类型: APPEARANCE-外观, DIMENSION-尺寸, FUNCTION-功能, PERFORMANCE-性能, SAFETY-安全")
    private String defectType;

    @Schema(description = "严重程度: MINOR-轻微, MAJOR-一般, CRITICAL-严重, FATAL-致命")
    private String severity;

    @Schema(description = "缺陷描述")
    @Size(max = 1000, message = "缺陷描述长度不能超过1000个字符")
    private String description;

    @Schema(description = "处理方法")
    @Size(max = 500, message = "处理方法长度不能超过500个字符")
    private String handlingMethod;

    @Schema(description = "预防措施")
    @Size(max = 500, message = "预防措施长度不能超过500个字符")
    private String preventiveAction;

    @Schema(description = "责任部门")
    private String responsibleDept;

    @Schema(description = "是否启用", defaultValue = "true")
    private Boolean enabled = true;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
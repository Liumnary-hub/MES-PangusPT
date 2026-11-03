package com.mes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



import java.util.List;

@Data
@Schema(description = "检验任务分配参数")
public class TaskAssignDTO {

    @Schema(description = "任务ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "任务ID列表不能为空")
    private List<Long> taskIds;

    @Schema(description = "检验员", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "检验员不能为空")
    private String inspector;

    @Schema(description = "分配人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "分配人不能为空")
    private String assignBy;

    @Schema(description = "预计完成时间")
    private String estimatedCompleteTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否紧急", defaultValue = "false")
    private Boolean urgent = false;
}
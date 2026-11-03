package com.mes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
@Schema(description = "检验计划执行参数")
public class PlanExecuteDTO {

    @Schema(description = "计划ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划ID不能为空")
    private Long planId;

    @Schema(description = "产品编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "产品编码不能为空")
    private String productCode;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "批次号不能为空")
    private String batchNo;

    @Schema(description = "生产订单号")
    private String orderNo;

    @Schema(description = "工位编码")
    private String workstationCode;

    @Schema(description = "车间编码")
    private String workshopCode;

    @Schema(description = "检验员")
    private String inspector;

    @Schema(description = "任务数量", defaultValue = "1")
    @Min(value = 1, message = "任务数量最小为1")
    private Integer taskCount = 1;

    @Schema(description = "总数量")
    @Min(value = 1, message = "总数量最小为1")
    private Integer totalQuantity;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "执行人")
    private String executeBy;
}
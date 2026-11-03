package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.dto.PlanItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "检验计划更新参数")
public class PlanUpdateDTO {

    @Schema(description = "计划ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划ID不能为空")
    private Long id;

    @Schema(description = "计划名称")
    private String planName;

    @Schema(description = "计划类型: FIRST, PATROL, FINAL, SPOT")
    private String planType;

    @Schema(description = "频率: DAILY, WEEKLY, MONTHLY")
    private String frequency;

    @Schema(description = "计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planStartTime;

    @Schema(description = "计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planEndTime;

    @Schema(description = "抽样数量")
    private Integer sampleSize;

    @Schema(description = "检验员")
    private String inspector;

    @Schema(description = "状态: DRAFT, PENDING, APPROVED, ACTIVE, INACTIVE")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "检验项目列表")
    private List<PlanItemDTO> items;
}
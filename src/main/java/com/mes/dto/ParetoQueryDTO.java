package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "帕累托分析查询参数")
public class ParetoQueryDTO {

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @Schema(description = "缺陷类型")
    private String defectType;

    @Schema(description = "严重程度")
    private String severity;

    @Schema(description = "车间编码")
    private String workshopCode;

    @Schema(description = "产线编码")
    private String lineCode;

    @Schema(description = "排序方式: COUNT-按数量, QUANTITY-按缺陷数量")
    private String sortBy = "COUNT";

    @Schema(description = "是否只统计主要缺陷（前80%）", defaultValue = "true")
    private Boolean vitalFew = true;

    @Schema(description = "最小缺陷数量")
    private Integer minDefectCount = 1;

    public ParetoQueryDTO() {
        if (this.startDate == null) {
            this.startDate = LocalDate.now().minusDays(30);
        }
        if (this.endDate == null) {
            this.endDate = LocalDate.now();
        }
    }
}

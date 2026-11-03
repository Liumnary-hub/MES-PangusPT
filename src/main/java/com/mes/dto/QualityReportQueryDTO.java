package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "质量报告生成参数")
public class QualityReportQueryDTO {

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "开始日期不能为空")
    private String startDate;

    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "结束日期不能为空")
    private String endDate;

    @Schema(description = "报告类型: DAILY-日报, WEEKLY-周报, MONTHLY-月报, QUARTERLY-季报, YEARLY-年报, CUSTOM-自定义")
    private String reportType = "CUSTOM";

    @Schema(description = "车间编码")
    private String workshopCode;

    @Schema(description = "产线编码")
    private String lineCode;

    @Schema(description = "是否包含SPC分析", defaultValue = "true")
    private Boolean includeSPC = true;

    @Schema(description = "是否包含帕累托分析", defaultValue = "true")
    private Boolean includePareto = true;

    @Schema(description = "是否包含改进建议", defaultValue = "true")
    private Boolean includeSuggestions = true;

    @Schema(description = "报告格式: HTML, PDF, EXCEL")
    private String format = "HTML";

    @Schema(description = "生成人")
    private String generateBy;
}

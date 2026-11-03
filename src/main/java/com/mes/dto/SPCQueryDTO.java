package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.dto.BaseQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "SPC统计分析查询参数")
public class SPCQueryDTO {

    @Schema(description = "检验项目ID")
    private Long itemId;

    @Schema(description = "检验项目编码")
    private String itemCode;

    @Schema(description = "产品编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "产品编码不能为空")
    private String productCode;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @Schema(description = "控制图类型: XBAR_R-均值极差图, XBAR_S-均值标准差图, I_MR-单值移动极差图, P-不良率图, NP-不良数图")
    private String chartType = "XBAR_R";

    @Schema(description = "子组大小", defaultValue = "5")
    private Integer subgroupSize = 5;

    @Schema(description = "是否计算过程能力", defaultValue = "true")
    private Boolean calculateCapability = true;

    @Schema(description = "规格上限")
    private Double usl;

    @Schema(description = "规格下限")
    private Double lsl;

    @Schema(description = "目标值")
    private Double target;

    @Schema(description = "车间编码")
    private String workshopCode;

    @Schema(description = "产线编码")
    private String lineCode;

    public SPCQueryDTO() {
        // 默认查询最近30天的数据
        if (this.startDate == null) {
            this.startDate = LocalDate.now().minusDays(30);
        }
        if (this.endDate == null) {
            this.endDate = LocalDate.now();
        }
    }
}
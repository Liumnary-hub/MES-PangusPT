package com.mes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 检验计划明细项DTO
 */
@Data
@Schema(description = "检验计划明细项")
public class PlanItemDTO {

    @Schema(description = "检验项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "检验项目ID不能为空")
    private Long itemId;

    @Schema(description = "检验项目编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "检验项目编码不能为空")
    private String itemCode;

    @Schema(description = "检验项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "检验项目名称不能为空")
    private String itemName;

    @Schema(description = "是否必检", defaultValue = "true")
    private Boolean required = true;

    @Schema(description = "顺序号", defaultValue = "1")
    @Min(value = 1, message = "顺序号最小为1")
    private Integer sequence = 1;

    @Schema(description = "抽样比例（0.01-1.00）")
    @DecimalMin(value = "0.01", message = "抽样比例最小为0.01")
    @DecimalMax(value = "1.00", message = "抽样比例最大为1.00")
    private BigDecimal samplingRatio;

    @Schema(description = "固定抽样数量（优先于抽样比例）")
    @Min(value = 1, message = "抽样数量最小为1")
    private Integer sampleSize;

    @Schema(description = "检验方法: VISUAL-目视, MEASUREMENT-测量, TEST-试验, INSTRUMENT-仪器")
    private String inspectionMethod;

    @Schema(description = "检验设备/工具")
    private String equipment;

    @Schema(description = "检验标准值")
    private String standardValue;

    @Schema(description = "规格上限")
    private Double upperLimit;

    @Schema(description = "规格下限")
    private Double lowerLimit;

    @Schema(description = "检验指引说明")
    private String inspectionGuide;

    @Schema(description = "缺陷判定标准")
    private String defectCriteria;

    @Schema(description = "检验耗时（分钟）")
    @Min(value = 1, message = "检验耗时最小为1分钟")
    private Integer estimatedMinutes = 5;

    @Schema(description = "权重（用于统计分析）")
    @DecimalMin(value = "0.01", message = "权重最小为0.01")
    private BigDecimal weight = BigDecimal.ONE;

    @Schema(description = "是否关键控制点")
    private Boolean criticalControlPoint = false;

    @Schema(description = "备注")
    private String remark;

    /**
     * 计算抽样数量
     * @param totalQuantity 总数量
     * @return 抽样数量
     */
    public Integer calculateSampleSize(Integer totalQuantity) {
        if (totalQuantity == null || totalQuantity <= 0) {
            return 0;
        }

        // 优先使用固定抽样数量
        if (sampleSize != null && sampleSize > 0) {
            return Math.min(sampleSize, totalQuantity);
        }

        // 使用抽样比例
        if (samplingRatio != null && samplingRatio.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal size = new BigDecimal(totalQuantity).multiply(samplingRatio);
            int calculated = size.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            // 至少抽1个，最多不超过总数
            return Math.max(1, Math.min(calculated, totalQuantity));
        }

        // 默认抽检1个
        return 1;
    }

    /**
     * 验证规格上下限
     */
    public void validateLimits() {
        if (upperLimit != null && lowerLimit != null) {
            if (upperLimit <= lowerLimit) {
                throw new IllegalArgumentException("规格上限必须大于规格下限");
            }
        }
    }

    /**
     * 获取检验方法的显示名称
     */
    public String getInspectionMethodDisplay() {
        if (inspectionMethod == null) {
            return "未定义";
        }

        switch (inspectionMethod.toUpperCase()) {
            case "VISUAL":
                return "目视检验";
            case "MEASUREMENT":
                return "测量检验";
            case "TEST":
                return "试验检验";
            case "INSTRUMENT":
                return "仪器检验";
            default:
                return inspectionMethod;
        }
    }

    /**
     * 判断是否需要数值检验
     */
    public boolean isNumericInspection() {
        return upperLimit != null || lowerLimit != null;
    }

    /**
     * 获取检验项描述
     */
    public String getItemDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(itemName);

        if (isNumericInspection()) {
            sb.append("(");
            if (lowerLimit != null && upperLimit != null) {
                sb.append(lowerLimit).append("~").append(upperLimit);
            } else if (lowerLimit != null) {
                sb.append("≥").append(lowerLimit);
            } else if (upperLimit != null) {
                sb.append("≤").append(upperLimit);
            }
            sb.append(")");
        } else if (standardValue != null) {
            sb.append("(").append(standardValue).append(")");
        }

        return sb.toString();
    }

    /**
     * 验证必填字段
     */
    public void validateRequiredFields() {
        if (itemId == null) {
            throw new IllegalArgumentException("检验项目ID不能为空");
        }
        if (itemCode == null || itemCode.trim().isEmpty()) {
            throw new IllegalArgumentException("检验项目编码不能为空");
        }
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("检验项目名称不能为空");
        }
        if (sequence == null || sequence < 1) {
            sequence = 1;
        }
        if (estimatedMinutes == null || estimatedMinutes < 1) {
            estimatedMinutes = 5;
        }
    }
}
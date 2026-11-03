package com.mes.dto;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// InspectionItemDTO.java
@Data
@Schema(description = "检验项目DTO")
public class InspectionItemDTO {

    @Schema(description = "项目ID（更新时使用）")
    private Long id;

    @Schema(description = "项目编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目编码不能为空")
    @Size(min = 2, max = 50, message = "项目编码长度在2-50个字符")
    private String itemCode;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "项目名称不能为空")
    @Size(min = 2, max = 100, message = "项目名称长度在2-100个字符")
    private String itemName;

    @Schema(description = "检验方法: 目视, 测量, 试验, 仪器")
    private String inspectionMethod;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "标准值")
    private String standardValue;

    @Schema(description = "上限值")
    @DecimalMin(value = "0", message = "上限值不能小于0")
    private Double upperLimit;

    @Schema(description = "下限值")
    @DecimalMin(value = "0", message = "下限值不能小于0")
    private Double lowerLimit;

    @Schema(description = "数据类型: numeric, text, option", defaultValue = "numeric")
    @NotBlank(message = "数据类型不能为空")
    private String dataType = "numeric";

    @Schema(description = "选项值（JSON格式，当dataType=option时使用）")
    private String options;

    @Schema(description = "是否关键项", defaultValue = "false")
    private Boolean isCritical = false;

    @Schema(description = "排序", defaultValue = "1")
    @Min(value = 1, message = "排序值最小为1")
    private Integer sequence = 1;

    @Schema(description = "使用设备")
    private String equipment;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "验证规则")
    public void validate() {
        if ("numeric".equals(dataType)) {
            if (upperLimit == null && lowerLimit == null) {
                throw new ValidationException("数值类型必须设置上限或下限");
            }
            if (upperLimit != null && lowerLimit != null && upperLimit <= lowerLimit) {
                throw new ValidationException("上限值必须大于下限值");
            }
        } else if ("option".equals(dataType)) {
            if (StringUtils.isEmpty(options)) {
                throw new ValidationException("选项类型必须设置选项值");
            }
        }
    }
}
package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QualityRecordDTO {

    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @NotNull(message = "检验项目ID不能为空")
    private Long itemId;

    @NotBlank(message = "检验项目编码不能为空")
    private String itemCode;

    @NotBlank(message = "检验项目名称不能为空")
    private String itemName;

    @NotNull(message = "实际值不能为空")
    private Double actualValue;

    private String unit;
    private Double upperLimit;
    private Double lowerLimit;

    @NotBlank(message = "检验结果不能为空")
    private String result; // PASS/FAIL

    private String defectCode;
    private String defectDescription;

    private String images; // JSON数组
    private String remark;

    @NotBlank(message = "检验员不能为空")
    private String inspector;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionTime;
}

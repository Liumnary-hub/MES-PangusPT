package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TaskSubmitDTO {
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @NotNull(message = "检验数量不能为空")
    private Integer quantityChecked;

    @NotNull(message = "合格数量不能为空")
    private Integer quantityQualified;

    @NotNull(message = "缺陷数量不能为空")
    private Integer quantityDefective;

    @NotBlank(message = "检验结果不能为空")
    private String result;

    // 改为DTO类型而不是Entity类型
    private List<QualityRecordDTO> records;
    private List<DefectDetailDTO> defects;
    private String remark;
}
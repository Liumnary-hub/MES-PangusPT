package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PickListCreateDTO {
    @NotNull(message = "工单ID不能为空")
    private Long workOrderId;

    @NotBlank(message = "工单编号不能为空")
    private String workOrderCode;

    private Long taskId;
    private String taskCode;

    private List<PickListItemDTO> items;
}
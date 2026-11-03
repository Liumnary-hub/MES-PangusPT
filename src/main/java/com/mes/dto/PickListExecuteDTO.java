package com.mes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PickListExecuteDTO {
    @NotNull(message = "领料单ID不能为空")
    private Long pickListId;

    @NotBlank(message = "领料员ID不能为空")
    private String pickerId;

    @NotBlank(message = "领料员姓名不能为空")
    private String pickerName;

    private List<PickListExecuteItemDTO> executeItems;
}
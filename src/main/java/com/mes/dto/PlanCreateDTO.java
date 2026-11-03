package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanCreateDTO {

    @NotBlank(message = "计划名称不能为空")
    private String planName;

    @NotBlank(message = "产品编码不能为空")
    private String productCode;

    @NotBlank(message = "标准编号不能为空")
    private String standardCode;

    @NotBlank(message = "计划类型不能为空")
    private String planType; // FIRST首检, PATROL巡检, FINAL末检

    @NotNull(message = "计划日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planDate; // 计划日期

    private String frequency; // DAILY, WEEKLY, MONTHLY

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planEndTime;

    private Integer sampleSize; // 抽样数量
    private Integer totalQuantity; // 总数量

    @NotNull(message = "检验项目不能为空")
    @Valid
    private List<PlanItemDTO> items;

    private String remark;
    private String createBy;
}
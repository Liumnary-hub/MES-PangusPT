package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatrolRecordDTO {

    @NotBlank(message = "巡检点编码不能为空")
    private String patrolPointCode;

    @NotBlank(message = "巡检点名称不能为空")
    private String patrolPointName;

    @NotBlank(message = "巡检路线不能为空")
    private String patrolRoute;

    @NotNull(message = "巡检时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime patrolTime;

    @NotBlank(message = "巡检员不能为空")
    private String inspector;

    private String checkItems; // JSON数组，检查项
    private String checkResults; // JSON数组，检查结果

    private String abnormalities; // 异常情况
    private String handlingMeasures; // 处理措施

    private String images; // 现场图片

    @NotBlank(message = "巡检结果不能为空")
    private String patrolResult; // NORMAL, ABNORMAL

    private String remark;
    private String createBy;
}
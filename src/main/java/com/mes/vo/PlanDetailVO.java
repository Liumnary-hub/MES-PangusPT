package com.mes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.entity.QualityPlanItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanDetailVO {
    private Long id;
    private String planNo;
    private String planName;
    private String planType;
    private String planTypeName;
    private Long productId;
    private String productCode;
    private String productName;
    private Long standardId;
    private String standardCode;
    private String standardName;
    private String inspectionType;
    private String status;
    private String statusName;
    private String createBy;
    private String updateBy;
    private String approver;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTime;

    private Integer quantity;           // 计划数量
    private Integer frequency;          // 检验频次
    private String frequencyUnit;       // 频次单位
    private Boolean isAutoGenerate;     // 是否自动生成任务
    private String remark;

    private List<QualityPlanItem> items; // 计划明细
}

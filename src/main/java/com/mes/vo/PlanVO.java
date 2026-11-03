package com.mes.vo;// PlanVO.java - 检验计划列表VO


import com.fasterxml.jackson.annotation.JsonFormat;

import com.mes.entity.QualityPlanItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanVO {
    private Long id;
    private String planNo;
    private String planName;
    private String planType;
    private String planTypeName;
    private String productCode;
    private String productName;
    private String standardCode;
    private String standardName;
    private String status;
    private String statusName;
    private String createBy;
    private String approver;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalTime;

    private Integer itemCount;          // 检验项目数量
    private Integer taskCount;          // 生成任务数量
    private Integer completedTaskCount; // 完成任务数量
    private String remark;

    private List<QualityPlanItem> items; // 计划明细（查询时可选）
}




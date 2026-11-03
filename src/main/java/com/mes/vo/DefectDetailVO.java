package com.mes.vo;// DefectDetailVO.java - 缺陷明细VO


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DefectDetailVO {
    private Long id;
    private Long taskId;
    private String taskNo;
    private String defectCode;
    private String defectName;
    private String defectType;
    private String defectTypeName;
    private String severity;
    private String severityName;
    private Integer quantity;          // 缺陷数量
    private String position;           // 缺陷位置
    private String causeAnalysis;      // 原因分析
    private String correctiveAction;   // 纠正措施
    private String preventiveAction;   // 预防措施
    private String responsiblePerson;  // 责任人
    private String status;             // 处理状态
    private String statusName;
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeTime;

    private String remark;
    private String attachments;        // 附件（JSON格式存储文件信息）
}



package com.mes.vo;// QualityAlertVO.java - 质量预警VO


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QualityAlertVO {
    private Long id;
    private String alertNo;             // 预警编号
    private String alertType;           // 预警类型（缺陷率/SPC/超时/巡检异常）
    private String alertLevel;          // 预警级别（紧急/重要/一般）
    private String alertTitle;          // 预警标题
    private String alertContent;        // 预警内容
    private String sourceType;          // 来源类型（任务/记录/计划/缺陷）
    private Long sourceId;              // 来源ID
    private String sourceNo;            // 来源编号（如任务编号）
    private String productCode;         // 产品编码
    private String productName;         // 产品名称
    private String workstationCode;     // 工位编码
    private String inspector;           // 检验员
    private String status;              // 状态（未处理/处理中/已处理/已关闭）
    private String handler;             // 处理人
    private String handleResult;        // 处理结果
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime alertTime;    // 预警时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;   // 处理时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeTime;    // 关闭时间

    private Boolean isRead;             // 是否已读
    private Boolean isAcknowledged;     // 是否已确认
    private String remark;
    private String attachments;         // 附件
}




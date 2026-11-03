package com.mes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QualityStandardVO {
    private Long id;
    private String standardCode;
    private String standardName;
    private String version;
    private String inspectionType;
    private String productCode;
    private String productName;
    private String status;
    private String statusName;
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireDate;

    private Long itemCount;          // 检验项目数量（改为Long类型）
    private String remark;
}
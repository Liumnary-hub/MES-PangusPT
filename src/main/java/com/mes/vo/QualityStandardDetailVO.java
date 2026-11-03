package com.mes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.entity.InspectionItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QualityStandardDetailVO {
    private Long id;
    private String standardCode;
    private String standardName;
    private String version;
    private String inspectionType;
    private String inspectionTypeName;
    private Long productId;
    private String productCode;
    private String productName;
    private String specification;
    private String measurementUnit;
    private String status;
    private String statusName;
    private String createBy;
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireDate;

    private List<InspectionItem> items;  // 检验项目列表
    private String remark;
}

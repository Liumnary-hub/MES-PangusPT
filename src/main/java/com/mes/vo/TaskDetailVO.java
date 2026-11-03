package com.mes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.entity.DefectDetail;
import com.mes.entity.QualityRecord;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDetailVO {
    private Long id;
    private String taskNo;
    private Long planId;
    private String planNo;
    private String planName;
    private Long standardId;
    private String standardCode;
    private String standardName;
    private String inspectionType;
    private String inspectionTypeName;
    private String productCode;
    private String productName;
    private String batchNo;
    private String orderNo;
    private String workstationCode;
    private String workstationName;
    private String inspector;
    private String inspectorName;
    private String status;
    private String statusName;
    private String result;
    private String resultName;
    private Integer quantityChecked;
    private Integer quantityQualified;
    private Integer quantityDefective;
    private Double passRate;
    private String createBy;
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    private Boolean isUrgent;
    private String sampleMethod;       // 抽样方法
    private Integer sampleSize;        // 样本大小
    private String sampleStandard;     // 抽样标准
    private String remark;

    private List<QualityRecord> records;   // 检验记录
    private List<DefectDetail> defects;    // 缺陷明细
}

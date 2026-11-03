package com.mes.vo;// TaskVO.java - 检验任务列表VO



import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.entity.DefectDetail;
import com.mes.entity.QualityRecord;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskVO {
    private Long id;
    private String taskNo;
    private String planNo;
    private String planName;
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
    private Double passRate;           // 合格率
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTime;

    private Boolean timeout;           // 是否超时
    private Long timeoutMinutes;       // 超时时长（分钟）
    private Boolean isUrgent;          // 是否紧急
    private String remark;

    private List<QualityRecord> records;   // 检验记录（查询时可选）
    private List<DefectDetail> defects;    // 缺陷明细（查询时可选）
}

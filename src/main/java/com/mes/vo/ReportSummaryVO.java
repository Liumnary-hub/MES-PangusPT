package com.mes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportSummaryVO {
    private String reportNo;
    private String reportTitle;
    private String reportType;
    private String period;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generateTime;

    private String generateBy;
    private String department;
    private Double overallPassRate;
    private Double defectRate;
    private Integer keyIssueCount;
    private String summary;
}
package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StatisticQueryDTO {

    @NotBlank(message = "开始日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String startDate;

    @NotBlank(message = "结束日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String endDate;

    private String productCode;
    private String productName;
    private String workshopCode;
    private String lineCode;

    private String inspectionType; // IQC, IPQC, FQC
    private String statisticType; // DAILY, WEEKLY, MONTHLY

    private String groupBy; // PRODUCT, LINE, INSPECTOR, DAY

    public StatisticQueryDTO() {
        this.startDate = LocalDate.now().minusDays(30).toString();
        this.endDate = LocalDate.now().toString();
        this.statisticType = "DAILY";
        this.groupBy = "DAY";
    }
}
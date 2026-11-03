package com.mes.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SPCAbnormalPoint {
    private Long recordId;              // 记录ID
    private String itemName;            // 项目名称
    private Double actualValue;         // 实际值
    private Double mean;                // 均值
    private Double stdDev;              // 标准差
    private Double zScore;              // Z分数
    private String rule;                // 违反规则

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 位置信息
    private String subgroupNo;          // 子组编号
    private String batchNo;             // 批次号
    private String inspector;           // 检验员
    private String workstation;         // 工位

    // 异常分类
    private String abnormalityType;     // 异常类型
    private String severity;            // 严重程度
    private Boolean needsInvestigation; // 是否需要调查
}

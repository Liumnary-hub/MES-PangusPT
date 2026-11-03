package com.mes.vo;// QualityRecordVO.java - 质量记录VO


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QualityRecordVO {
    private Long id;
    private Long taskId;
    private String taskNo;
    private Long itemId;
    private String itemCode;
    private String itemName;
    private String dataType;
    private Double nominalValue;       // 标称值
    private Double upperLimit;         // 上限值
    private Double lowerLimit;         // 下限值
    private Double actualValue;        // 实测值
    private String textValue;          // 文本值
    private String optionValue;        // 选项值
    private Boolean booleanValue;      // 布尔值
    private String unit;               // 单位
    private String result;             // 单项结果（合格/不合格）
    private String operator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String remark;
    private Boolean isCritical;        // 是否关键项目
}

// RecordStatisticVO.java - 记录统计VO



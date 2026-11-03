package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.dto.BaseQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "检验记录查询参数")
public class QualityRecordQueryDTO extends BaseQueryDTO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "任务编号")
    private String taskNo;

    @Schema(description = "计划编号")
    private String planNo;

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "检验项目编码")
    private String itemCode;

    @Schema(description = "检验项目名称")
    private String itemName;

    @Schema(description = "检验员")
    private String inspector;

    @Schema(description = "检验结果: QUALIFIED, DEFECTIVE")
    private String result;

    @Schema(description = "缺陷代码")
    private String defectCode;

    @Schema(description = "检验时间开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionTimeStart;

    @Schema(description = "检验时间结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionTimeEnd;

    @Schema(description = "数据类型: NUMERIC, TEXT, OPTION")
    private String dataType;

    @Schema(description = "是否关键项")
    private Boolean isCritical;

    @Schema(description = "是否包含缺陷详情", defaultValue = "false")
    private Boolean includeDefectDetails = false;

    public QualityRecordQueryDTO() {
        // 默认查询最近30天的数据
        if (this.inspectionTimeStart == null) {
            this.inspectionTimeStart = LocalDateTime.now().minusDays(30);
        }
        if (this.inspectionTimeEnd == null) {
            this.inspectionTimeEnd = LocalDateTime.now();
        }
    }
}
package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.dto.BaseQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "巡检记录查询参数")
public class PatrolQueryDTO extends BaseQueryDTO {

    @Schema(description = "巡检点编码")
    private String patrolPointCode;

    @Schema(description = "巡检点名称")
    private String patrolPointName;

    @Schema(description = "巡检路线")
    private String patrolRoute;

    @Schema(description = "巡检员")
    private String inspector;

    @Schema(description = "巡检结果: NORMAL-正常, ABNORMAL-异常")
    private String patrolResult;

    @Schema(description = "是否处理")
    private Boolean handled;

    @Schema(description = "巡检开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime patrolTimeStart;

    @Schema(description = "巡检结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime patrolTimeEnd;

    @Schema(description = "车间编码")
    private String workshopCode;

    @Schema(description = "产线编码")
    private String lineCode;

    @Schema(description = "设备编码")
    private String equipmentCode;

    @Schema(description = "异常类型")
    private String abnormalType;

    @Schema(description = "是否包含图片", defaultValue = "false")
    private Boolean includeImages = false;

    public PatrolQueryDTO() {
        // 默认查询最近7天的巡检记录
        if (this.patrolTimeStart == null) {
            this.patrolTimeStart = LocalDateTime.now().minusDays(7);
        }
        if (this.patrolTimeEnd == null) {
            this.patrolTimeEnd = LocalDateTime.now();
        }
    }
}
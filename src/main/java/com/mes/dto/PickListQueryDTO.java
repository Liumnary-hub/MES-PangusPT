package com.mes.dto;  // 确保包名正确

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 领料单查询参数
 */
@Data
public class PickListQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String pickListCode;
    private Long workOrderId;
    private String workOrderCode;
    private Long taskId;
    private String taskCode;
    private String status;
    private String pickerId;
    private String pickerName;
    private String materialCode;
    private String materialName;
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;
    private LocalDateTime pickTimeStart;
    private LocalDateTime pickTimeEnd;
}
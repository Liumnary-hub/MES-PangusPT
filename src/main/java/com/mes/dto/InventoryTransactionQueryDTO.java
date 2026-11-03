package com.mes.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库存交易记录查询参数
 */
@Data
public class InventoryTransactionQueryDTO {

    /** 页码 */
    private Integer pageNum = 1;

    /** 页大小 */
    private Integer pageSize = 10;

    /** 交易类型 */
    private String transactionType;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 关联类型 */
    private String referenceType;

    /** 关联单号 */
    private String referenceCode;

    /** 批次号 */
    private String batchNumber;

    /** 操作员姓名 */
    private String operatorName;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 库位编码 */
    private String locationCode;

    /** 仓库编码 */
    private String warehouseCode;
}
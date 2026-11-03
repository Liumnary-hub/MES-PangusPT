package com.mes.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存流水账视图对象
 */
@Data
public class InventoryLedgerVO {

    private Long id;

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;

    /**
     * 交易类型
     */
    private String transactionType;

    /**
     * 交易类型描述
     */
    private String transactionTypeDesc;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 批次号
     */
    private String batchNumber;

    /**
     * 关联单号
     */
    private String referenceCode;

    /**
     * 入库数量
     */
    private BigDecimal inQuantity;

    /**
     * 出库数量
     */
    private BigDecimal outQuantity;

    /**
     * 结存数量
     */
    private BigDecimal balanceQuantity;

    /**
     * 操作员
     */
    private String operatorName;

    /**
     * 备注
     */
    private String remark;
}

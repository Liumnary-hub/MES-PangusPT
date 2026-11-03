package com.mes.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存交易统计视图对象
 */
@Data
public class InventoryTransactionStatVO {

    /** 统计日期 */
    private LocalDate statDate;

    /** 交易类型 */
    private String transactionType;

    /** 交易类型描述 */
    private String transactionTypeDesc;

    /** 物料类型 */
    private String materialType;

    /** 物料类型描述 */
    private String materialTypeDesc;

    /** 交易笔数 */
    private Integer transactionCount;

    /** 交易总量 */
    private BigDecimal totalQuantity;

    /** 入库数量 */
    private BigDecimal inQuantity;

    /** 出库数量 */
    private BigDecimal outQuantity;

    /** 净入库数量 */
    private BigDecimal netInQuantity;
}


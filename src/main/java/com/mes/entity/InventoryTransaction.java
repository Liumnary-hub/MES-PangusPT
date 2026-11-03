package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存交易记录实体
 */
@Data
@TableName("mes_inventory_transaction")
public class InventoryTransaction {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 交易类型: IN-入库, OUT-出库, TRANSFER-转移, ADJUST-调整 */
    private String transactionType;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 来源库位ID */
    private Long fromLocationId;

    /** 来源库位编码 */
    private String fromLocationCode;

    /** 目标库位ID */
    private Long toLocationId;

    /** 目标库位编码 */
    private String toLocationCode;

    /** 交易数量 */
    private BigDecimal quantity;

    /** 批次号 */
    private String batchNumber;

    /** 关联类型: WORK_ORDER-工单, PICK_LIST-领料单, ADJUSTMENT-调整 */
    private String referenceType;

    /** 关联ID */
    private Long referenceId;

    /** 关联单号 */
    private String referenceCode;

    /** 交易时间 */
    private LocalDateTime transactionTime;

    /** 操作员ID */
    private String operatorId;

    /** 操作员姓名 */
    private String operatorName;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
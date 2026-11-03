package com.mes.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存交易记录视图对象
 */
@Data
public class InventoryTransactionVO {

    private Long id;

    /** 交易类型: IN-入库, OUT-出库, TRANSFER-转移, ADJUST-调整 */
    private String transactionType;

    /** 交易类型描述 */
    private String transactionTypeDesc;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 物料类型 */
    private String materialType;

    /** 规格型号 */
    private String specification;

    /** 计量单位 */
    private String unit;

    /** 来源库位ID */
    private Long fromLocationId;

    /** 来源库位编码 */
    private String fromLocationCode;

    /** 来源库位名称 */
    private String fromLocationName;

    /** 来源仓库编码 */
    private String fromWarehouseCode;

    /** 来源仓库名称 */
    private String fromWarehouseName;

    /** 目标库位ID */
    private Long toLocationId;

    /** 目标库位编码 */
    private String toLocationCode;

    /** 目标库位名称 */
    private String toLocationName;

    /** 目标仓库编码 */
    private String toWarehouseCode;

    /** 目标仓库名称 */
    private String toWarehouseName;

    /** 交易数量 */
    private BigDecimal quantity;

    /** 批次号 */
    private String batchNumber;

    /** 关联类型: WORK_ORDER-工单, PICK_LIST-领料单, ADJUSTMENT-调整 */
    private String referenceType;

    /** 关联类型描述 */
    private String referenceTypeDesc;

    /** 关联ID */
    private Long referenceId;

    /** 关联单号 */
    private String referenceCode;

    /** 关联单号描述（如工单的产品名称） */
    private String referenceDescription;

    /** 交易时间 */
    private LocalDateTime transactionTime;

    /** 操作员ID */
    private String operatorId;

    /** 操作员姓名 */
    private String operatorName;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createTime;

    // 扩展字段
    /** 交易方向：1-入库，-1-出库，0-转移 */
    private Integer direction;

    /** 交易后库存数量 */
    private BigDecimal afterQuantity;

    /** 是否影响库存 */
    private Boolean affectInventory;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
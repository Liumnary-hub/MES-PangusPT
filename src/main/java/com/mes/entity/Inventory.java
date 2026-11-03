package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 库存实体
 */
@Data
@TableName("mes_inventory")
public class Inventory {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 库位ID */
    private Long locationId;

    /** 库位编码 */
    private String locationCode;

    /** 当前数量 */
    private BigDecimal currentQuantity;

    /** 锁定数量(已分配未出库) */
    private BigDecimal lockedQuantity;

    /** 可用数量 */
    private BigDecimal availableQuantity;

    /** 批次号 */
    private String batchNumber;

    /** 供应商批次 */
    private String supplierBatch;

    /** 生产日期 */
    private LocalDate productionDate;

    /** 有效期至 */
    private LocalDate expiryDate;

    /** 质量状态: qualified-合格, unqualified-不合格, quarantined-隔离 */
    private String qualityStatus;

    /** 最后更新时间 */
    private LocalDateTime lastUpdateTime;
}
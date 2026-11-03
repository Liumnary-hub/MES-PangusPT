package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BOM明细实体
 */
@Data
@TableName("mes_bom_detail")
public class BomDetail {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** BOM ID */
    private Long bomId;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 数量 */
    private BigDecimal quantity;

    /** 单位 */
    private String unit;

    /** 损耗率(%) */
    private BigDecimal lossRate;

    /** 顺序 */
    private Integer sequence;

    /** 使用工序 */
    private String processStep;

    /** 是否关键物料 */
    private Boolean isKeyMaterial;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
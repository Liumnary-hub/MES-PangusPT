package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * BOM物料清单实体
 */
@Data
@TableName("mes_bom")
public class Bom {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** BOM编码 */
    private String bomCode;

    /** 产品ID */
    private Long productId;

    /** 产品编码 */
    private String productCode;

    /** BOM版本 */
    private String version;

    /** 状态: active-启用, inactive-停用 */
    private String status;

    /** 生效日期 */
    private LocalDateTime effectiveDate;

    /** 失效日期 */
    private LocalDateTime expiryDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableLogic
    private Integer isDeleted;
}
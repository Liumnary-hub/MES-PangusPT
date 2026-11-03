package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("mes_pick_list_detail")
public class PickListDetail {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 领料单ID */
    private Long pickListId;

    /** 物料ID */
    private Long materialId;

    /** 物料编码 */
    private String materialCode;

    /** 库位ID */
    private Long locationId;

    /** 库位编码 - 添加这个字段 */
    private String locationCode;

    /** 批次号 */
    private String batchNumber;

    /** 需求数量 */
    private BigDecimal requiredQuantity;

    /** 实拣数量 */
    private BigDecimal pickedQuantity;

    /** 状态: pending-待拣选, picked-已拣选 */
    private String status;

    /** 备注 */
    private String remark;
}
package com.mes.vo;

import com.mes.entity.DefectDetail;
import com.mes.entity.Inventory;
import com.mes.entity.PickList;
import com.mes.entity.PickListDetail;
import com.mes.entity.ProductionOrder;
import com.mes.entity.ProductionTask;
import com.mes.entity.QualityTask;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 批次全流程追溯结果
 */
@Data
public class BatchTraceVO {

    private String batchNo;

    /** 质量检验任务 */
    private List<QualityTask> inspectionTasks = new ArrayList<>();

    /** 该批次检出的缺陷明细 */
    private List<DefectDetail> defects = new ArrayList<>();

    /** 关联生产工单 */
    private List<ProductionOrder> orders = new ArrayList<>();

    /** 工单下的工序任务 */
    private List<ProductionTask> processTasks = new ArrayList<>();

    /** 领料单 */
    private List<PickList> pickLists = new ArrayList<>();

    /** 该批次的领料明细（物料） */
    private List<PickListDetail> pickedMaterials = new ArrayList<>();

    /** 该批次的库存记录 */
    private List<Inventory> inventoryLots = new ArrayList<>();
}

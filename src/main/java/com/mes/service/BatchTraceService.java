package com.mes.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mes.entity.DefectDetail;
import com.mes.entity.Inventory;
import com.mes.entity.PickList;
import com.mes.entity.PickListDetail;
import com.mes.entity.ProductionOrder;
import com.mes.entity.ProductionTask;
import com.mes.entity.QualityTask;
import com.mes.mapper.DefectDetailMapper;
import com.mes.mapper.InventoryMapper;
import com.mes.mapper.PickListDetailMapper;
import com.mes.mapper.PickListMapper;
import com.mes.mapper.ProductionOrderMapper;
import com.mes.mapper.ProductionTaskMapper;
import com.mes.mapper.QualityTaskMapper;
import com.mes.vo.BatchTraceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 批次追溯：批次 -> 质量任务 -> 工单 -> 工序任务 -> 领料/库存批次
 */
@Service
@RequiredArgsConstructor
public class BatchTraceService {

    private final QualityTaskMapper qualityTaskMapper;
    private final DefectDetailMapper defectDetailMapper;
    private final PickListMapper pickListMapper;
    private final PickListDetailMapper pickListDetailMapper;
    private final ProductionOrderMapper productionOrderMapper;
    private final ProductionTaskMapper productionTaskMapper;
    private final InventoryMapper inventoryMapper;

    @Cacheable(cacheNames = "batchTrace", key = "#batchNo")
    public BatchTraceVO traceByBatchNo(String batchNo) {
        BatchTraceVO trace = new BatchTraceVO();
        trace.setBatchNo(batchNo);

        List<QualityTask> inspectionTasks = qualityTaskMapper.selectList(
                new LambdaQueryWrapper<QualityTask>().eq(QualityTask::getBatchNo, batchNo));
        trace.setInspectionTasks(inspectionTasks);

        List<DefectDetail> defects = defectDetailMapper.selectList(
                new LambdaQueryWrapper<DefectDetail>().eq(DefectDetail::getBatchNo, batchNo));
        trace.setDefects(defects);

        List<PickListDetail> pickedMaterials = pickListDetailMapper.selectByBatchNumber(batchNo);
        trace.setPickedMaterials(pickedMaterials);

        Set<Long> pickListIds = pickedMaterials.stream()
                .map(PickListDetail::getPickListId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<PickList> pickLists = pickListIds.isEmpty()
                ? new ArrayList<>()
                : pickListMapper.selectBatchIds(pickListIds);
        trace.setPickLists(pickLists);

        Set<String> orderCodes = new LinkedHashSet<>();
        inspectionTasks.stream()
                .map(QualityTask::getOrderNo)
                .filter(StringUtils::hasText)
                .forEach(orderCodes::add);
        pickLists.stream()
                .map(PickList::getWorkOrderCode)
                .filter(StringUtils::hasText)
                .forEach(orderCodes::add);

        List<ProductionOrder> orders = orderCodes.isEmpty()
                ? new ArrayList<>()
                : productionOrderMapper.selectList(new LambdaQueryWrapper<ProductionOrder>()
                        .in(ProductionOrder::getOrderCode, orderCodes));
        trace.setOrders(orders);

        Set<Long> orderIds = orders.stream()
                .map(ProductionOrder::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<ProductionTask> processTasks = orderIds.isEmpty()
                ? new ArrayList<>()
                : productionTaskMapper.selectList(new LambdaQueryWrapper<ProductionTask>()
                        .in(ProductionTask::getOrderId, orderIds)
                        .orderByAsc(ProductionTask::getOperationSequence));
        trace.setProcessTasks(processTasks);

        trace.setInventoryLots(inventoryMapper.selectList(
                new LambdaQueryWrapper<Inventory>().eq(Inventory::getBatchNumber, batchNo)));

        return trace;
    }
}

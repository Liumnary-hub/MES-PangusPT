package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.dto.InventoryTransactionQueryDTO;
import com.mes.entity.InventoryTransaction;
import com.mes.entity.Material;
import com.mes.entity.WarehouseLocation;
import com.mes.mapper.InventoryTransactionMapper;
import com.mes.mapper.MaterialMapper;
import com.mes.mapper.WarehouseLocationMapper;
import com.mes.service.InventoryTransactionService;
import com.mes.util.SecurityUtils;
import com.mes.util.TableDataInfo;
import com.mes.vo.InventoryTransactionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl extends ServiceImpl<InventoryTransactionMapper, InventoryTransaction> implements InventoryTransactionService {

    private final InventoryTransactionMapper transactionMapper;
    private final MaterialMapper materialMapper;
    private final WarehouseLocationMapper locationMapper;

    @Override
    public TableDataInfo<InventoryTransactionVO> selectTransactionPage(InventoryTransactionQueryDTO queryDTO) {
        int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

        Page<InventoryTransactionVO> page = new Page<>(pageNum, pageSize);
        InventoryTransactionVO queryVO = new InventoryTransactionVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        IPage<InventoryTransactionVO> result = transactionMapper.selectTransactionPage(page, queryVO);

        // 补充关联信息
        List<InventoryTransactionVO> voList = result.getRecords();
        enrichTransactionVoList(voList);

        TableDataInfo<InventoryTransactionVO> tableData = new TableDataInfo<>();
        tableData.setRows(voList);
        tableData.setTotal(result.getTotal());
        tableData.setCode(200);
        tableData.setMsg("查询成功");

        return tableData;
    }

    @Override
    public List<InventoryTransactionVO> selectTransactionList(InventoryTransactionQueryDTO queryDTO) {
        InventoryTransactionVO queryVO = new InventoryTransactionVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        Page<InventoryTransactionVO> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<InventoryTransactionVO> result = transactionMapper.selectTransactionPage(page, queryVO);

        List<InventoryTransactionVO> voList = result.getRecords();
        enrichTransactionVoList(voList);

        return voList;
    }

    @Override
    public List<InventoryTransactionVO> selectByReference(String referenceType, String referenceCode) {
        LambdaQueryWrapper<InventoryTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryTransaction::getReferenceType, referenceType);
        wrapper.eq(InventoryTransaction::getReferenceCode, referenceCode);
        wrapper.orderByAsc(InventoryTransaction::getCreateTime);

        List<InventoryTransaction> transactions = transactionMapper.selectList(wrapper);
        List<InventoryTransactionVO> voList = transactions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        enrichTransactionVoList(voList);
        return voList;
    }

    @Override
    public boolean recordInTransaction(Long materialId, Long locationId, Double quantity, String batchNumber, String referenceType, String referenceCode) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionType("IN");
        transaction.setMaterialId(materialId);
        transaction.setToLocationId(locationId);
        transaction.setQuantity(BigDecimal.valueOf(quantity));
        transaction.setBatchNumber(batchNumber);
        transaction.setReferenceType(referenceType);
        transaction.setReferenceCode(referenceCode);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setOperatorId(SecurityUtils.getUsername());
        transaction.setOperatorName(SecurityUtils.getUser().getNickName());
        transaction.setRemark("入库交易");

        return transactionMapper.insert(transaction) > 0;
    }

    @Override
    public boolean recordOutTransaction(Long materialId, Long locationId, Double quantity, String batchNumber, String referenceType, String referenceCode) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionType("OUT");
        transaction.setMaterialId(materialId);
        transaction.setFromLocationId(locationId);
        transaction.setQuantity(BigDecimal.valueOf(quantity));
        transaction.setBatchNumber(batchNumber);
        transaction.setReferenceType(referenceType);
        transaction.setReferenceCode(referenceCode);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setOperatorId(SecurityUtils.getUsername());
        transaction.setOperatorName(SecurityUtils.getUser().getNickName());
        transaction.setRemark("出库交易");

        return transactionMapper.insert(transaction) > 0;
    }

    @Override
    public boolean recordTransferTransaction(Long materialId, Long fromLocationId, Long toLocationId, Double quantity, String batchNumber, String referenceType, String referenceCode) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionType("TRANSFER");
        transaction.setMaterialId(materialId);
        transaction.setFromLocationId(fromLocationId);
        transaction.setToLocationId(toLocationId);
        transaction.setQuantity(BigDecimal.valueOf(quantity));
        transaction.setBatchNumber(batchNumber);
        transaction.setReferenceType(referenceType);
        transaction.setReferenceCode(referenceCode);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setOperatorId(SecurityUtils.getUsername());
        transaction.setOperatorName(SecurityUtils.getUser().getNickName());
        transaction.setRemark("库存转移");

        return transactionMapper.insert(transaction) > 0;
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean recordInTransaction(Long materialId, Long locationId, BigDecimal quantity,
//                                       String batchNumber, String referenceType, String referenceCode) {
//        InventoryTransaction transaction = new InventoryTransaction();
//        transaction.setTransactionType("IN");
//        transaction.setMaterialId(materialId);
//        transaction.setToLocationId(locationId);
//        transaction.setQuantity(quantity);
//        transaction.setBatchNumber(batchNumber);
//        transaction.setReferenceType(referenceType);
//        transaction.setReferenceCode(referenceCode);
//        transaction.setTransactionTime(LocalDateTime.now());
//        transaction.setOperatorId(SecurityUtils.getUsername());
//        transaction.setOperatorName(SecurityUtils.getUser().getNickName());
//        transaction.setRemark("入库交易");
//
//        return transactionMapper.insert(transaction) > 0;
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean recordOutTransaction(Long materialId, Long locationId, BigDecimal quantity,
//                                        String batchNumber, String referenceType, String referenceCode) {
//        InventoryTransaction transaction = new InventoryTransaction();
//        transaction.setTransactionType("OUT");
//        transaction.setMaterialId(materialId);
//        transaction.setFromLocationId(locationId);
//        transaction.setQuantity(quantity);
//        transaction.setBatchNumber(batchNumber);
//        transaction.setReferenceType(referenceType);
//        transaction.setReferenceCode(referenceCode);
//        transaction.setTransactionTime(LocalDateTime.now());
//        transaction.setOperatorId(SecurityUtils.getUsername());
//        transaction.setOperatorName(SecurityUtils.getUser().getNickName());
//        transaction.setRemark("出库交易");
//
//        return transactionMapper.insert(transaction) > 0;
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean recordTransferTransaction(Long materialId, Long fromLocationId, Long toLocationId,
//                                             BigDecimal quantity, String batchNumber, String referenceType, String referenceCode) {
//        InventoryTransaction transaction = new InventoryTransaction();
//        transaction.setTransactionType("TRANSFER");
//        transaction.setMaterialId(materialId);
//        transaction.setFromLocationId(fromLocationId);
//        transaction.setToLocationId(toLocationId);
//        transaction.setQuantity(quantity);
//        transaction.setBatchNumber(batchNumber);
//        transaction.setReferenceType(referenceType);
//        transaction.setReferenceCode(referenceCode);
//        transaction.setTransactionTime(LocalDateTime.now());
//        transaction.setOperatorId(SecurityUtils.getUsername());
//        transaction.setOperatorName(SecurityUtils.getUser().getNickName());
//        transaction.setRemark("库存转移");
//
//        return transactionMapper.insert(transaction) > 0;
//    }

    @Override
    public List<InventoryTransactionVO> getMaterialTransactionHistory(Long materialId) {
        LambdaQueryWrapper<InventoryTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryTransaction::getMaterialId, materialId);
        wrapper.orderByDesc(InventoryTransaction::getTransactionTime);

        List<InventoryTransaction> transactions = transactionMapper.selectList(wrapper);
        List<InventoryTransactionVO> voList = transactions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        enrichTransactionVoList(voList);
        return voList;
    }

    /**
     * 补充VO的关联信息
     */
    private void enrichTransactionVoList(List<InventoryTransactionVO> voList) {
        if (voList == null || voList.isEmpty()) {
            return;
        }

        for (InventoryTransactionVO vo : voList) {
            // 补充物料信息
            if (vo.getMaterialId() != null) {
                Material material = materialMapper.selectById(vo.getMaterialId());
                if (material != null) {
                    vo.setMaterialName(material.getMaterialName());
                    vo.setMaterialType(material.getMaterialType());
                    vo.setSpecification(material.getSpecification());
                    vo.setUnit(material.getUnit());
                }
            }

            // 补充来源库位信息
            if (vo.getFromLocationId() != null) {
                WarehouseLocation location = locationMapper.selectById(vo.getFromLocationId());
                if (location != null) {
                    vo.setFromLocationName(location.getLocationName());
                    vo.setFromWarehouseCode(location.getWarehouseCode());
                    vo.setFromWarehouseName(location.getWarehouseName());
                }
            }

            // 补充目标库位信息
            if (vo.getToLocationId() != null) {
                WarehouseLocation location = locationMapper.selectById(vo.getToLocationId());
                if (location != null) {
                    vo.setToLocationName(location.getLocationName());
                    vo.setToWarehouseCode(location.getWarehouseCode());
                    vo.setToWarehouseName(location.getWarehouseName());
                }
            }

            // 设置交易类型描述
            vo.setTransactionTypeDesc(getTransactionTypeDesc(vo.getTransactionType()));

            // 设置关联类型描述
            vo.setReferenceTypeDesc(getReferenceTypeDesc(vo.getReferenceType()));

            // 设置交易方向
            vo.setDirection(getTransactionDirection(vo.getTransactionType()));

            // 设置是否影响库存
            vo.setAffectInventory(isAffectInventory(vo.getTransactionType()));
        }
    }

    /**
     * Entity 转 VO
     */
    private InventoryTransactionVO convertToVO(InventoryTransaction entity) {
        InventoryTransactionVO vo = new InventoryTransactionVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    /**
     * 获取交易类型描述
     */
    private String getTransactionTypeDesc(String transactionType) {
        if (transactionType == null) return "";
        switch (transactionType) {
            case "IN": return "入库";
            case "OUT": return "出库";
            case "TRANSFER": return "转移";
            case "ADJUST": return "调整";
            default: return transactionType;
        }
    }

    /**
     * 获取关联类型描述
     */
    private String getReferenceTypeDesc(String referenceType) {
        if (referenceType == null) return "";
        switch (referenceType) {
            case "WORK_ORDER": return "工单";
            case "PICK_LIST": return "领料单";
            case "ADJUSTMENT": return "调整";
            case "TRANSFER": return "转移";
            default: return referenceType;
        }
    }

    /**
     * 获取交易方向
     */
    private Integer getTransactionDirection(String transactionType) {
        if (transactionType == null) return 0;
        switch (transactionType) {
            case "IN": return 1;
            case "OUT": return -1;
            case "TRANSFER":
            case "ADJUST":
            default: return 0;
        }
    }

    /**
     * 判断是否影响库存总量
     */
    private Boolean isAffectInventory(String transactionType) {
        if (transactionType == null) return false;
        return "IN".equals(transactionType) || "OUT".equals(transactionType);
    }
}
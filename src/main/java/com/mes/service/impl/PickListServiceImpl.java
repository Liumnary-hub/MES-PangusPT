package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.dto.*;

import com.mes.entity.*;
import com.mes.mapper.*;
import com.mes.service.InventoryService;
import com.mes.service.PickListService;
import com.mes.util.SecurityUtils;
import com.mes.util.TableDataInfo;
import com.mes.vo.PickListVO;
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
public class PickListServiceImpl extends ServiceImpl<PickListMapper, PickList> implements PickListService {

    private final PickListMapper pickListMapper;
    private final PickListDetailMapper pickListDetailMapper;
    private final InventoryMapper inventoryMapper;
    private final MaterialMapper materialMapper;
    private final WarehouseLocationMapper locationMapper;
    private final InventoryService inventoryService;

    @Override
    public TableDataInfo<PickListVO> selectPickListPage(PickListQueryDTO queryDTO) {
        int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

        Page<PickListVO> page = new Page<>(pageNum, pageSize);
        PickListVO queryVO = new PickListVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        IPage<PickListVO> result = pickListMapper.selectPickListPage(page, queryVO);

        // 补充领料单明细和统计信息
        List<PickListVO> voList = result.getRecords();
        for (PickListVO vo : voList) {
            List<PickListDetail> details = pickListDetailMapper.selectByPickListId(vo.getId());
            vo.setDetails(convertToDetailVOList(details));
            calculatePickListStats(vo);
        }

        TableDataInfo<PickListVO> tableData = new TableDataInfo<>();
        tableData.setRows(voList);
        tableData.setTotal(result.getTotal());
        tableData.setCode(200);
        tableData.setMsg("查询成功");

        return tableData;
    }

    @Override
    public List<PickListVO> selectPickListList(PickListQueryDTO queryDTO) {
        PickListVO queryVO = new PickListVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        Page<PickListVO> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<PickListVO> result = pickListMapper.selectPickListPage(page, queryVO);

        List<PickListVO> voList = result.getRecords();
        for (PickListVO vo : voList) {
            List<PickListDetail> details = pickListDetailMapper.selectByPickListId(vo.getId());
            vo.setDetails(convertToDetailVOList(details));
            calculatePickListStats(vo);
        }

        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPickListFromWorkOrder(Long workOrderId) {
        // TODO: 根据工单和BOM生成领料需求
        // 1. 查询工单信息
        // 2. 查询产品BOM
        // 3. 计算物料需求
        // 4. 检查库存可用性
        // 5. 生成领料单

        // 暂时返回false，需要根据具体业务实现
        log.info("根据工单生成领料单，工单ID: {}", workOrderId);
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPickList(PickListCreateDTO createDTO) {
        // 生成领料单号
        String pickListCode = generatePickListCode();

        // 保存领料单主表
        PickList pickList = new PickList();
        pickList.setPickListCode(pickListCode);
        pickList.setWorkOrderId(createDTO.getWorkOrderId());
        pickList.setWorkOrderCode(createDTO.getWorkOrderCode());
        pickList.setTaskId(createDTO.getTaskId());
        pickList.setTaskCode(createDTO.getTaskCode());
        pickList.setStatus("pending");
        pickList.setCreateUser(SecurityUtils.getUsername());
        pickList.setUpdateUser(SecurityUtils.getUsername());

        boolean pickListSaved = pickListMapper.insert(pickList) > 0;

        if (pickListSaved && createDTO.getItems() != null) {
            // 保存领料单明细
            for (PickListItemDTO itemDTO : createDTO.getItems()) {
                PickListDetail detail = new PickListDetail();
                detail.setPickListId(pickList.getId());
                detail.setMaterialId(itemDTO.getMaterialId());
                detail.setMaterialCode(itemDTO.getMaterialCode());

                // 直接使用 BigDecimal，不需要转换
                detail.setRequiredQuantity(itemDTO.getRequiredQuantity());
                detail.setPickedQuantity(BigDecimal.ZERO);
                detail.setStatus("pending");
                detail.setBatchNumber(itemDTO.getBatchNumber());
                detail.setRemark(itemDTO.getRemark());

                // 自动分配库位（如果有指定批次，则查找对应库位）
                if (StringUtils.hasText(itemDTO.getBatchNumber())) {
                    LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Inventory::getMaterialId, itemDTO.getMaterialId());
                    wrapper.eq(Inventory::getBatchNumber, itemDTO.getBatchNumber());
                    wrapper.gt(Inventory::getCurrentQuantity, 0); // 使用 current_quantity > 0
                    List<Inventory> inventories = inventoryMapper.selectList(wrapper);

                    if (!inventories.isEmpty()) {
                        Inventory inventory = inventories.get(0);
                        detail.setLocationId(inventory.getLocationId());
                        detail.setLocationCode(inventory.getLocationCode()); // 现在这个方法存在了
                    }
                }

                // 如果没有指定库位，自动推荐可用库位
                if (detail.getLocationId() == null) {
                    List<WarehouseLocation> availableLocations = locationMapper.selectAvailableLocations("RAW");
                    if (!availableLocations.isEmpty()) {
                        WarehouseLocation location = availableLocations.get(0);
                        detail.setLocationId(location.getId());
                        detail.setLocationCode(location.getLocationCode()); // 现在这个方法存在了
                    }
                }

                pickListDetailMapper.insert(detail);
            }
        }

        return pickListSaved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean executePickList(PickListExecuteDTO executeDTO) {
        PickList pickList = pickListMapper.selectById(executeDTO.getPickListId());
        if (pickList == null) {
            throw new RuntimeException("领料单不存在");
        }

        if (!"pending".equals(pickList.getStatus()) && !"picking".equals(pickList.getStatus())) {
            throw new RuntimeException("领料单状态不正确，无法执行领料");
        }

        // 更新领料单状态和领料员信息
        pickList.setStatus("picking");
        pickList.setPickerId(executeDTO.getPickerId());
        pickList.setPickerName(executeDTO.getPickerName());
        pickList.setUpdateUser(SecurityUtils.getUsername());
        pickList.setUpdateTime(LocalDateTime.now());
        pickListMapper.updateById(pickList);

        // 处理领料明细
        if (executeDTO.getExecuteItems() != null) {
            for (PickListExecuteItemDTO executeItem : executeDTO.getExecuteItems()) {
                PickListDetail detail = pickListDetailMapper.selectById(executeItem.getPickListDetailId());
                if (detail == null) {
                    throw new RuntimeException("领料明细不存在");
                }

                // 直接使用 BigDecimal，不需要转换
                BigDecimal pickedQuantity = executeItem.getPickedQuantity();
                detail.setPickedQuantity(pickedQuantity);

                // 如果实拣数量大于0，更新状态为已拣选
                if (pickedQuantity.compareTo(BigDecimal.ZERO) > 0) {
                    detail.setStatus("picked");
                }

                // 更新批次号和库位（如果有变更）
                if (StringUtils.hasText(executeItem.getBatchNumber())) {
                    detail.setBatchNumber(executeItem.getBatchNumber());
                }
                if (StringUtils.hasText(executeItem.getLocationCode())) {
                    LambdaQueryWrapper<WarehouseLocation> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(WarehouseLocation::getLocationCode, executeItem.getLocationCode());
                    WarehouseLocation location = locationMapper.selectOne(wrapper);
                    if (location != null) {
                        detail.setLocationId(location.getId());
                        detail.setLocationCode(location.getLocationCode()); // 现在这个方法存在了
                    }
                }

                pickListDetailMapper.updateById(detail);

                // 锁定库存
                if (pickedQuantity.compareTo(BigDecimal.ZERO) > 0) {
                    // 实现库存锁定逻辑
                    try {
                        inventoryService.lockStock(
                                detail.getMaterialId(),
                                detail.getLocationId(),
                                detail.getBatchNumber(),
                                pickedQuantity
                        );
                    } catch (Exception e) {
                        log.error("锁定库存失败: {}", e.getMessage());
                        throw new RuntimeException("锁定库存失败: " + e.getMessage());
                    }
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelPickList(Long pickListId, String reason) {
        PickList pickList = pickListMapper.selectById(pickListId);
        if (pickList == null) {
            throw new RuntimeException("领料单不存在");
        }

        if ("completed".equals(pickList.getStatus())) {
            throw new RuntimeException("已完成领料单无法取消");
        }

        // 更新领料单状态
        pickList.setStatus("cancelled");
        pickList.setUpdateUser(SecurityUtils.getUsername());
        pickList.setUpdateTime(LocalDateTime.now());

        // 释放已锁定的库存
        List<PickListDetail> details = pickListDetailMapper.selectByPickListId(pickListId);
        for (PickListDetail detail : details) {
            if (detail.getPickedQuantity().compareTo(BigDecimal.ZERO) > 0) {
                // TODO: 释放锁定的库存
                // inventoryService.unlockInventory(...);
            }
        }

        return pickListMapper.updateById(pickList) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completePickList(Long pickListId) {
        PickList pickList = pickListMapper.selectById(pickListId);
        if (pickList == null) {
            throw new RuntimeException("领料单不存在");
        }

        if (!"picking".equals(pickList.getStatus())) {
            throw new RuntimeException("只有领料中的领料单才能完成");
        }

        // 检查是否所有明细都已拣选
        List<PickListDetail> details = pickListDetailMapper.selectByPickListId(pickListId);
        boolean allPicked = details.stream().allMatch(detail -> "picked".equals(detail.getStatus()));

        if (!allPicked) {
            throw new RuntimeException("还有未拣选的物料，无法完成领料单");
        }

        // 更新领料单状态
        pickList.setStatus("completed");
        pickList.setPickTime(LocalDateTime.now());
        pickList.setUpdateUser(SecurityUtils.getUsername());
        pickList.setUpdateTime(LocalDateTime.now());

        // 执行库存出库
        for (PickListDetail detail : details) {
            if (detail.getPickedQuantity().compareTo(BigDecimal.ZERO) > 0) {
                // TODO: 执行库存出库
                // inventoryService.recordOutTransaction(...);
            }
        }

        return pickListMapper.updateById(pickList) > 0;
    }

    @Override
    public PickListVO getPickListDetail(Long pickListId) {
        PickList pickList = pickListMapper.selectById(pickListId);
        if (pickList == null) {
            throw new RuntimeException("领料单不存在");
        }

        PickListVO vo = convertToVO(pickList);
        List<PickListDetail> details = pickListDetailMapper.selectByPickListId(pickListId);
        vo.setDetails(convertToDetailVOList(details));
        calculatePickListStats(vo);

        return vo;
    }

    @Override
    public boolean printPickList(Long pickListId) {
        // TODO: 实现领料单打印逻辑
        log.info("打印领料单，领料单ID: {}", pickListId);
        return true;
    }

    /**
     * 生成领料单号
     */
    private String generatePickListCode() {
        String prefix = "PL";
        String dateStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        // TODO: 根据序列号生成唯一编码
        return prefix + dateStr + String.format("%04d", (int)(Math.random() * 10000));
    }

    /**
     * 计算领料单统计信息
     */
    private void calculatePickListStats(PickListVO vo) {
        if (vo.getDetails() == null) return;

        int totalItems = vo.getDetails().size();
        int pickedItems = (int) vo.getDetails().stream()
                .filter(detail -> "picked".equals(detail.getStatus()))
                .count();

        BigDecimal totalRequired = vo.getDetails().stream()
                .map(com.mes.vo.PickListDetailVO::getRequiredQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPicked = vo.getDetails().stream()
                .map(com.mes.vo.PickListDetailVO::getPickedQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        vo.setTotalItems(totalItems);
        vo.setPickedItems(pickedItems);
        vo.setTotalRequired(totalRequired);
        vo.setTotalPicked(totalPicked);
    }

    /**
     * Entity 转 VO
     */
    private PickListVO convertToVO(PickList pickList) {
        PickListVO vo = new PickListVO();
        BeanUtils.copyProperties(pickList, vo);
        vo.setStatusDesc(getStatusDesc(pickList.getStatus()));
        return vo;
    }

    /**
     * Detail Entity 转 VO
     */
    private List<com.mes.vo.PickListDetailVO> convertToDetailVOList(List<PickListDetail> details) {
        return details.stream().map(detail -> {
            com.mes.vo.PickListDetailVO vo = new com.mes.vo.PickListDetailVO();
            BeanUtils.copyProperties(detail, vo);
            vo.setStatusDesc(getDetailStatusDesc(detail.getStatus()));

            // 补充物料信息
            Material material = materialMapper.selectById(detail.getMaterialId());
            if (material != null) {
                vo.setMaterialName(material.getMaterialName());
                vo.setMaterialType(material.getMaterialType());
                vo.setSpecification(material.getSpecification());
                vo.setUnit(material.getUnit());
            }

            // 补充库位信息
            if (detail.getLocationId() != null) {
                WarehouseLocation location = locationMapper.selectById(detail.getLocationId());
                if (location != null) {
                    vo.setLocationName(location.getLocationName());
                }
            }

            // 查询可用库存
            BigDecimal availableQuantity = inventoryMapper.selectAvailableQuantityByMaterial(detail.getMaterialId());
            vo.setAvailableQuantity(availableQuantity != null ? availableQuantity : BigDecimal.ZERO);
            vo.setStockEnough(availableQuantity != null && availableQuantity.compareTo(detail.getRequiredQuantity()) >= 0);

            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        if (status == null) return "";
        switch (status) {
            case "pending": return "待领料";
            case "picking": return "领料中";
            case "completed": return "已完成";
            case "cancelled": return "已取消";
            default: return status;
        }
    }

    /**
     * 获取明细状态描述
     */
    private String getDetailStatusDesc(String status) {
        if (status == null) return "";
        switch (status) {
            case "pending": return "待拣选";
            case "picked": return "已拣选";
            default: return status;
        }
    }
}
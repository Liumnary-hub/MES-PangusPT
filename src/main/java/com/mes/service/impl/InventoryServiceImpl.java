package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.dto.InventoryQueryDTO;
import com.mes.entity.Inventory;
import com.mes.entity.Material;
import com.mes.entity.WarehouseLocation;
import com.mes.mapper.InventoryMapper;
import com.mes.mapper.MaterialMapper;
import com.mes.mapper.WarehouseLocationMapper;
import com.mes.service.InventoryService;
import com.mes.util.SecurityUtils;
import com.mes.util.TableDataInfo;
import com.mes.vo.InventoryVO;
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
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final MaterialMapper materialMapper;
    private final WarehouseLocationMapper locationMapper;


    @Override
    public Inventory getByMaterialAndLocation(Long materialId, Long locationId, String batchNumber) {
        return inventoryMapper.selectByMaterialAndLocation(materialId, locationId, batchNumber);
    }

    @Override
    public TableDataInfo<InventoryVO> selectInventoryPage(InventoryQueryDTO queryDTO) {
        int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

        // 使用 Mapper 的关联查询方法，而不是基础的 selectPage
        Page<InventoryVO> page = new Page<>(pageNum, pageSize);
        InventoryVO queryVO = new InventoryVO();

        // 将 DTO 转换为 VO（需要添加对应的 setter 方法）
        BeanUtils.copyProperties(queryDTO, queryVO);

        IPage<InventoryVO> result = inventoryMapper.selectInventoryPage(page, queryVO);

        TableDataInfo<InventoryVO> tableData = new TableDataInfo<>();
        tableData.setRows(result.getRecords());
        tableData.setTotal(result.getTotal());
        tableData.setCode(200);
        tableData.setMsg("查询成功");

        return tableData;
    }

    @Override
    public List<InventoryVO> selectInventoryList(InventoryQueryDTO queryDTO) {
        InventoryVO queryVO = new InventoryVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        Page<InventoryVO> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<InventoryVO> result = inventoryMapper.selectInventoryPage(page, queryVO);
        return result.getRecords();
    }

    @Override
    public List<InventoryVO> selectByMaterialId(Long materialId) {
        return inventoryMapper.selectByMaterialId(materialId);
    }


    @Override
    public List<InventoryVO> selectByLocationId(Long locationId) {
        return inventoryMapper.selectByLocationId(locationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adjustInventory(Long inventoryId, BigDecimal quantity, String reason) {
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        inventory.setCurrentQuantity(quantity);
        inventory.setLastUpdateTime(LocalDateTime.now());

        return inventoryMapper.updateById(inventory) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferInventory(Long fromInventoryId, Long toLocationId, BigDecimal quantity, String reason) {
        Inventory fromInventory = inventoryMapper.selectById(fromInventoryId);
        if (fromInventory == null) {
            throw new RuntimeException("源库存记录不存在");
        }

        if (fromInventory.getAvailableQuantity().compareTo(quantity) < 0) {
            throw new RuntimeException("可用库存不足");
        }

        // 减少源库存
        fromInventory.setCurrentQuantity(fromInventory.getCurrentQuantity().subtract(quantity));
        fromInventory.setLastUpdateTime(LocalDateTime.now());
        inventoryMapper.updateById(fromInventory);

        // 查找或创建目标库存记录
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getMaterialId, fromInventory.getMaterialId());
        wrapper.eq(Inventory::getLocationId, toLocationId);
        wrapper.eq(Inventory::getBatchNumber, fromInventory.getBatchNumber());

        Inventory toInventory = inventoryMapper.selectOne(wrapper);
        if (toInventory == null) {
            toInventory = new Inventory();
            toInventory.setMaterialId(fromInventory.getMaterialId());
            toInventory.setMaterialCode(fromInventory.getMaterialCode());
            toInventory.setLocationId(toLocationId);

            WarehouseLocation location = locationMapper.selectById(toLocationId);
            toInventory.setLocationCode(location.getLocationCode());

            toInventory.setBatchNumber(fromInventory.getBatchNumber());
            toInventory.setCurrentQuantity(quantity);
            toInventory.setLockedQuantity(BigDecimal.ZERO);
            toInventory.setQualityStatus(fromInventory.getQualityStatus());
            toInventory.setLastUpdateTime(LocalDateTime.now());

            inventoryMapper.insert(toInventory);
        } else {
            toInventory.setCurrentQuantity(toInventory.getCurrentQuantity().add(quantity));
            toInventory.setLastUpdateTime(LocalDateTime.now());
            inventoryMapper.updateById(toInventory);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockInventory(Long inventoryId, BigDecimal quantity) {
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        if (inventory.getAvailableQuantity().compareTo(quantity) < 0) {
            throw new RuntimeException("可用库存不足，无法锁定");
        }

        inventory.setLockedQuantity(inventory.getLockedQuantity().add(quantity));
        inventory.setLastUpdateTime(LocalDateTime.now());

        return inventoryMapper.updateById(inventory) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlockInventory(Long inventoryId, BigDecimal quantity) {
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        if (inventory.getLockedQuantity().compareTo(quantity) < 0) {
            throw new RuntimeException("锁定库存不足，无法释放");
        }

        inventory.setLockedQuantity(inventory.getLockedQuantity().subtract(quantity));
        inventory.setLastUpdateTime(LocalDateTime.now());

        return inventoryMapper.updateById(inventory) > 0;
    }

    @Override
    public InventoryVO getMaterialTotalStock(Long materialId) {
        List<InventoryVO> inventories = selectByMaterialId(materialId);

        InventoryVO totalStock = new InventoryVO();
        totalStock.setMaterialId(materialId);

        // 计算总库存
        double totalQuantity = inventories.stream()
                .mapToDouble(vo -> vo.getCurrentQuantity().doubleValue())
                .sum();
        double lockedQuantity = inventories.stream()
                .mapToDouble(vo -> vo.getLockedQuantity().doubleValue())
                .sum();
        double availableQuantity = inventories.stream()
                .mapToDouble(vo -> vo.getAvailableQuantity().doubleValue())
                .sum();

        totalStock.setCurrentQuantity(BigDecimal.valueOf(totalQuantity));
        totalStock.setLockedQuantity(BigDecimal.valueOf(lockedQuantity));
        totalStock.setAvailableQuantity(BigDecimal.valueOf(availableQuantity));

        return totalStock;
    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean adjustInventory(Long inventoryId, Double quantity, String reason) {
//        Inventory inventory = inventoryMapper.selectById(inventoryId);
//        if (inventory == null) {
//            throw new RuntimeException("库存记录不存在");
//        }
//
//        // 更新库存数量
//        inventory.setCurrentQuantity(BigDecimal.valueOf(quantity));
//        inventory.setLastUpdateTime(LocalDateTime.now());
//
//        boolean success = inventoryMapper.updateById(inventory) > 0;
//
//        if (success) {
//            // 记录库存调整交易
//            // TODO: 调用 InventoryTransactionService 记录调整交易
//        }
//
//        return success;
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean transferInventory(Long fromInventoryId, Long toLocationId, Double quantity, String reason) {
//        Inventory fromInventory = inventoryMapper.selectById(fromInventoryId);
//        if (fromInventory == null) {
//            throw new RuntimeException("源库存记录不存在");
//        }
//
//        // 修复：使用 compareTo 方法比较 BigDecimal
//        BigDecimal transferQuantity = BigDecimal.valueOf(quantity);
//        if (fromInventory.getAvailableQuantity().compareTo(transferQuantity) < 0) {
//            throw new RuntimeException("可用库存不足");
//        }
//
//
//        // 减少源库存 - 使用 BigDecimal 的 subtract 方法
//        fromInventory.setCurrentQuantity(fromInventory.getCurrentQuantity().subtract(transferQuantity));
//        fromInventory.setLastUpdateTime(LocalDateTime.now());
//        inventoryMapper.updateById(fromInventory);
//
//
//        // 查找或创建目标库存记录
//        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Inventory::getMaterialId, fromInventory.getMaterialId());
//        wrapper.eq(Inventory::getLocationId, toLocationId);
//        wrapper.eq(Inventory::getBatchNumber, fromInventory.getBatchNumber());
//
//        Inventory toInventory = inventoryMapper.selectOne(wrapper);
//        if (toInventory == null) {
//            toInventory = new Inventory();
//            toInventory.setMaterialId(fromInventory.getMaterialId());
//            toInventory.setMaterialCode(fromInventory.getMaterialCode());
//            toInventory.setLocationId(toLocationId);
//
//            WarehouseLocation location = locationMapper.selectById(toLocationId);
//            toInventory.setLocationCode(location.getLocationCode());
//
//            toInventory.setBatchNumber(fromInventory.getBatchNumber());
//            toInventory.setCurrentQuantity(BigDecimal.valueOf(quantity));
//            toInventory.setQualityStatus(fromInventory.getQualityStatus());
//            toInventory.setLastUpdateTime(LocalDateTime.now());
//
//            inventoryMapper.insert(toInventory);
//        } else {
//            // 使用 BigDecimal 的 add 方法
//            toInventory.setCurrentQuantity(toInventory.getCurrentQuantity().add(transferQuantity));
//            toInventory.setLastUpdateTime(LocalDateTime.now());
//            inventoryMapper.updateById(toInventory);
//        }
//
//        // 记录转移交易
//        // TODO: 调用 InventoryTransactionService 记录转移交易
//
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean lockInventory(Long inventoryId, Double quantity) {
//        Inventory inventory = inventoryMapper.selectById(inventoryId);
//        if (inventory == null) {
//            throw new RuntimeException("库存记录不存在");
//        }
//        // 修复：使用 compareTo 方法比较 BigDecimal
//        BigDecimal lockQuantity = BigDecimal.valueOf(quantity);
//        if (inventory.getAvailableQuantity().compareTo(lockQuantity) < 0) {
//            throw new RuntimeException("可用库存不足，无法锁定");
//        }
//
//        // 使用 BigDecimal 的 add 方法
//        inventory.setLockedQuantity(inventory.getLockedQuantity().add(lockQuantity));
//        inventory.setLastUpdateTime(LocalDateTime.now());
//
//
//        return inventoryMapper.updateById(inventory) > 0;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean unlockInventory(Long inventoryId, Double quantity) {
//        Inventory inventory = inventoryMapper.selectById(inventoryId);
//        if (inventory == null) {
//            throw new RuntimeException("库存记录不存在");
//        }
//
//        BigDecimal unlockQuantity = BigDecimal.valueOf(quantity);
//        if (inventory.getLockedQuantity().compareTo(unlockQuantity) < 0) {
//            throw new RuntimeException("锁定库存不足，无法释放");
//        }
//
//
//        // 使用 BigDecimal 的 subtract 方法
//        inventory.setLockedQuantity(inventory.getLockedQuantity().subtract(unlockQuantity));
//        inventory.setLastUpdateTime(LocalDateTime.now());
//
//        return inventoryMapper.updateById(inventory) > 0;
//    }

    @Override
    public BigDecimal getAvailableQuantity(Long materialId) {
        BigDecimal available = inventoryMapper.selectAvailableQuantityByMaterial(materialId);
        return available != null ? available : BigDecimal.ZERO;
    }

    @Override
    public boolean checkStockEnough(Long materialId, BigDecimal requiredQuantity) {
        BigDecimal availableQuantity = getAvailableQuantity(materialId);
        return availableQuantity.compareTo(requiredQuantity) >= 0;
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<Inventory> buildQueryWrapper(InventoryQueryDTO queryDTO) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(queryDTO.getMaterialCode())) {
            wrapper.like(Inventory::getMaterialCode, queryDTO.getMaterialCode());
        }
        if (StringUtils.hasText(queryDTO.getMaterialName())) {
            // 需要通过关联查询实现
        }
        if (StringUtils.hasText(queryDTO.getLocationCode())) {
            wrapper.like(Inventory::getLocationCode, queryDTO.getLocationCode());
        }
        if (StringUtils.hasText(queryDTO.getBatchNumber())) {
            wrapper.like(Inventory::getBatchNumber, queryDTO.getBatchNumber());
        }
        if (StringUtils.hasText(queryDTO.getQualityStatus())) {
            wrapper.eq(Inventory::getQualityStatus, queryDTO.getQualityStatus());
        }

        wrapper.orderByDesc(Inventory::getLastUpdateTime);

        return wrapper;
    }

    /**
     * Entity 转 VO
     */
    private InventoryVO convertToVO(Inventory inventory) {
        InventoryVO vo = new InventoryVO();
        BeanUtils.copyProperties(inventory, vo);

        // 补充物料信息
        Material material = materialMapper.selectById(inventory.getMaterialId());
        if (material != null) {
            vo.setMaterialName(material.getMaterialName());
            vo.setMaterialType(material.getMaterialType());
            vo.setSpecification(material.getSpecification());
            vo.setUnit(material.getUnit());
        }

        // 补充库位信息
        WarehouseLocation location = locationMapper.selectById(inventory.getLocationId());
        if (location != null) {
            vo.setWarehouseCode(location.getWarehouseCode());
            vo.setWarehouseName(location.getWarehouseName());
        }

        // 设置质量状态描述
        vo.setQualityStatusDesc(getQualityStatusDesc(inventory.getQualityStatus()));

        return vo;
    }

    /**
     * 获取质量状态描述
     */
    private String getQualityStatusDesc(String qualityStatus) {
        if (qualityStatus == null) return "";
        switch (qualityStatus) {
            case "qualified": return "合格";
            case "unqualified": return "不合格";
            case "quarantined": return "隔离";
            default: return qualityStatus;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockStock(Long materialId, Long locationId, String batchNumber, BigDecimal quantity) {
        Inventory inventory = getByMaterialAndLocation(materialId, locationId, batchNumber);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        // 检查可用库存是否充足
        BigDecimal availableQuantity = inventory.getCurrentQuantity().subtract(inventory.getLockedQuantity());
        if (availableQuantity.compareTo(quantity) < 0) {
            throw new RuntimeException("可用库存不足，无法锁定");
        }

        // 锁定库存
        inventory.setLockedQuantity(inventory.getLockedQuantity().add(quantity));
        inventory.setLastUpdateTime(LocalDateTime.now());
        return inventoryMapper.updateById(inventory) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlockStock(Long materialId, Long locationId, String batchNumber, BigDecimal quantity) {
        Inventory inventory = getByMaterialAndLocation(materialId, locationId, batchNumber);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }

        // 检查锁定库存是否足够
        if (inventory.getLockedQuantity().compareTo(quantity) < 0) {
            throw new RuntimeException("锁定库存不足，无法释放");
        }

        // 释放锁定库存
        inventory.setLockedQuantity(inventory.getLockedQuantity().subtract(quantity));
        inventory.setLastUpdateTime(LocalDateTime.now());
        return inventoryMapper.updateById(inventory) > 0;
    }
}
package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.dto.InventoryQueryDTO;
import com.mes.entity.Inventory;
import com.mes.util.TableDataInfo;
import com.mes.vo.InventoryVO;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryService extends IService<Inventory> {

    TableDataInfo<InventoryVO> selectInventoryPage(InventoryQueryDTO queryDTO);
    List<InventoryVO> selectInventoryList(InventoryQueryDTO queryDTO);
    List<InventoryVO> selectByMaterialId(Long materialId);
    List<InventoryVO> selectByLocationId(Long locationId);
    InventoryVO getMaterialTotalStock(Long materialId);

    // 统一使用 BigDecimal
    boolean adjustInventory(Long inventoryId, BigDecimal quantity, String reason);
    boolean transferInventory(Long fromInventoryId, Long toLocationId, BigDecimal quantity, String reason);
    boolean lockInventory(Long inventoryId, BigDecimal quantity);
    boolean unlockInventory(Long inventoryId, BigDecimal quantity);

    // 统一使用 BigDecimal
    BigDecimal getAvailableQuantity(Long materialId);
    boolean checkStockEnough(Long materialId, BigDecimal requiredQuantity);

    boolean lockStock(Long materialId, Long locationId, String batchNumber, BigDecimal quantity);
    boolean unlockStock(Long materialId, Long locationId, String batchNumber, BigDecimal quantity);
    boolean deductStock(Long materialId, Long locationId, String batchNumber, BigDecimal quantity);

    // 添加缺失的方法
    Inventory getByMaterialAndLocation(Long materialId, Long locationId, String batchNumber);
}
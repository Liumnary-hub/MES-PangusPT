package com.mes.controller;

import com.mes.dto.InventoryAdjustDTO;
import com.mes.dto.InventoryQueryDTO;
import com.mes.dto.InventoryTransferDTO;
import com.mes.service.InventoryService;
import com.mes.util.TableDataInfo;
import com.mes.vo.InventoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存管理控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * 分页查询库存列表
     */
    @GetMapping("/list")
    public TableDataInfo<InventoryVO> list(InventoryQueryDTO queryDTO) {
        return inventoryService.selectInventoryPage(queryDTO);
    }

    /**
     * 查询库存列表
     */
    @GetMapping("/all")
    public List<InventoryVO> all(InventoryQueryDTO queryDTO) {
        return inventoryService.selectInventoryList(queryDTO);
    }

    /**
     * 根据物料ID查询库存
     */
    @GetMapping("/material/{materialId}")
    public List<InventoryVO> getByMaterialId(@PathVariable Long materialId) {
        return inventoryService.selectByMaterialId(materialId);
    }

    /**
     * 根据库位查询库存
     */
    @GetMapping("/location/{locationId}")
    public List<InventoryVO> getByLocationId(@PathVariable Long locationId) {
        return inventoryService.selectByLocationId(locationId);
    }

    /**
     * 查询物料总库存
     */
    @GetMapping("/material/{materialId}/total")
    public InventoryVO getMaterialTotalStock(@PathVariable Long materialId) {
        return inventoryService.getMaterialTotalStock(materialId);
    }

    /**
     * 库存调整
     */
    @PostMapping("/adjust")
    public boolean adjustInventory(@Validated @RequestBody InventoryAdjustDTO adjustDTO) {
        return inventoryService.adjustInventory(
                adjustDTO.getInventoryId(),
                adjustDTO.getQuantity(),
                adjustDTO.getReason()
        );
    }

    /**
     * 库存转移
     */
    @PostMapping("/transfer")
    public boolean transferInventory(@Validated @RequestBody InventoryTransferDTO transferDTO) {
        return inventoryService.transferInventory(
                transferDTO.getFromInventoryId(),
                transferDTO.getToLocationId(),
                transferDTO.getQuantity(),
                transferDTO.getReason()
        );
    }

    /**
     * 锁定库存
     */
    @PostMapping("/lock")
    public boolean lockInventory(@RequestParam Long inventoryId,
                                 @RequestParam BigDecimal quantity) {
        return inventoryService.lockInventory(inventoryId, quantity);
    }

    /**
     * 释放锁定库存
     */
    @PostMapping("/unlock")
    public boolean unlockInventory(@RequestParam Long inventoryId,
                                   @RequestParam BigDecimal quantity) {
        return inventoryService.unlockInventory(inventoryId, quantity);
    }

    /**
     * 获取可用库存数量
     */
    @GetMapping("/material/{materialId}/available")
    public BigDecimal getAvailableQuantity(@PathVariable Long materialId) {
        return inventoryService.getAvailableQuantity(materialId);
    }

    /**
     * 检查库存是否充足
     */
    @GetMapping("/checkStockEnough")
    public boolean checkStockEnough(@RequestParam Long materialId,
                                    @RequestParam BigDecimal requiredQuantity) {
        return inventoryService.checkStockEnough(materialId, requiredQuantity);
    }

    /**
     * 库存盘点
     */
    @PostMapping("/count")
    public boolean inventoryCount(@RequestBody List<InventoryAdjustDTO> countList) {
        // 实现库存盘点逻辑
        for (InventoryAdjustDTO countItem : countList) {
            inventoryService.adjustInventory(
                    countItem.getInventoryId(),
                    countItem.getQuantity(),
                    "库存盘点"
            );
        }
        return true;
    }
}
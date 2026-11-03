package com.mes.controller;

import com.mes.dto.InventoryTransactionQueryDTO;
import com.mes.service.InventoryTransactionService;
import com.mes.util.TableDataInfo;
import com.mes.vo.InventoryTransactionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存交易记录控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory-transaction")
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    /**
     * 分页查询交易记录
     */
    @GetMapping("/list")
    public TableDataInfo<InventoryTransactionVO> list(InventoryTransactionQueryDTO queryDTO) {
        return transactionService.selectTransactionPage(queryDTO);
    }

    /**
     * 查询交易记录列表
     */
    @GetMapping("/all")
    public List<InventoryTransactionVO> all(InventoryTransactionQueryDTO queryDTO) {
        return transactionService.selectTransactionList(queryDTO);
    }

    /**
     * 根据关联信息查询交易记录
     */
    @GetMapping("/reference")
    public List<InventoryTransactionVO> getByReference(@RequestParam String referenceType,
                                                       @RequestParam String referenceCode) {
        return transactionService.selectByReference(referenceType, referenceCode);
    }

    /**
     * 查询物料交易历史
     */
    @GetMapping("/material/{materialId}/history")
    public List<InventoryTransactionVO> getMaterialHistory(@PathVariable Long materialId) {
        return transactionService.getMaterialTransactionHistory(materialId);
    }

    /**
     * 库存交易统计
     */
    @GetMapping("/statistics")
    public Object getStatistics(@RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate) {
        // 实现交易统计逻辑
        // 返回入库、出库、转移等各类交易的数量和金额统计
        return null; // 实际开发中需要实现
    }
}
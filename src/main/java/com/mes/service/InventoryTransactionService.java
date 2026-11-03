package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.dto.InventoryTransactionQueryDTO;
import com.mes.entity.InventoryTransaction;
import com.mes.util.TableDataInfo;
import com.mes.vo.InventoryTransactionVO;

import java.util.List;

/**
 * 库存交易服务接口
 */
public interface InventoryTransactionService extends IService<InventoryTransaction> {

    /**
     * 分页查询交易记录
     */
    TableDataInfo<InventoryTransactionVO> selectTransactionPage(InventoryTransactionQueryDTO queryDTO);

    /**
     * 查询交易记录列表
     */
    List<InventoryTransactionVO> selectTransactionList(InventoryTransactionQueryDTO queryDTO);

    /**
     * 根据关联信息查询交易记录
     */
    List<InventoryTransactionVO> selectByReference(String referenceType, String referenceCode);

    /**
     * 记录入库交易
     */
    boolean recordInTransaction(Long materialId, Long locationId, Double quantity,
                                String batchNumber, String referenceType, String referenceCode);

    /**
     * 记录出库交易
     */
    boolean recordOutTransaction(Long materialId, Long locationId, Double quantity,
                                 String batchNumber, String referenceType, String referenceCode);

    /**
     * 记录转移交易
     */
    boolean recordTransferTransaction(Long materialId, Long fromLocationId, Long toLocationId,
                                      Double quantity, String batchNumber, String referenceType, String referenceCode);

    /**
     * 获取物料交易历史
     */
    List<InventoryTransactionVO> getMaterialTransactionHistory(Long materialId);
}
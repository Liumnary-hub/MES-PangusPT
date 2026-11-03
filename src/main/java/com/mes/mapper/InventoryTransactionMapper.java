package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.mes.entity.InventoryTransaction;
import com.mes.vo.InventoryTransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存交易记录 Mapper 接口
 */
@Mapper
public interface InventoryTransactionMapper extends BaseMapper<InventoryTransaction> {

    /**
     * 分页查询交易记录
     */
    IPage<InventoryTransactionVO> selectTransactionPage(Page<InventoryTransactionVO> page, @Param("vo") InventoryTransactionVO vo);

    /**
     * 根据关联信息查询交易记录
     */
    List<InventoryTransaction> selectByReference(@Param("referenceType") String referenceType,
                                                 @Param("referenceId") Long referenceId);

    /**
     * 查询物料交易历史
     */
    List<InventoryTransactionVO> selectMaterialTransactionHistory(@Param("materialId") Long materialId);
}
package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.Inventory;
import com.mes.vo.InventoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {  // 修复：改为继承 BaseMapper<Inventory>

    /**
     * 分页查询库存列表
     */
    IPage<InventoryVO> selectInventoryPage(Page<InventoryVO> page, @Param("vo") InventoryVO vo);  // 修复：使用正确的 Page 类型

    /**
     * 查询物料库存汇总
     */
    List<InventoryVO> selectMaterialInventorySummary(@Param("materialId") Long materialId);

    /**
     * 获取物料的可用库存数量
     */
    @Select("SELECT SUM(current_quantity - locked_quantity) FROM mes_inventory WHERE material_id = #{materialId}")
    BigDecimal selectAvailableQuantityByMaterial(@Param("materialId") Long materialId);

    /**
     * 根据物料、库位和批次查询库存
     */
    @Select("SELECT * FROM mes_inventory WHERE material_id = #{materialId} AND location_id = #{locationId} AND batch_number = #{batchNumber}")
    Inventory selectByMaterialAndLocation(@Param("materialId") Long materialId,
                                          @Param("locationId") Long locationId,
                                          @Param("batchNumber") String batchNumber);

    /**
     * 更新库存数量
     */
    int updateStockQuantity(@Param("id") Long id,
                            @Param("currentQuantity") BigDecimal currentQuantity,
                            @Param("lockedQuantity") BigDecimal lockedQuantity);

    /**
     * 根据物料ID查询库存列表
     */
    List<InventoryVO> selectByMaterialId(@Param("materialId") Long materialId);

    /**
     * 根据库位ID查询库存列表
     */
    List<InventoryVO> selectByLocationId(@Param("locationId") Long locationId);

    /**
     * 原子锁定库存：可用量不足时更新 0 行，由数据库保证并发安全
     */
    @Update("UPDATE mes_inventory SET locked_quantity = locked_quantity + #{quantity}, last_update_time = NOW() " +
            "WHERE id = #{id} AND current_quantity - locked_quantity >= #{quantity}")
    int lockStockById(@Param("id") Long id, @Param("quantity") BigDecimal quantity);

    /**
     * 原子释放锁定库存
     */
    @Update("UPDATE mes_inventory SET locked_quantity = locked_quantity - #{quantity}, last_update_time = NOW() " +
            "WHERE id = #{id} AND locked_quantity >= #{quantity}")
    int unlockStockById(@Param("id") Long id, @Param("quantity") BigDecimal quantity);

    /**
     * 原子扣减已锁定库存（出库）：同时减少现存量与锁定量
     */
    @Update("UPDATE mes_inventory SET current_quantity = current_quantity - #{quantity}, " +
            "locked_quantity = locked_quantity - #{quantity}, last_update_time = NOW() " +
            "WHERE id = #{id} AND current_quantity >= #{quantity} AND locked_quantity >= #{quantity}")
    int deductLockedStockById(@Param("id") Long id, @Param("quantity") BigDecimal quantity);

    /**
     * 原子扣减可用库存（调拨出库）
     */
    @Update("UPDATE mes_inventory SET current_quantity = current_quantity - #{quantity}, last_update_time = NOW() " +
            "WHERE id = #{id} AND current_quantity - locked_quantity >= #{quantity}")
    int deductAvailableStockById(@Param("id") Long id, @Param("quantity") BigDecimal quantity);

    /**
     * 原子增加库存（调拨入库）
     */
    @Update("UPDATE mes_inventory SET current_quantity = current_quantity + #{quantity}, last_update_time = NOW() " +
            "WHERE id = #{id}")
    int increaseStockById(@Param("id") Long id, @Param("quantity") BigDecimal quantity);
}
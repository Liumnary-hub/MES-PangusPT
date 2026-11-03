package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductionOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProductionOrderMapper extends BaseMapper<ProductionOrder> {

    /**
     * 根据状态查询工单
     */
    @Select("SELECT * FROM mes_production_order WHERE status = #{status} AND is_deleted = 0 ORDER BY create_time DESC")
    List<ProductionOrder> selectByStatus(@Param("status") String status);

    /**
     * 根据产品ID查询工单
     */
    @Select("SELECT * FROM mes_production_order WHERE product_id = #{productId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<ProductionOrder> selectByProductId(@Param("productId") Long productId);

    /**
     * 分页查询工单
     */
    Page<ProductionOrder> selectOrderPage(Page<ProductionOrder> page,
                                          @Param("orderCode") String orderCode,
                                          @Param("productCode") String productCode,
                                          @Param("status") String status,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    /**
     * 更新工单状态
     */
    @Select("UPDATE mes_production_order SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateOrderStatus(@Param("id") Long id, @Param("status") String status);
//
//    /**
//     * 更新完成数量
//     */
//    int updateCompletedQuantity(@Param("id") Long id, @Param("completedQuantity") Integer completedQuantity);



    /**
     * 根据多个状态查询工单
     */
    @Select("<script>" +
            "SELECT * FROM mes_production_order WHERE is_deleted = 0 " +
            "<if test='statusList != null and statusList.size() > 0'>" +
            "AND status IN <foreach collection='statusList' item='status' open='(' separator=',' close=')'>#{status}</foreach>" +
            "</if>" +
            "ORDER BY create_time DESC" +
            "</script>")
    List<ProductionOrder> selectByStatusList(@Param("statusList") List<String> statusList);

    /**
     * 更新工单完成数量
     */
    @Update("UPDATE mes_production_order SET completed_quantity = #{completedQuantity}, update_time = NOW() WHERE id = #{id}")
    int updateCompletedQuantity(@Param("id") Long id, @Param("completedQuantity") Integer completedQuantity);
}
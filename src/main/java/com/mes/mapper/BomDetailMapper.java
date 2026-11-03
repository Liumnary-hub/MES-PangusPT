package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mes.entity.BomDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * BOM明细 Mapper 接口
 */
@Mapper
public interface BomDetailMapper extends BaseMapper<BomDetail> {

    @Select("SELECT * FROM mes_bom_detail WHERE bom_id = #{bomId} ORDER BY sequence")
    List<BomDetail> selectByBomId(@Param("bomId") Long bomId);

    @Select("SELECT * FROM mes_bom_detail WHERE bom_id IN (SELECT id FROM mes_bom WHERE product_id = #{productId} AND status = 'active')")
    List<BomDetail> selectByProductId(@Param("productId") Long productId);
}
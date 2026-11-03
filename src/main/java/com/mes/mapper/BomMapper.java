package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.Bom;
import com.mes.vo.BomVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface BomMapper extends BaseMapper<Bom> {

    /**
     * 分页查询BOM列表
     */
    IPage<BomVO> selectBomPage(Page<BomVO> page, @Param("vo") BomVO vo);

    /**
     * 查询BOM列表
     */
    List<BomVO> selectBomList(@Param("vo") BomVO vo);

    /**
     * 根据产品ID查询激活的BOM
     */
    Bom selectActiveByProductId(@Param("productId") Long productId);

    /**
     * 检查BOM编码是否唯一
     */
    int countByBomCodeExcludeId(@Param("bomCode") String bomCode, @Param("excludeId") Long excludeId);

    /**
     * 根据产品编码查询BOM
     */
    Bom selectByProductCode(@Param("productCode") String productCode);

    /**
     * 获取产品的所有BOM版本
     */
    List<Bom> selectBomVersionsByProduct(@Param("productId") Long productId);

    /**
     * 根据状态查询BOM列表
     */
    List<Bom> selectByStatus(@Param("status") String status);

    /**
     * 查询过期的BOM
     */
    List<Bom> selectExpiredBoms();

    /**
     * 查询即将生效的BOM
     */
    List<Bom> selectFutureEffectiveBoms();

    /**
     * 批量更新BOM状态
     */
    int updateBomStatusBatch(@Param("ids") List<Long> ids, @Param("status") String status, @Param("updateUser") String updateUser);

    /**
     * 逻辑删除BOM
     */
    int deleteBomById(@Param("id") Long id, @Param("updateUser") String updateUser);

    /**
     * 批量逻辑删除BOM
     */
    int deleteBomByIds(@Param("ids") List<Long> ids, @Param("updateUser") String updateUser);
}
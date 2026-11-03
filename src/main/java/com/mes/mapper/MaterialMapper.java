package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mes.entity.Material;
import com.mes.vo.MaterialVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;

import java.util.List;
@Mapper
public interface MaterialMapper extends BaseMapper<Material> {


    IPage<MaterialVO> selectMaterialPage(Page<MaterialVO> page, @Param("vo") MaterialVO vo);

    /**
     * 查询物料列表
     */
    List<MaterialVO> selectMaterialList(@Param("vo") MaterialVO vo);

    /**
     * 根据物料编码查询
     */
    @Select("SELECT * FROM mes_material WHERE material_code = #{materialCode} AND is_deleted = 0")
    Material selectByMaterialCode(@Param("materialCode") String materialCode);

    /**
     * 检查物料编码是否唯一
     */
    @Select("SELECT COUNT(*) FROM mes_material WHERE material_code = #{materialCode} AND is_deleted = 0 AND id != #{excludeId}")
    int countByMaterialCodeExcludeId(@Param("materialCode") String materialCode, @Param("excludeId") Long excludeId);

    /**
     * 根据类型查询物料
     */
    @Select("SELECT * FROM mes_material WHERE material_type = #{materialType} AND status = 'active' AND is_deleted = 0")
    List<Material> selectByMaterialType(@Param("materialType") String materialType);
}

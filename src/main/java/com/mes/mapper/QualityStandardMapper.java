package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.dto.StandardQueryDTO;
import com.mes.entity.QualityStandard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// QualityStandardMapper.java
@Mapper
public interface QualityStandardMapper extends BaseMapper<QualityStandard> {

    /**
     * 分页查询检验标准列表
     */
    IPage<QualityStandard> selectPageList(Page<QualityStandard> page, @Param("query") StandardQueryDTO query);

    /**
     * 根据标准编号查询
     */
    QualityStandard selectByCode(@Param("standardCode") String standardCode);

    /**
     * 根据标准编号查询（别名方法）
     */
    QualityStandard selectByStandardCode(@Param("standardCode") String standardCode);

    /**
     * 检查标准编号是否存在
     */
    boolean existsByStandardCode(@Param("standardCode") String standardCode);

    /**
     * 根据标准编号和产品ID检查标准是否存在
     */
    boolean existsByCodeAndProduct(@Param("standardCode") String standardCode, @Param("productId") Long productId);

    /**
     * 根据标准编号统计数量
     */
    Long countByStandardCode(@Param("standardCode") String standardCode);

    /**
     * 根据查询条件统计数量
     */
    Long countByQuery(@Param("query") StandardQueryDTO query);

    /**
     * 根据产品ID和检验类型查询检验标准
     */
    List<QualityStandard> selectByProductAndType(@Param("productId") Long productId,
                                                 @Param("inspectionType") String inspectionType,
                                                 @Param("status") String status);

    /**
     * 根据产品ID查询检验标准
     */
    List<QualityStandard> selectByProductId(@Param("productId") Long productId);

    /**
     * 更新标准状态
     */
    int updateStatus(@Param("id") Long id,
                     @Param("status") String status,
                     @Param("updateBy") Long updateBy,
                     @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量逻辑删除检验标准
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids);

    /**
     * 查询生效中的标准
     */
    List<QualityStandard> selectActiveStandards();

    /**
     * 查询某个产品的当前生效标准
     */
    QualityStandard selectActiveStandardByProduct(@Param("productId") Long productId,
                                                  @Param("inspectionType") String inspectionType);

    /**
     * 根据状态统计
     */
    Map<String, Long> countByStatus(@Param("query") StandardQueryDTO query);

    /**
     * 根据检验类型统计
     */
    Map<String, Long> countByInspectionType(@Param("query") StandardQueryDTO query);

    /**
     * 根据产品统计
     */
    List<Map<String, Object>> countByProduct(@Param("query") StandardQueryDTO query);

    /**
     * 导出数据
     */
    List<Map<String, Object>> exportByQuery(@Param("query") StandardQueryDTO query);
}
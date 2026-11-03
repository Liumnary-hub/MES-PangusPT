package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.InspectionItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// InspectionItemMapper.java
@Mapper
public interface InspectionItemMapper extends BaseMapper<InspectionItem> {

    /**
     * 根据标准ID查询检验项目
     */
    List<InspectionItem> selectByStandardId(@Param("standardId") Long standardId);

    /**
     * 根据标准ID统计检验项目数量
     */
    Long countByStandardId(@Param("standardId") Long standardId);

    /**
     * 根据标准ID删除检验项目
     */
    int deleteByStandardId(@Param("standardId") Long standardId);

    /**
     * 批量插入检验项目
     */
    int insertBatch(@Param("items") List<InspectionItem> items);
}


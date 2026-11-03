package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.dto.DefectCodeQueryDTO;
import com.mes.entity.DefectCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// DefectCodeMapper.java
@Mapper
public interface DefectCodeMapper extends BaseMapper<DefectCode> {

    /**
     * 分页查询缺陷代码
     */
    List<DefectCode> selectDefectCodeList(DefectCodeQueryDTO query);

    /**
     * 根据缺陷代码查询
     */
    DefectCode selectByDefectCode(@Param("defectCode") String defectCode);

    /**
     * 根据缺陷类型查询
     */
    List<DefectCode> selectByDefectType(@Param("defectType") String defectType);

    /**
     * 批量更新启用状态
     */
    int updateBatchStatus(@Param("ids") List<Long> ids,
                          @Param("enabled") Boolean enabled);


}

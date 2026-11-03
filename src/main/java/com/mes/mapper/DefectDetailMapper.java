package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.dto.ParetoQueryDTO;
import com.mes.entity.DefectDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

// DefectDetailMapper.java
@Mapper
public interface DefectDetailMapper extends BaseMapper<DefectDetail> {

    /**
     * 根据任务ID查询缺陷明细
     */
    List<DefectDetail> selectByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据记录ID查询缺陷明细
     */
    List<DefectDetail> selectByRecordId(@Param("recordId") Long recordId);

    /**
     * 获取缺陷TOP N
     */
    List<Map<String, Object>> getTopDefects(@Param("limit") Integer limit);

    /**
     * 获取缺陷类型分布
     */
    List<Map<String, Object>> getDefectTypeDistribution(@Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);

    /**
     * 获取帕累托分析数据
     */
    List<Map<String, Object>> getDefectForPareto(@Param("query") ParetoQueryDTO query);

    /**
     * 根据任务ID删除缺陷明细
     */
    int deleteByTaskId(@Param("taskId") Long taskId);

    /**
     * 批量插入缺陷明细
     */
    int insertBatch(@Param("defects") List<DefectDetail> defects);

    List<Map<String, Object>> getTodayDefectStatistics(String today);
}

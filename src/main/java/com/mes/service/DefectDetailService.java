package com.mes.service;

import com.mes.dto.DefectDetailDTO;
import com.mes.vo.DefectDetailVO;
import com.mes.vo.DefectStatisticVO;
import com.mes.vo.ParetoAnalysisVO;

import java.util.List;
import java.util.Map;

/**
 * 缺陷明细服务接口
 */
public interface DefectDetailService {

    /**
     * 创建缺陷明细
     */
    Long createDefectDetail(DefectDetailDTO defectDetailDTO);

    /**
     * 批量创建缺陷明细
     */
    void batchCreateDefectDetails(List<DefectDetailDTO> defectDetailDTOs);

    /**
     * 更新缺陷明细
     */
    void updateDefectDetail(Long id, DefectDetailDTO defectDetailDTO);

    /**
     * 删除缺陷明细
     */
    void deleteDefectDetail(Long id);

    /**
     * 根据记录ID查询缺陷明细
     */
    List<DefectDetailVO> getDefectDetailsByRecordId(Long recordId);

    /**
     * 根据任务ID查询缺陷明细
     */
    List<DefectDetailVO> getDefectDetailsByTaskId(Long taskId);

    /**
     * 根据记录ID统计缺陷数量
     */
    Map<String, Integer> countDefectsByRecordId(Long recordId);

    /**
     * 获取缺陷TOP N
     */
    List<Map<String, Object>> getTopDefects(Integer limit);

    /**
     * 获取缺陷类型分布
     */
    List<Map<String, Object>> getDefectTypeDistribution(String startDate, String endDate);

    /**
     * 获取帕累托分析数据
     */
    ParetoAnalysisVO getDefectParetoAnalysis();

    /**
     * 根据任务ID删除缺陷明细
     */
    void deleteByTaskId(Long taskId);

    /**
     * 获取缺陷统计信息
     */
    DefectStatisticVO getDefectStatistics(Long recordId);
}

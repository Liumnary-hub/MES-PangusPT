package com.mes.service;

import com.mes.common.PageResult;
import com.mes.dto.DefectCodeDTO;
import com.mes.dto.DefectCodeQueryDTO;
import com.mes.vo.DefectCodeVO;
import com.mes.vo.DefectStatisticVO;

import java.util.List;
import java.util.Map;

/**
 * 缺陷代码服务接口
 */
public interface DefectCodeService {

    /**
     * 创建缺陷代码
     */
    Long createDefectCode(DefectCodeDTO defectCodeDTO);

    /**
     * 更新缺陷代码
     */
    void updateDefectCode(Long id, DefectCodeDTO defectCodeDTO);

    /**
     * 根据ID获取缺陷代码
     */
    DefectCodeVO getDefectCodeById(Long id);

    /**
     * 根据代码获取缺陷代码
     */
    DefectCodeVO getDefectCodeByCode(String defectCode);

    /**
     * 删除缺陷代码
     */
    void deleteDefectCode(Long id);

    /**
     * 批量删除缺陷代码
     */
    void deleteBatch(List<Long> ids);

    /**
     * 启用/禁用缺陷代码
     */
    void toggleDefectCodeStatus(Long id, Boolean enabled);

    /**
     * 批量更新状态
     */
    void updateBatchStatus(List<Long> ids, Boolean enabled);

    /**
     * 分页查询缺陷代码
     */
    PageResult<DefectCodeVO> queryDefectCodes(DefectCodeQueryDTO queryDTO);

    /**
     * 查询所有启用的缺陷代码
     */
    List<DefectCodeVO> getAllEnabledDefectCodes();

    /**
     * 按类型查询缺陷代码
     */
    List<DefectCodeVO> getDefectCodesByType(String defectType);

    /**
     * 获取严重缺陷代码
     */
    List<DefectCodeVO> getCriticalDefectCodes();

    /**
     * 检查缺陷代码是否已存在
     */
    boolean isDefectCodeExists(String defectCode);

    /**
     * 验证缺陷代码
     */
    void validateDefectCode(String defectCode);

    /**
     * 获取缺陷类型分布
     */
    Map<String, Long> getDefectTypeDistribution();

    /**
     * 获取缺陷严重程度分布
     */
    Map<String, Long> getSeverityDistribution();

    /**
     * 导出缺陷代码
     */
    List<Map<String, Object>> exportDefectCodes(DefectCodeQueryDTO queryDTO);

    /**
     * 获取缺陷统计（今日）
     */
    Map<String, Object> getTodayDefectStatistics();
}


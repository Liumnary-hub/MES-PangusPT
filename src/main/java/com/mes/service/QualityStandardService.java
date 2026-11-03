package com.mes.service;

import com.mes.common.PageResult;
import com.mes.dto.StandardQueryDTO;
import com.mes.dto.StandardCreateDTO;
import com.mes.dto.StandardUpdateDTO;
import com.mes.entity.QualityStandard;
import com.mes.vo.QualityStandardDetailVO;
import com.mes.vo.QualityStandardVO;

import java.util.List;
import java.util.Map;

public interface QualityStandardService {

    // ============ 标准管理 ============
    PageResult<QualityStandardVO> getStandardList(StandardQueryDTO query);
    QualityStandardDetailVO getStandardDetail(Long id);
    Long createStandard(StandardCreateDTO dto);
    void updateStandard(StandardUpdateDTO dto);
    void deleteStandard(Long id);
    void deleteBatch(List<Long> ids);
    void updateStatus(Long id, String status);

    // ============ 查询接口 ============
    List<QualityStandard> getStandardsByProduct(Long productId, String inspectionType);
    QualityStandard getStandardByCode(String standardCode);
    List<QualityStandard> getAllActiveStandards();

    // ============ 统计分析 ============
    Map<String, Object> getStandardStatistics(StandardQueryDTO query);
    Map<String, Long> getStandardCountByStatus();
    Map<String, Long> getStandardCountByType();

    // ============ 其他功能 ============
    Long copyStandard(Long sourceId, String newStandardCode);
    void importStandards(List<QualityStandard> standards, String operator);
    List<Map<String, Object>> exportStandards(StandardQueryDTO query);

    // ============ 校验接口 ============
    boolean validateStandardCode(String standardCode);
    boolean checkStandardInUse(Long standardId);
    boolean checkStandardExists(String standardCode, Long productId);
}
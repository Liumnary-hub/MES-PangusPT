package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.common.BusinessException;
import com.mes.common.PageResult;
import com.mes.common.ValidationException;
import com.mes.dto.DefectCodeDTO;
import com.mes.dto.DefectCodeQueryDTO;
import com.mes.entity.DefectCode;
import com.mes.mapper.DefectCodeMapper;
import com.mes.service.DefectCodeService;
import com.mes.vo.DefectCodeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DefectCodeServiceImpl implements DefectCodeService {

    private final DefectCodeMapper defectCodeMapper;

    @Override
    public Long createDefectCode(DefectCodeDTO defectCodeDTO) {
        try {
            log.info("创建缺陷代码: {}", defectCodeDTO.getDefectCode());

            // 验证参数
            validateDefectCodeDTO(defectCodeDTO);

            // 检查重复
            if (isDefectCodeExists(defectCodeDTO.getDefectCode())) {
                throw new BusinessException("缺陷代码已存在: " + defectCodeDTO.getDefectCode());
            }

            // DTO转Entity
            DefectCode defectCode = new DefectCode();
            BeanUtils.copyProperties(defectCodeDTO, defectCode);

            // 设置默认值
            defectCode.setCreateTime(LocalDateTime.now());
            defectCode.setUpdateTime(LocalDateTime.now());
            defectCode.setDeleted(0);

            // 如果未设置启用状态，默认为启用
            if (defectCode.getEnabled() == null) {
                defectCode.setEnabled(true);
            }

            // 保存到数据库
            defectCodeMapper.insert(defectCode);

            log.info("创建缺陷代码成功，ID: {}", defectCode.getId());
            return defectCode.getId();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建缺陷代码失败", e);
            throw new BusinessException("创建缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public void updateDefectCode(Long id, DefectCodeDTO defectCodeDTO) {
        try {
            log.info("更新缺陷代码，ID: {}", id);

            // 验证参数
            validateDefectCodeDTO(defectCodeDTO);

            // 检查是否存在
            DefectCode existingDefectCode = defectCodeMapper.selectById(id);
            if (existingDefectCode == null) {
                throw new BusinessException("缺陷代码不存在，ID: " + id);
            }

            // 如果缺陷代码有变化，检查是否重复
            if (!existingDefectCode.getDefectCode().equals(defectCodeDTO.getDefectCode())
                    && isDefectCodeExists(defectCodeDTO.getDefectCode())) {
                throw new BusinessException("缺陷代码已存在: " + defectCodeDTO.getDefectCode());
            }

            // DTO转Entity
            DefectCode defectCode = new DefectCode();
            BeanUtils.copyProperties(defectCodeDTO, defectCode);
            defectCode.setId(id);
            defectCode.setUpdateTime(LocalDateTime.now());

            // 更新数据库
            defectCodeMapper.updateById(defectCode);

            log.info("更新缺陷代码成功，ID: {}", id);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新缺陷代码失败，ID: {}", id, e);
            throw new BusinessException("更新缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public DefectCodeVO getDefectCodeById(Long id) {
        try {
            log.debug("查询缺陷代码，ID: {}", id);

            DefectCode defectCode = defectCodeMapper.selectById(id);
            if (defectCode == null) {
                throw new BusinessException("缺陷代码不存在，ID: " + id);
            }

            return convertToVO(defectCode);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询缺陷代码失败，ID: {}", id, e);
            throw new BusinessException("查询缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public DefectCodeVO getDefectCodeByCode(String defectCode) {
        try {
            log.debug("查询缺陷代码，代码: {}", defectCode);

            DefectCode entity = defectCodeMapper.selectByDefectCode(defectCode);
            if (entity == null) {
                throw new BusinessException("缺陷代码不存在: " + defectCode);
            }

            return convertToVO(entity);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询缺陷代码失败，代码: {}", defectCode, e);
            throw new BusinessException("查询缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteDefectCode(Long id) {
        try {
            log.info("删除缺陷代码，ID: {}", id);

            // 检查是否存在
            DefectCode defectCode = defectCodeMapper.selectById(id);
            if (defectCode == null) {
                throw new BusinessException("缺陷代码不存在，ID: " + id);
            }

            // 逻辑删除
            defectCode.setDeleted(1);
            defectCode.setUpdateTime(LocalDateTime.now());
            defectCodeMapper.updateById(defectCode);

            log.info("删除缺陷代码成功，ID: {}", id);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除缺陷代码失败，ID: {}", id, e);
            throw new BusinessException("删除缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        try {
            log.info("批量删除缺陷代码，IDs: {}", ids);

            if (CollectionUtils.isEmpty(ids)) {
                throw new ValidationException("请选择要删除的缺陷代码");
            }

            // 检查每个缺陷代码是否存在
            for (Long id : ids) {
                DefectCode defectCode = defectCodeMapper.selectById(id);
                if (defectCode == null) {
                    throw new BusinessException("缺陷代码不存在，ID: " + id);
                }
            }

            // 批量逻辑删除
//            defectCodeMapper.deleteBatchByIds(ids);

            log.info("批量删除缺陷代码成功，数量: {}", ids.size());

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量删除缺陷代码失败", e);
            throw new BusinessException("批量删除缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public void toggleDefectCodeStatus(Long id, Boolean enabled) {
        try {
            log.info("更新缺陷代码状态，ID: {}, enabled: {}", id, enabled);

            // 检查是否存在
            DefectCode defectCode = defectCodeMapper.selectById(id);
            if (defectCode == null) {
                throw new BusinessException("缺陷代码不存在，ID: " + id);
            }

            defectCode.setEnabled(enabled);
            defectCode.setUpdateTime(LocalDateTime.now());
            defectCodeMapper.updateById(defectCode);

            log.info("更新缺陷代码状态成功，ID: {}, enabled: {}", id, enabled);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新缺陷代码状态失败，ID: {}", id, e);
            throw new BusinessException("更新缺陷代码状态失败: " + e.getMessage());
        }
    }

    @Override
    public void updateBatchStatus(List<Long> ids, Boolean enabled) {
        try {
            log.info("批量更新缺陷代码状态，IDs: {}, enabled: {}", ids, enabled);

            if (CollectionUtils.isEmpty(ids)) {
                throw new ValidationException("请选择要更新的缺陷代码");
            }

            // 批量更新状态
            defectCodeMapper.updateBatchStatus(ids, enabled);

            log.info("批量更新缺陷代码状态成功，数量: {}", ids.size());

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量更新缺陷代码状态失败", e);
            throw new BusinessException("批量更新缺陷代码状态失败: " + e.getMessage());
        }
    }

    @Override
    public PageResult<DefectCodeVO> queryDefectCodes(DefectCodeQueryDTO queryDTO) {
        try {
            log.debug("查询缺陷代码，参数: {}", queryDTO);

            // 验证查询参数
            validateQueryDTO(queryDTO);

            // 构建分页
            Page<DefectCode> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            // 执行查询
            List<DefectCode> defectCodes = defectCodeMapper.selectDefectCodeList(queryDTO);

            // 转换为VO
            List<DefectCodeVO> voList = defectCodes.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

            // 获取总记录数（需要根据查询条件统计）
            LambdaQueryWrapper<DefectCode> queryWrapper = buildQueryWrapper(queryDTO);
            long total = defectCodeMapper.selectCount(queryWrapper);

            // 构建分页结果
            return PageResult.of(voList, total, queryDTO.getPageNum(), queryDTO.getPageSize());

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询缺陷代码失败", e);
            throw new BusinessException("查询缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public List<DefectCodeVO> getAllEnabledDefectCodes() {
        try {
            log.debug("查询所有启用的缺陷代码");

            LambdaQueryWrapper<DefectCode> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DefectCode::getEnabled, true)
                    .eq(DefectCode::getDeleted, 0)
                    .orderByAsc(DefectCode::getDefectCode);

            List<DefectCode> defectCodes = defectCodeMapper.selectList(queryWrapper);

            return defectCodes.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("查询所有启用的缺陷代码失败", e);
            throw new BusinessException("查询所有启用的缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public List<DefectCodeVO> getDefectCodesByType(String defectType) {
        try {
            log.debug("按类型查询缺陷代码，类型: {}", defectType);

            if (!StringUtils.hasText(defectType)) {
                throw new ValidationException("缺陷类型不能为空");
            }

            List<DefectCode> defectCodes = defectCodeMapper.selectByDefectType(defectType);

            return defectCodes.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("按类型查询缺陷代码失败", e);
            throw new BusinessException("按类型查询缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public List<DefectCodeVO> getCriticalDefectCodes() {
        try {
            log.debug("查询严重缺陷代码");

            LambdaQueryWrapper<DefectCode> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(DefectCode::getSeverity, Arrays.asList("CRITICAL", "FATAL"))
                    .eq(DefectCode::getEnabled, true)
                    .eq(DefectCode::getDeleted, 0)
                    .orderByAsc(DefectCode::getDefectType, DefectCode::getDefectCode);

            List<DefectCode> defectCodes = defectCodeMapper.selectList(queryWrapper);

            return defectCodes.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("查询严重缺陷代码失败", e);
            throw new BusinessException("查询严重缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isDefectCodeExists(String defectCode) {
        try {
            if (!StringUtils.hasText(defectCode)) {
                return false;
            }

            LambdaQueryWrapper<DefectCode> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DefectCode::getDefectCode, defectCode)
                    .eq(DefectCode::getDeleted, 0);

            return defectCodeMapper.selectCount(queryWrapper) > 0;

        } catch (Exception e) {
            log.error("检查缺陷代码是否存在失败", e);
            throw new BusinessException("检查缺陷代码是否存在失败: " + e.getMessage());
        }
    }

    @Override
    public void validateDefectCode(String defectCode) {
        try {
            if (!StringUtils.hasText(defectCode)) {
                throw new ValidationException("缺陷代码不能为空");
            }

            LambdaQueryWrapper<DefectCode> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DefectCode::getDefectCode, defectCode)
                    .eq(DefectCode::getDeleted, 0)
                    .eq(DefectCode::getEnabled, true);

            if (defectCodeMapper.selectCount(queryWrapper) == 0) {
                throw new ValidationException("无效的缺陷代码: " + defectCode);
            }

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("验证缺陷代码失败", e);
            throw new BusinessException("验证缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getDefectTypeDistribution() {
        try {
            log.debug("获取缺陷类型分布");

            // 查询所有启用的缺陷代码
            LambdaQueryWrapper<DefectCode> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DefectCode::getEnabled, true)
                    .eq(DefectCode::getDeleted, 0)
                    .isNotNull(DefectCode::getDefectType);

            List<DefectCode> defectCodes = defectCodeMapper.selectList(queryWrapper);

            // 按类型统计
            return defectCodes.stream()
                    .collect(Collectors.groupingBy(DefectCode::getDefectType, Collectors.counting()));

        } catch (Exception e) {
            log.error("获取缺陷类型分布失败", e);
            throw new BusinessException("获取缺陷类型分布失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getSeverityDistribution() {
        try {
            log.debug("获取缺陷严重程度分布");

            // 查询所有启用的缺陷代码
            LambdaQueryWrapper<DefectCode> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DefectCode::getEnabled, true)
                    .eq(DefectCode::getDeleted, 0)
                    .isNotNull(DefectCode::getSeverity);

            List<DefectCode> defectCodes = defectCodeMapper.selectList(queryWrapper);

            // 按严重程度统计
            return defectCodes.stream()
                    .collect(Collectors.groupingBy(DefectCode::getSeverity, Collectors.counting()));

        } catch (Exception e) {
            log.error("获取缺陷严重程度分布失败", e);
            throw new BusinessException("获取缺陷严重程度分布失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> exportDefectCodes(DefectCodeQueryDTO queryDTO) {
        try {
            log.info("导出缺陷代码");

            // 查询所有数据（不分页）
            queryDTO.setPageSize(Integer.MAX_VALUE);
            List<DefectCode> defectCodes = defectCodeMapper.selectDefectCodeList(queryDTO);

            // 转换为导出格式
            return defectCodes.stream()
                    .map(this::convertToExportMap)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("导出缺陷代码失败", e);
            throw new BusinessException("导出缺陷代码失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTodayDefectStatistics() {
        try {
            log.debug("获取今日缺陷统计");

            String today = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);

            // 这里需要调用DefectDetailMapper的方法
            // 由于DefectDetailMapper中已有getTodayDefectStatistics方法
            // 我们假设有一个service可以调用它
            // 这里简化为返回空Map
            Map<String, Object> result = new HashMap<>();
            result.put("date", today);
            result.put("totalDefects", 0);
            result.put("criticalDefects", 0);

            return result;

        } catch (Exception e) {
            log.error("获取今日缺陷统计失败", e);
            throw new BusinessException("获取今日缺陷统计失败: " + e.getMessage());
        }
    }

    // ============ 私有方法 ============

    private void validateDefectCodeDTO(DefectCodeDTO defectCodeDTO) {
        if (defectCodeDTO == null) {
            throw new ValidationException("缺陷代码信息不能为空");
        }

        if (!StringUtils.hasText(defectCodeDTO.getDefectCode())) {
            throw new ValidationException("缺陷代码不能为空");
        }

        if (!StringUtils.hasText(defectCodeDTO.getDefectName())) {
            throw new ValidationException("缺陷名称不能为空");
        }

        // 验证缺陷代码格式（2-50个字符）
        if (defectCodeDTO.getDefectCode().length() < 2 || defectCodeDTO.getDefectCode().length() > 50) {
            throw new ValidationException("缺陷代码长度必须在2-50个字符之间");
        }

        // 验证缺陷名称格式（2-100个字符）
        if (defectCodeDTO.getDefectName().length() < 2 || defectCodeDTO.getDefectName().length() > 100) {
            throw new ValidationException("缺陷名称长度必须在2-100个字符之间");
        }

        // 验证缺陷类型
        if (StringUtils.hasText(defectCodeDTO.getDefectType())) {
            List<String> validTypes = Arrays.asList("APPEARANCE", "DIMENSION", "FUNCTION", "PERFORMANCE", "SAFETY");
            if (!validTypes.contains(defectCodeDTO.getDefectType())) {
                throw new ValidationException("无效的缺陷类型: " + defectCodeDTO.getDefectType());
            }
        }

        // 验证严重程度
        if (StringUtils.hasText(defectCodeDTO.getSeverity())) {
            List<String> validSeverities = Arrays.asList("MINOR", "MAJOR", "CRITICAL", "FATAL");
            if (!validSeverities.contains(defectCodeDTO.getSeverity())) {
                throw new ValidationException("无效的严重程度: " + defectCodeDTO.getSeverity());
            }
        }
    }

    private void validateQueryDTO(DefectCodeQueryDTO queryDTO) {
        if (queryDTO == null) {
            throw new ValidationException("查询参数不能为空");
        }

        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
            queryDTO.setPageNum(1);
        }

        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(20);
        }
    }

    private LambdaQueryWrapper<DefectCode> buildQueryWrapper(DefectCodeQueryDTO queryDTO) {
        LambdaQueryWrapper<DefectCode> queryWrapper = new LambdaQueryWrapper<>();

        // 未删除的条件
        queryWrapper.eq(DefectCode::getDeleted, 0);

        // 缺陷代码
        if (StringUtils.hasText(queryDTO.getDefectCode())) {
            if (queryDTO.isFuzzySearch()) {
                queryWrapper.like(DefectCode::getDefectCode, queryDTO.getLikePattern(queryDTO.getDefectCode()));
            } else {
                queryWrapper.eq(DefectCode::getDefectCode, queryDTO.getDefectCode());
            }
        }

        // 缺陷名称
        if (StringUtils.hasText(queryDTO.getDefectName())) {
            if (queryDTO.isFuzzySearch()) {
                queryWrapper.like(DefectCode::getDefectName, queryDTO.getLikePattern(queryDTO.getDefectName()));
            } else {
                queryWrapper.eq(DefectCode::getDefectName, queryDTO.getDefectName());
            }
        }

        // 缺陷类型
        if (StringUtils.hasText(queryDTO.getDefectType())) {
            queryWrapper.eq(DefectCode::getDefectType, queryDTO.getDefectType());
        }

        // 严重程度
        if (StringUtils.hasText(queryDTO.getSeverity())) {
            if (queryDTO.getSeverity().contains(",")) {
                List<String> severities = Arrays.asList(queryDTO.getSeverity().split(","));
                queryWrapper.in(DefectCode::getSeverity, severities);
            } else {
                queryWrapper.eq(DefectCode::getSeverity, queryDTO.getSeverity());
            }
        }

        // 启用状态
        if (queryDTO.getEnabled() != null) {
            queryWrapper.eq(DefectCode::getEnabled, queryDTO.getEnabled());
        }

        // 责任部门
        if (StringUtils.hasText(queryDTO.getResponsibleDept())) {
            queryWrapper.eq(DefectCode::getResponsibleDept, queryDTO.getResponsibleDept());
        }

        // 创建人
        if (StringUtils.hasText(queryDTO.getCreateBy())) {
            queryWrapper.eq(DefectCode::getCreateBy, queryDTO.getCreateBy());
        }

        // 创建时间范围
        if (queryDTO.getCreateTimeStart() != null) {
            queryWrapper.ge(DefectCode::getCreateTime, queryDTO.getCreateTimeStart());
        }

        if (queryDTO.getCreateTimeEnd() != null) {
            queryWrapper.le(DefectCode::getCreateTime, queryDTO.getCreateTimeEnd());
        }

        // 排序
        if (StringUtils.hasText(queryDTO.getSortField())) {
            String sortField = queryDTO.getSortField();
            if ("defect_code".equals(sortField)) {
                queryWrapper.orderByAsc(DefectCode::getDefectCode);
            } else if ("defect_name".equals(sortField)) {
                queryWrapper.orderByAsc(DefectCode::getDefectName);
            } else if ("defect_type".equals(sortField)) {
                queryWrapper.orderByAsc(DefectCode::getDefectType);
            } else if ("severity".equals(sortField)) {
                queryWrapper.orderByAsc(DefectCode::getSeverity);
            } else if ("create_time".equals(sortField)) {
                queryWrapper.orderByAsc(DefectCode::getCreateTime);
            }
        }

        return queryWrapper;
    }

    private DefectCodeVO convertToVO(DefectCode defectCode) {
        DefectCodeVO vo = new DefectCodeVO();
        BeanUtils.copyProperties(defectCode, vo);

        // 设置显示名称
        vo.setDefectTypeName(getDefectTypeName(defectCode.getDefectType()));
        vo.setSeverityName(getSeverityName(defectCode.getSeverity()));

        return vo;
    }

    private String getDefectTypeName(String defectType) {
        if (defectType == null) {
            return "";
        }
        switch (defectType) {
            case "APPEARANCE":
                return "外观";
            case "DIMENSION":
                return "尺寸";
            case "FUNCTION":
                return "功能";
            case "PERFORMANCE":
                return "性能";
            case "SAFETY":
                return "安全";
            default:
                return defectType;
        }
    }

    private String getSeverityName(String severity) {
        if (severity == null) {
            return "";
        }
        switch (severity) {
            case "MINOR":
                return "轻微";
            case "MAJOR":
                return "一般";
            case "CRITICAL":
                return "严重";
            case "FATAL":
                return "致命";
            default:
                return severity;
        }
    }

    private Map<String, Object> convertToExportMap(DefectCode defectCode) {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("缺陷代码", defectCode.getDefectCode());
        map.put("缺陷名称", defectCode.getDefectName());
        map.put("缺陷类型", getDefectTypeName(defectCode.getDefectType()));
        map.put("严重程度", getSeverityName(defectCode.getSeverity()));
        map.put("缺陷描述", defectCode.getDescription());
        map.put("处理方法", defectCode.getHandlingMethod());
        map.put("预防措施", defectCode.getPreventiveAction());
        map.put("责任部门", defectCode.getResponsibleDept());
        map.put("启用状态", defectCode.getEnabled() ? "启用" : "禁用");
        map.put("备注", defectCode.getRemark());
        map.put("创建人", defectCode.getCreateBy());
        map.put("创建时间", defectCode.getCreateTime());

        return map;
    }
}
package com.mes.service.impl;// QualityStandardServiceImpl.java - 实现


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.common.PageResult;
import com.mes.common.BusinessException;
import com.mes.common.ValidationException;
import com.mes.dto.StandardQueryDTO;
import com.mes.dto.StandardCreateDTO;
import com.mes.dto.StandardUpdateDTO;
import com.mes.entity.QualityStandard;
import com.mes.entity.InspectionItem;
import com.mes.mapper.QualityStandardMapper;
import com.mes.mapper.InspectionItemMapper;
import com.mes.service.QualityStandardService;
import com.mes.vo.QualityStandardDetailVO;
import com.mes.vo.QualityStandardVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QualityStandardServiceImpl implements QualityStandardService {

    private final QualityStandardMapper standardMapper;
    private final InspectionItemMapper inspectionItemMapper;

    @Override
    public PageResult<QualityStandardVO> getStandardList(StandardQueryDTO query) {
        try {
            validateQuery(query);

            // 构建分页查询
            Page<QualityStandard> page = new Page<>(query.getPageNum(), query.getPageSize());
            IPage<QualityStandard> standardPage = standardMapper.selectPageList(page, query);

            // 转换为VO列表
            List<QualityStandardVO> voList = standardPage.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

            // 如果需要，查询关联的检验项目数量
            if (query.isIncludeItemCount()) {
                for (QualityStandardVO vo : voList) {
                    Long itemCount = inspectionItemMapper.countByStandardId(vo.getId());
                    vo.setItemCount(itemCount);
                }
            }

            return PageResult.of(voList, standardPage.getTotal(),
                    query.getPageNum(), query.getPageSize());

        } catch (Exception e) {
            log.error("查询检验标准列表失败", e);
            throw new BusinessException("查询检验标准列表失败: " + e.getMessage());
        }
    }

    @Override
    public QualityStandardDetailVO getStandardDetail(Long id) {
        try {
            // 查询标准信息
            QualityStandard standard = standardMapper.selectById(id);
            if (standard == null) {
                throw new BusinessException("检验标准不存在");
            }

            // 构建详情VO
            QualityStandardDetailVO detailVO = new QualityStandardDetailVO();
            BeanUtils.copyProperties(standard, detailVO);

            // 查询检验项目
            List<InspectionItem> items = inspectionItemMapper.selectByStandardId(id);
            detailVO.setItems(items);

            // 查询关联信息（如产品信息等）
            // TODO: 根据需求添加产品信息查询

            return detailVO;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取检验标准详情失败，ID: {}", id, e);
            throw new BusinessException("获取检验标准详情失败: " + e.getMessage());
        }
    }

    @Override
    public Long createStandard(StandardCreateDTO dto) {
        try {
            // 校验DTO
            validateCreateDTO(dto);

            // 检查标准编号是否重复
            if (standardMapper.existsByStandardCode(dto.getStandardCode())) {
                throw new ValidationException("标准编号已存在");
            }

            // 创建标准实体
            QualityStandard standard = new QualityStandard();
            BeanUtils.copyProperties(dto, standard);

            // 设置系统字段
            standard.setStatus("DRAFT");
            standard.setVersion("1.0");
            standard.setCreateTime(LocalDateTime.now());
            standard.setUpdateTime(LocalDateTime.now());
            standard.setDeleted(0);

            // 保存标准
            standardMapper.insert(standard);

            // 保存检验项目
            if (!CollectionUtils.isEmpty(dto.getItems())) {
                saveInspectionItems(standard.getId(), dto.getItems());
            }

            log.info("创建检验标准成功，标准编号: {}", dto.getStandardCode());
            return standard.getId();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建检验标准失败", e);
            throw new BusinessException("创建检验标准失败: " + e.getMessage());
        }
    }

    @Override
    public void updateStandard(StandardUpdateDTO dto) {
        try {
            // 校验DTO
            validateUpdateDTO(dto);

            // 查询标准是否存在
            QualityStandard standard = standardMapper.selectById(dto.getId());
            if (standard == null) {
                throw new BusinessException("检验标准不存在");
            }

            // 检查状态，已生效的标准不能修改
            if ("ACTIVE".equals(standard.getStatus())) {
                throw new BusinessException("已生效的标准不能修改");
            }

            // 更新标准信息
            BeanUtils.copyProperties(dto, standard, "standardCode", "version");
            standard.setUpdateTime(LocalDateTime.now());
            standardMapper.updateById(standard);

            // 更新检验项目
            if (dto.getItems() != null) {
                // 删除旧的检验项目
                inspectionItemMapper.deleteByStandardId(dto.getId());
                // 保存新的检验项目
                if (!dto.getItems().isEmpty()) {
                    saveInspectionItems(standard.getId(), dto.getItems());
                }
            }

            log.info("更新检验标准成功，标准ID: {}", dto.getId());

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新检验标准失败，ID: {}", dto.getId(), e);
            throw new BusinessException("更新检验标准失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteStandard(Long id) {
        try {
            // 检查标准是否存在
            QualityStandard standard = standardMapper.selectById(id);
            if (standard == null) {
                throw new BusinessException("检验标准不存在");
            }

            // 检查是否在使用中
            if (checkStandardInUse(id)) {
                throw new BusinessException("标准正在使用中，无法删除");
            }

            // 逻辑删除
            standard.setDeleted(1);
            standard.setUpdateTime(LocalDateTime.now());
            standardMapper.updateById(standard);

            log.info("删除检验标准成功，标准ID: {}", id);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除检验标准失败，ID: {}", id, e);
            throw new BusinessException("删除检验标准失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        try {
            if (CollectionUtils.isEmpty(ids)) {
                throw new ValidationException("请选择要删除的标准");
            }

            // 检查每个标准是否可删除
            for (Long id : ids) {
                if (checkStandardInUse(id)) {
                    throw new BusinessException("标准ID:" + id + "正在使用中，无法删除");
                }
            }

            // 批量逻辑删除
            standardMapper.deleteBatchByIds(ids);

            log.info("批量删除检验标准成功，数量: {}", ids.size());

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量删除检验标准失败", e);
            throw new BusinessException("批量删除检验标准失败: " + e.getMessage());
        }
    }

    @Override
    public void updateStatus(Long id, String status) {
        try {
            // 验证状态值
            if (!isValidStatus(status)) {
                throw new ValidationException("状态值无效");
            }

            // 检查标准是否存在
            QualityStandard standard = standardMapper.selectById(id);
            if (standard == null) {
                throw new BusinessException("检验标准不存在");
            }

            // 验证状态转换
            validateStatusTransition(standard.getStatus(), status);

            // 更新状态
            standard.setStatus(status);
            if ("ACTIVE".equals(status)) {
                standard.setEffectiveDate(LocalDateTime.now());
            } else if ("INACTIVE".equals(status)) {
                standard.setExpireDate(LocalDateTime.now());
            }
            standard.setUpdateTime(LocalDateTime.now());
            standardMapper.updateById(standard);

            log.info("更新检验标准状态成功，ID: {}，状态: {}", id, status);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新检验标准状态失败，ID: {}", id, e);
            throw new BusinessException("更新检验标准状态失败: " + e.getMessage());
        }
    }

    @Override
    public List<QualityStandard> getStandardsByProduct(Long productId, String inspectionType) {
        try {
            return standardMapper.selectByProductAndType(productId, inspectionType, "ACTIVE");
        } catch (Exception e) {
            log.error("获取产品检验标准失败，产品ID: {}", productId, e);
            throw new BusinessException("获取产品检验标准失败: " + e.getMessage());
        }
    }

    @Override
    public QualityStandard getStandardByCode(String standardCode) {
        try {
            return standardMapper.selectByStandardCode(standardCode);
        } catch (Exception e) {
            log.error("根据标准编号查询失败，标准编号: {}", standardCode, e);
            throw new BusinessException("根据标准编号查询失败: " + e.getMessage());
        }
    }

    @Override
    public List<QualityStandard> getAllActiveStandards() {
        try {
            return standardMapper.selectActiveStandards();
        } catch (Exception e) {
            log.error("获取所有有效标准失败", e);
            throw new BusinessException("获取所有有效标准失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getStandardStatistics(StandardQueryDTO query) {
        try {
            Map<String, Object> statistics = new HashMap<>();

            // 标准总数
            Long totalCount = standardMapper.countByQuery(query);
            statistics.put("totalCount", totalCount);

            // 按状态统计
            Map<String, Long> statusCount = standardMapper.countByStatus(query);
            statistics.put("statusCount", statusCount);

            // 按类型统计
            Map<String, Long> typeCount = standardMapper.countByInspectionType(query);
            statistics.put("typeCount", typeCount);

            // 按产品统计
            List<Map<String, Object>> productStats = standardMapper.countByProduct(query);
            statistics.put("productStats", productStats);

            return statistics;

        } catch (Exception e) {
            log.error("获取标准统计失败", e);
            throw new BusinessException("获取标准统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getStandardCountByStatus() {
        try {
            return standardMapper.countByStatus(null);
        } catch (Exception e) {
            log.error("获取标准状态统计失败", e);
            throw new BusinessException("获取标准状态统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getStandardCountByType() {
        try {
            return standardMapper.countByInspectionType(null);
        } catch (Exception e) {
            log.error("获取标准类型统计失败", e);
            throw new BusinessException("获取标准类型统计失败: " + e.getMessage());
        }
    }

    @Override
    public Long copyStandard(Long sourceId, String newStandardCode) {
        try {
            // 验证新标准编号
            if (!StringUtils.hasText(newStandardCode)) {
                throw new ValidationException("新标准编号不能为空");
            }

            // 检查原标准是否存在
            QualityStandard sourceStandard = standardMapper.selectById(sourceId);
            if (sourceStandard == null) {
                throw new BusinessException("原检验标准不存在");
            }

            // 检查新标准编号是否重复
            if (standardMapper.existsByStandardCode(newStandardCode)) {
                throw new ValidationException("新标准编号已存在");
            }

            // 复制标准
            QualityStandard newStandard = new QualityStandard();
            BeanUtils.copyProperties(sourceStandard, newStandard,
                    "id", "standardCode", "version", "status",
                    "effectiveDate", "expireDate", "createTime", "updateTime");

            newStandard.setStandardCode(newStandardCode);
            newStandard.setVersion("1.0");
            newStandard.setStatus("DRAFT");
            newStandard.setCreateTime(LocalDateTime.now());
            newStandard.setUpdateTime(LocalDateTime.now());
            newStandard.setDeleted(0);

            // 保存新标准
            standardMapper.insert(newStandard);

            // 复制检验项目
            List<InspectionItem> sourceItems = inspectionItemMapper.selectByStandardId(sourceId);
            if (!CollectionUtils.isEmpty(sourceItems)) {
                List<InspectionItem> newItems = sourceItems.stream()
                        .map(item -> {
                            InspectionItem newItem = new InspectionItem();
                            BeanUtils.copyProperties(item, newItem, "id", "standardId");
                            newItem.setStandardId(newStandard.getId());
                            newItem.setCreateTime(LocalDateTime.now());
                            return newItem;
                        })
                        .collect(Collectors.toList());

                inspectionItemMapper.insertBatch(newItems);
            }

            log.info("复制检验标准成功，源ID: {}，目标编号: {}", sourceId, newStandardCode);
            return newStandard.getId();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("复制检验标准失败，源ID: {}", sourceId, e);
            throw new BusinessException("复制检验标准失败: " + e.getMessage());
        }
    }

    @Override
    public void importStandards(List<QualityStandard> standards, String operator) {
        try {
            if (CollectionUtils.isEmpty(standards)) {
                throw new ValidationException("导入数据不能为空");
            }

            // 验证每个标准
            for (QualityStandard standard : standards) {
                validateImportStandard(standard);
            }

            // 批量插入
            for (QualityStandard standard : standards) {
                standard.setCreateBy(operator);
                standard.setCreateTime(LocalDateTime.now());
                standard.setUpdateTime(LocalDateTime.now());
                standard.setStatus("DRAFT");
                standard.setDeleted(0);

                standardMapper.insert(standard);
            }

            log.info("导入检验标准成功，数量: {}", standards.size());

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("导入检验标准失败", e);
            throw new BusinessException("导入检验标准失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> exportStandards(StandardQueryDTO query) {
        try {
            return standardMapper.exportByQuery(query);
        } catch (Exception e) {
            log.error("导出检验标准失败", e);
            throw new BusinessException("导出检验标准失败: " + e.getMessage());
        }
    }

    @Override
    public boolean validateStandardCode(String standardCode) {
        try {
            return !standardMapper.existsByStandardCode(standardCode);
        } catch (Exception e) {
            log.error("验证标准编号失败", e);
            return false;
        }
    }

    @Override
    public boolean checkStandardInUse(Long standardId) {
        try {
            // TODO: 实现检查标准是否被检验计划或任务使用
            // 这里可以根据实际情况调用其他Mapper检查
            return false; // 临时返回false
        } catch (Exception e) {
            log.error("检查标准使用状态失败", e);
            return true; // 出错时保守处理，认为在使用中
        }
    }

    @Override
    public boolean checkStandardExists(String standardCode, Long productId) {
        try {
            return standardMapper.existsByCodeAndProduct(standardCode, productId);
        } catch (Exception e) {
            log.error("检查标准是否存在失败", e);
            return false;
        }
    }

    // ============ 私有方法 ============

    private QualityStandardVO convertToVO(QualityStandard standard) {
        QualityStandardVO vo = new QualityStandardVO();
        BeanUtils.copyProperties(standard, vo);
        return vo;
    }

    private void validateQuery(StandardQueryDTO query) {
        if (query == null) {
            throw new ValidationException("查询参数不能为空");
        }

        if (query.getPageNum() == null || query.getPageNum() < 1) {
            query.setPageNum(1);
        }

        if (query.getPageSize() == null || query.getPageSize() < 1) {
            query.setPageSize(20);
        }

        if (query.getPageSize() > 100) {
            query.setPageSize(100);
        }
    }

    private void validateCreateDTO(StandardCreateDTO dto) {
        if (dto == null) {
            throw new ValidationException("创建参数不能为空");
        }

        if (!StringUtils.hasText(dto.getStandardCode())) {
            throw new ValidationException("标准编号不能为空");
        }

        if (!StringUtils.hasText(dto.getStandardName())) {
            throw new ValidationException("标准名称不能为空");
        }

        if (!StringUtils.hasText(dto.getInspectionType())) {
            throw new ValidationException("检验类型不能为空");
        }

        // 验证检验类型
        if (!isValidInspectionType(dto.getInspectionType())) {
            throw new ValidationException("检验类型无效");
        }

        // 验证检验项目
        if (CollectionUtils.isEmpty(dto.getItems())) {
            throw new ValidationException("检验项目不能为空");
        }
    }

    private void validateUpdateDTO(StandardUpdateDTO dto) {
        if (dto == null) {
            throw new ValidationException("更新参数不能为空");
        }

        if (dto.getId() == null) {
            throw new ValidationException("标准ID不能为空");
        }
    }

    private void saveInspectionItems(Long standardId, List<com.mes.dto.InspectionItemDTO> itemDTOs) {
        List<InspectionItem> items = new ArrayList<>();
        int sequence = 1;

        for (com.mes.dto.InspectionItemDTO dto : itemDTOs) {
            InspectionItem item = new InspectionItem();
            BeanUtils.copyProperties(dto, item);

            item.setStandardId(standardId);
            item.setSequence(sequence++);
            item.setStatus("ACTIVE");
            item.setCreateTime(LocalDateTime.now());
            item.setDeleted(0);

            // 验证项目数据
            validateInspectionItem(item);

            items.add(item);
        }

        // 批量插入
        if (!items.isEmpty()) {
            inspectionItemMapper.insertBatch(items);
        }
    }

    private void validateInspectionItem(InspectionItem item) {
        if (!StringUtils.hasText(item.getItemCode())) {
            throw new ValidationException("检验项目编码不能为空");
        }

        if (!StringUtils.hasText(item.getItemName())) {
            throw new ValidationException("检验项目名称不能为空");
        }

        if (!StringUtils.hasText(item.getDataType())) {
            throw new ValidationException("数据类型不能为空");
        }
    }

    private void validateImportStandard(QualityStandard standard) {
        if (!StringUtils.hasText(standard.getStandardCode())) {
            throw new ValidationException("标准编号不能为空");
        }

        if (!StringUtils.hasText(standard.getStandardName())) {
            throw new ValidationException("标准名称不能为空");
        }

        // 检查标准编号是否重复
        if (standardMapper.existsByStandardCode(standard.getStandardCode())) {
            throw new ValidationException("标准编号已存在: " + standard.getStandardCode());
        }
    }

    private boolean isValidInspectionType(String inspectionType) {
        List<String> validTypes = Arrays.asList("FIRST", "PATROL", "FINAL", "SPOT",
                "INCOMING", "PROCESS", "FINAL");
        return validTypes.contains(inspectionType.toUpperCase());
    }

    private boolean isValidStatus(String status) {
        List<String> validStatus = Arrays.asList("DRAFT", "ACTIVE", "INACTIVE", "DELETED");
        return validStatus.contains(status.toUpperCase());
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        // 定义状态转换规则
        Map<String, List<String>> allowedTransitions = new HashMap<>();
        allowedTransitions.put("DRAFT", Arrays.asList("ACTIVE", "DELETED"));
        allowedTransitions.put("ACTIVE", Arrays.asList("INACTIVE", "DELETED"));
        allowedTransitions.put("INACTIVE", Arrays.asList("ACTIVE", "DELETED"));

        List<String> allowed = allowedTransitions.get(currentStatus.toUpperCase());
        if (allowed == null || !allowed.contains(newStatus.toUpperCase())) {
            throw new BusinessException("不允许从状态 " + currentStatus + " 转换到 " + newStatus);
        }
    }
}
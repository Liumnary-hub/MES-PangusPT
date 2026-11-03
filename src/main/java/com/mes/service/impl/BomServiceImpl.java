package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.dto.BomDTO;
import com.mes.dto.BomQueryDTO;
import com.mes.entity.Bom;
import com.mes.entity.BomDetail;
import com.mes.entity.Material;
import com.mes.mapper.BomDetailMapper;
import com.mes.mapper.BomMapper;
import com.mes.mapper.MaterialMapper;
import com.mes.service.BomService;
import com.mes.util.SecurityUtils;
import com.mes.util.TableDataInfo;
import com.mes.vo.BomDetailVO;
import com.mes.vo.BomVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BomServiceImpl extends ServiceImpl<BomMapper, Bom> implements BomService {

    private final BomMapper bomMapper;
    private final BomDetailMapper bomDetailMapper;
    private final MaterialMapper materialMapper;

    @Override
    public TableDataInfo<BomVO> selectBomPage(BomQueryDTO queryDTO) {
        int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

        Page<BomVO> page = new Page<>(pageNum, pageSize);
        BomVO queryVO = new BomVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        IPage<BomVO> result = bomMapper.selectBomPage((Page<BomVO>) page, queryVO);

        // 补充BOM明细信息
        List<BomVO> voList = result.getRecords();
        for (BomVO bomVO : voList) {
            List<BomDetail> details = bomDetailMapper.selectByBomId(bomVO.getId());
            bomVO.setBomDetails(convertToDetailVOList(details));
        }

        TableDataInfo<BomVO> tableData = new TableDataInfo<>();
        tableData.setRows(voList);
        tableData.setTotal(result.getTotal());
        tableData.setCode(200);
        tableData.setMsg("查询成功");

        return tableData;
    }

    @Override
    public List<BomVO> selectBomList(BomQueryDTO queryDTO) {
        BomVO queryVO = new BomVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        Page<BomVO> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<BomVO> result = bomMapper.selectBomPage((Page<BomVO>) page, queryVO);

        List<BomVO> voList = result.getRecords();
        for (BomVO bomVO : voList) {
            List<BomDetail> details = bomDetailMapper.selectByBomId(bomVO.getId());
            bomVO.setBomDetails(convertToDetailVOList(details));
        }

        return voList;
    }

    @Override
    public BomVO selectByProductId(Long productId) {
        LambdaQueryWrapper<Bom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Bom::getProductId, productId);
        wrapper.eq(Bom::getStatus, "active");

        Bom bom = bomMapper.selectOne(wrapper);
        if (bom == null) {
            return null;
        }

        BomVO bomVO = convertToVO(bom);
        List<BomDetail> details = bomDetailMapper.selectByBomId(bom.getId());
        bomVO.setBomDetails(convertToDetailVOList(details));

        return bomVO;
    }

    @Override
    public BomVO selectByProductCode(String productCode) {
        LambdaQueryWrapper<Bom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Bom::getProductCode, productCode);
        wrapper.eq(Bom::getStatus, "active");

        Bom bom = bomMapper.selectOne(wrapper);
        if (bom == null) {
            return null;
        }

        BomVO bomVO = convertToVO(bom);
        List<BomDetail> details = bomDetailMapper.selectByBomId(bom.getId());
        bomVO.setBomDetails(convertToDetailVOList(details));

        return bomVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertBom(BomDTO bomDTO) {
        // 检查BOM编码唯一性
        if (!checkBomCodeUnique(bomDTO.getBomCode(), null)) {
            throw new RuntimeException("BOM编码已存在: " + bomDTO.getBomCode());
        }

        // 保存BOM主表
        Bom bom = convertToEntity(bomDTO);
        bom.setCreateUser(SecurityUtils.getUsername());
        bom.setUpdateUser(SecurityUtils.getUsername());

        boolean bomSaved = bomMapper.insert(bom) > 0;

        if (bomSaved && bomDTO.getBomDetails() != null) {
            // 保存BOM明细
            for (int i = 0; i < bomDTO.getBomDetails().size(); i++) {
                BomDetail detail = convertToDetailEntity(bomDTO.getBomDetails().get(i));
                detail.setBomId(bom.getId());
                detail.setSequence(i + 1);
                bomDetailMapper.insert(detail);
            }
        }

        return bomSaved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBom(BomDTO bomDTO) {
        // 检查BOM编码唯一性
        if (!checkBomCodeUnique(bomDTO.getBomCode(), bomDTO.getId())) {
            throw new RuntimeException("BOM编码已存在: " + bomDTO.getBomCode());
        }

        // 更新BOM主表
        Bom bom = convertToEntity(bomDTO);
        bom.setUpdateUser(SecurityUtils.getUsername());
        bom.setUpdateTime(LocalDateTime.now());

        boolean bomUpdated = bomMapper.updateById(bom) > 0;

        if (bomUpdated && bomDTO.getBomDetails() != null) {
            // 删除原有明细
            LambdaQueryWrapper<BomDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BomDetail::getBomId, bom.getId());
            bomDetailMapper.delete(wrapper);

            // 保存新明细
            for (int i = 0; i < bomDTO.getBomDetails().size(); i++) {
                BomDetail detail = convertToDetailEntity(bomDTO.getBomDetails().get(i));
                detail.setBomId(bom.getId());
                detail.setSequence(i + 1);
                bomDetailMapper.insert(detail);
            }
        }

        return bomUpdated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBomByIds(List<Long> ids) {
        for (Long id : ids) {
            // 删除BOM明细
            LambdaQueryWrapper<BomDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BomDetail::getBomId, id);
            bomDetailMapper.delete(wrapper);

            // 删除BOM主表
            bomMapper.deleteById(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyBom(Long sourceBomId, String newBomCode, String newVersion) {
        // 检查新BOM编码唯一性
        if (!checkBomCodeUnique(newBomCode, null)) {
            throw new RuntimeException("BOM编码已存在: " + newBomCode);
        }

        // 查询源BOM
        Bom sourceBom = bomMapper.selectById(sourceBomId);
        if (sourceBom == null) {
            throw new RuntimeException("源BOM不存在");
        }

        // 复制BOM主表
        Bom newBom = new Bom();
        BeanUtils.copyProperties(sourceBom, newBom);
        newBom.setId(null);
        newBom.setBomCode(newBomCode);
        newBom.setVersion(newVersion);
        newBom.setCreateUser(SecurityUtils.getUsername());
        newBom.setUpdateUser(SecurityUtils.getUsername());
        newBom.setCreateTime(LocalDateTime.now());
        newBom.setUpdateTime(LocalDateTime.now());

        boolean bomSaved = bomMapper.insert(newBom) > 0;

        if (bomSaved) {
            // 复制BOM明细
            List<BomDetail> sourceDetails = bomDetailMapper.selectByBomId(sourceBomId);
            for (BomDetail sourceDetail : sourceDetails) {
                BomDetail newDetail = new BomDetail();
                BeanUtils.copyProperties(sourceDetail, newDetail);
                newDetail.setId(null);
                newDetail.setBomId(newBom.getId());
                bomDetailMapper.insert(newDetail);
            }
        }

        return bomSaved;
    }

    @Override
    public List<BomVO> calculateMaterialRequirement(Long productId, Double quantity) {
        BomVO bom = selectByProductId(productId);
        if (bom == null) {
            throw new RuntimeException("产品BOM不存在");
        }

        List<BomVO> requirements = new ArrayList<>();
        for (BomDetailVO detail : bom.getBomDetails()) {
            BomVO requirement = new BomVO();
            requirement.setMaterialId(detail.getMaterialId());
            requirement.setMaterialCode(detail.getMaterialCode());
            requirement.setMaterialName(detail.getMaterialName());

            // 计算需求数量（考虑损耗率）
            double requiredQty = detail.getQuantity().doubleValue() * quantity;
            if (detail.getLossRate() != null && detail.getLossRate().doubleValue() > 0) {
                requiredQty *= (1 + detail.getLossRate().doubleValue() / 100);
            }
            requirement.setCurrentQuantity(requiredQty); // 借用字段存储需求数量

            requirements.add(requirement);
        }

        return requirements;
    }

    @Override
    public boolean checkBomCodeUnique(String bomCode, Long excludeId) {
        LambdaQueryWrapper<Bom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Bom::getBomCode, bomCode);
        if (excludeId != null) {
            wrapper.ne(Bom::getId, excludeId);
        }
        int count = Math.toIntExact(bomMapper.selectCount(wrapper));
        return count == 0;
    }

    /**
     * Entity 转 VO
     */
    private BomVO convertToVO(Bom bom) {
        BomVO vo = new BomVO();
        BeanUtils.copyProperties(bom, vo);
        vo.setStatusDesc(getStatusDesc(bom.getStatus()));
        return vo;
    }

    /**
     * DTO 转 Entity
     */
    private Bom convertToEntity(BomDTO dto) {
        Bom bom = new Bom();
        BeanUtils.copyProperties(dto, bom);
        return bom;
    }

    /**
     * DTO 转 Detail Entity
     */
    private BomDetail convertToDetailEntity(com.mes.dto.BomDetailDTO dto) {
        BomDetail detail = new BomDetail();
        BeanUtils.copyProperties(dto, detail);
        return detail;
    }

    /**
     * Detail Entity 转 VO
     */
    private List<com.mes.vo.BomDetailVO> convertToDetailVOList(List<BomDetail> details) {
        return details.stream().map(detail -> {
            com.mes.vo.BomDetailVO vo = new com.mes.vo.BomDetailVO();
            BeanUtils.copyProperties(detail, vo);

            // 补充物料信息
            Material material = materialMapper.selectById(detail.getMaterialId());
            if (material != null) {
                vo.setMaterialType(material.getMaterialType());
                vo.setSpecification(material.getSpecification());
            }

            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        if (status == null) return "";
        switch (status) {
            case "active": return "启用";
            case "inactive": return "停用";
            default: return status;
        }
    }
}
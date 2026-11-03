package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mes.dto.MaterialDTO;
import com.mes.dto.MaterialQueryDTO;
import com.mes.entity.Material;
import com.mes.mapper.MaterialMapper;
import com.mes.service.MaterialService;
import com.mes.util.PageUtils;
import com.mes.util.SecurityUtils;
import com.mes.util.TableDataInfo;
import com.mes.vo.MaterialVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

    private final MaterialMapper materialMapper;


//    @Override
//    public TableDataInfo<MaterialVO> selectMaterialPage(MaterialQueryDTO queryDTO) {
//        // 设置分页参数
//        int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
//        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
//
//        // 创建分页对象
//        Page<Material> page = new Page<>(pageNum, pageSize);
//
//        // 构建查询条件
//        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.isNotBlank(queryDTO.getMaterialCode())) {
//            queryWrapper.like(Material::getMaterialCode, queryDTO.getMaterialCode());
//        }
//        if (StringUtils.isNotBlank(queryDTO.getMaterialName())) {
//            queryWrapper.like(Material::getMaterialName, queryDTO.getMaterialName());
//        }
//        if (StringUtils.isNotBlank(queryDTO.getMaterialType())) {
//            queryWrapper.eq(Material::getMaterialType, queryDTO.getMaterialType());
//        }
//        if (StringUtils.isNotBlank(queryDTO.getStatus())) {
//            queryWrapper.eq(Material::getStatus, queryDTO.getStatus());
//        }
//
//        // 执行分页查询
//        IPage<Material> materialPage = materialMapper.selectPage(page, queryWrapper);
//
//        // 转换为 VO
//        List<MaterialVO> voList = materialPage.getRecords().stream().map(material -> {
//            MaterialVO vo = new MaterialVO();
//            BeanUtils.copyProperties(material, vo);
//            return vo;
//        }).collect(Collectors.toList());
//
//        return TableDataInfo.<MaterialVO>builder()
//                .total(materialPage.getTotal())
//                .rows(voList)
//                .build();
//    }

    @Override
    public TableDataInfo<MaterialVO> selectMaterialPage(MaterialQueryDTO queryDTO) {
        // 设置分页参数
        int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

        // 创建分页对象
        Page<Material> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<Material> queryWrapper = buildQueryWrapper(queryDTO);

        // 执行分页查询
        IPage<Material> materialPage = materialMapper.selectPage(page, queryWrapper);

        // 转换为 VO
        List<MaterialVO> voList = materialPage.getRecords().stream().map(material -> {
            MaterialVO vo = new MaterialVO();
            BeanUtils.copyProperties(material, vo);
            return vo;
        }).collect(Collectors.toList());

        // 使用你 TableDataInfo 类的 build 方法
        return TableDataInfo.build(voList, materialPage.getTotal());
    }

    //查询条件
    private LambdaQueryWrapper<Material> buildQueryWrapper(MaterialQueryDTO queryDTO) {
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(queryDTO.getMaterialCode())) {
            queryWrapper.like(Material::getMaterialCode, queryDTO.getMaterialCode());
        }
        if (StringUtils.isNotBlank(queryDTO.getMaterialName())) {
            queryWrapper.like(Material::getMaterialName, queryDTO.getMaterialName());
        }
        if (StringUtils.isNotBlank(queryDTO.getMaterialType())) {
            queryWrapper.eq(Material::getMaterialType, queryDTO.getMaterialType());
        }
        if (StringUtils.isNotBlank(queryDTO.getStatus())) {
            queryWrapper.eq(Material::getStatus, queryDTO.getStatus());
        }
        if (StringUtils.isNotBlank(queryDTO.getSupplierCode())) {
            queryWrapper.eq(Material::getSupplierCode, queryDTO.getSupplierCode());
        }
        if (StringUtils.isNotBlank(queryDTO.getSpecification())) {
            queryWrapper.like(Material::getSpecification, queryDTO.getSpecification());
        }

        return queryWrapper;
    }






    @Override
    public List<MaterialVO> selectMaterialList(MaterialQueryDTO queryDTO) {
        // 构建查询条件（同上）
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
        // ... 添加查询条件

        List<Material> materials = materialMapper.selectList(queryWrapper);
        return materials.stream().map(material -> {
            MaterialVO vo = new MaterialVO();
            BeanUtils.copyProperties(material, vo);
            return vo;
        }).collect(Collectors.toList());
    }






    @Override
    public Material getByMaterialCode(String materialCode) {
        return materialMapper.selectByMaterialCode(materialCode);
    }






    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertMaterial(Material material) {
        // 检查物料编码唯一性
        if (!checkMaterialCodeUnique(material.getMaterialCode(), null)) {
            throw new RuntimeException("物料编码已存在: " + material.getMaterialCode());
        }

        material.setCreateUser(SecurityUtils.getUsername());
        material.setUpdateUser(SecurityUtils.getUsername());

        return materialMapper.insert(material) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMaterial(Material material) {
        // 检查物料编码唯一性
        if (!checkMaterialCodeUnique(material.getMaterialCode(), material.getId())) {
            throw new RuntimeException("物料编码已存在: " + material.getMaterialCode());
        }

        material.setUpdateUser(SecurityUtils.getUsername());
        material.setUpdateTime(LocalDateTime.now());

        return materialMapper.updateById(material) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMaterialByIds(List<Long> ids) {
        // 检查物料是否被使用
        for (Long id : ids) {
            if (isMaterialInUse(id)) {
                Material material = materialMapper.selectById(id);
                throw new RuntimeException("物料【" + material.getMaterialName() + "】已被使用，无法删除");
            }
        }

        return materialMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean checkMaterialCodeUnique(String materialCode, Long excludeId) {
        int count = materialMapper.countByMaterialCodeExcludeId(materialCode, excludeId);
        return count == 0;
    }

    @Override
    public List<Material> selectByMaterialType(String materialType) {
        return materialMapper.selectByMaterialType(materialType);
    }

    @Override
    public boolean updateMaterialStatus(Long id, String status) {
        Material material = new Material();
        material.setId(id);
        material.setStatus(status);
        material.setUpdateUser(SecurityUtils.getUsername());
        material.setUpdateTime(LocalDateTime.now());
        return materialMapper.updateById(material) > 0;
    }

    /**
     * 检查物料是否被使用（在BOM、库存、交易记录中引用）
     */
    private boolean isMaterialInUse(Long materialId) {
        // TODO: 实现物料使用情况检查
        // 1. 检查BOM明细表中是否引用
        // 2. 检查库存表中是否有库存
        // 3. 检查交易记录表中是否有记录
        // 目前先返回false，实际开发中需要实现这些检查
        return false;
    }
}
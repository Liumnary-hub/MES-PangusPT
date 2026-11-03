package com.mes.controller;

import com.mes.dto.MaterialDTO;
import com.mes.dto.MaterialQueryDTO;
import com.mes.entity.Material;
import com.mes.service.MaterialService;
import com.mes.util.SecurityUtils;
import com.mes.util.TableDataInfo;
import com.mes.vo.MaterialVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物料管理控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/material")
public class MaterialController {

    private final MaterialService materialService;

    /**
     * 分页查询物料列表
     */
    @GetMapping("/list")
    public TableDataInfo<MaterialVO> list(MaterialQueryDTO queryDTO) {
        return materialService.selectMaterialPage(queryDTO);
    }

    /**
     * 获取物料详细信息
     */
    @GetMapping(value = "/{id}")
    public MaterialVO getInfo(@PathVariable Long id) {
        Material material = materialService.getById(id);
        MaterialVO vo = new MaterialVO();
        BeanUtils.copyProperties(material, vo);
        return vo;
    }

    /**
     * 根据物料编码获取物料信息
     */
    @GetMapping("/code/{materialCode}")
    public MaterialVO getByCode(@PathVariable String materialCode) {
        Material material = materialService.getByMaterialCode(materialCode);
        if (material == null) {
            throw new RuntimeException("物料不存在");
        }
        MaterialVO vo = new MaterialVO();
        BeanUtils.copyProperties(material, vo);
        return vo;
    }

    /**
     * 新增物料
     */
    @PostMapping
    public boolean add(@Validated @RequestBody MaterialDTO materialDTO) {
        Material material = new Material();
        BeanUtils.copyProperties(materialDTO, material);
        return materialService.insertMaterial(material);
    }

    /**
     * 修改物料
     */
    @PutMapping
    public boolean edit(@Validated @RequestBody MaterialDTO materialDTO) {
        Material material = new Material();
        BeanUtils.copyProperties(materialDTO, material);
        return materialService.updateMaterial(material);
    }

    /**
     * 删除物料
     */
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable List<Long> ids) {
        return materialService.deleteMaterialByIds(ids);
    }

    /**
     * 检查物料编码是否唯一
     */
    @GetMapping("/checkMaterialCodeUnique")
    public boolean checkMaterialCodeUnique(@RequestParam String materialCode,
                                           @RequestParam(required = false) Long excludeId) {
        return materialService.checkMaterialCodeUnique(materialCode, excludeId);
    }

    /**
     * 更新物料状态
     */
    @PutMapping("/{id}/status/{status}")
    public boolean updateStatus(@PathVariable Long id, @PathVariable String status) {
        return materialService.updateMaterialStatus(id, status);
    }

    /**
     * 根据类型查询物料列表
     */
    @GetMapping("/type/{materialType}")
    public List<MaterialVO> listByType(@PathVariable String materialType) {
        List<Material> materials = materialService.selectByMaterialType(materialType);
        return materials.stream().map(material -> {
            MaterialVO vo = new MaterialVO();
            BeanUtils.copyProperties(material, vo);
            return vo;
        }).collect(java.util.stream.Collectors.toList());
    }
}
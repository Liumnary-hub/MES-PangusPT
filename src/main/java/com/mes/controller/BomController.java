package com.mes.controller;

import com.mes.dto.BomDTO;
import com.mes.dto.BomQueryDTO;
import com.mes.service.BomService;
import com.mes.util.TableDataInfo;
import com.mes.vo.BomVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BOM管理控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bom")
public class BomController {

    private final BomService bomService;

    /**
     * 分页查询BOM列表
     */
    @GetMapping("/list")
    public TableDataInfo<BomVO> list(BomQueryDTO queryDTO) {
        return bomService.selectBomPage(queryDTO);
    }

    /**
     * 查询BOM列表
     */
    @GetMapping("/all")
    public List<BomVO> all(BomQueryDTO queryDTO) {
        return bomService.selectBomList(queryDTO);
    }

    /**
     * 获取BOM详细信息
     */
    @GetMapping("/{id}")
    public BomVO getInfo(@PathVariable Long id) {
        // 这里需要实现根据ID获取BOM详情的方法
        // 暂时返回null，实际开发中需要实现
        return null;
    }

    /**
     * 根据产品ID获取BOM
     */
    @GetMapping("/product/{productId}")
    public BomVO getByProductId(@PathVariable Long productId) {
        return bomService.selectByProductId(productId);
    }

    /**
     * 根据产品编码获取BOM
     */
    @GetMapping("/product/code/{productCode}")
    public BomVO getByProductCode(@PathVariable String productCode) {
        return bomService.selectByProductCode(productCode);
    }

    /**
     * 新增BOM
     */
    @PostMapping
    public boolean add(@Validated @RequestBody BomDTO bomDTO) {
        return bomService.insertBom(bomDTO);
    }

    /**
     * 修改BOM
     */
    @PutMapping
    public boolean edit(@Validated @RequestBody BomDTO bomDTO) {
        return bomService.updateBom(bomDTO);
    }

    /**
     * 删除BOM
     */
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable List<Long> ids) {
        return bomService.deleteBomByIds(ids);
    }

    /**
     * 复制BOM
     */
    @PostMapping("/{sourceBomId}/copy")
    public boolean copy(@PathVariable Long sourceBomId,
                        @RequestParam String newBomCode,
                        @RequestParam String newVersion) {
        return bomService.copyBom(sourceBomId, newBomCode, newVersion);
    }

    /**
     * 计算物料需求
     */
    @GetMapping("/calculateRequirement")
    public List<BomVO> calculateRequirement(@RequestParam Long productId,
                                            @RequestParam Double quantity) {
        return bomService.calculateMaterialRequirement(productId, quantity);
    }

    /**
     * 检查BOM编码是否唯一
     */
    @GetMapping("/checkBomCodeUnique")
    public boolean checkBomCodeUnique(@RequestParam String bomCode,
                                      @RequestParam(required = false) Long excludeId) {
        return bomService.checkBomCodeUnique(bomCode, excludeId);
    }
}
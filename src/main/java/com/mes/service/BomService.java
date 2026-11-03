package com.mes.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.dto.BomDTO;
import com.mes.dto.BomQueryDTO;
import com.mes.entity.Bom;
import com.mes.util.TableDataInfo;
import com.mes.vo.BomVO;

import java.util.List;

/**
 * BOM服务接口
 */
public interface BomService extends IService<Bom> {

    /**
     * 分页查询BOM列表
     */
    TableDataInfo<BomVO> selectBomPage(BomQueryDTO queryDTO);

    /**
     * 查询BOM列表
     */
    List<BomVO> selectBomList(BomQueryDTO queryDTO);

    /**
     * 根据产品ID查询BOM
     */
    BomVO selectByProductId(Long productId);

    /**
     * 根据产品编码查询BOM
     */
    BomVO selectByProductCode(String productCode);

    /**
     * 新增BOM
     */
    boolean insertBom(BomDTO bomDTO);

    /**
     * 修改BOM
     */
    boolean updateBom(BomDTO bomDTO);

    /**
     * 删除BOM
     */
    boolean deleteBomByIds(List<Long> ids);

    /**
     * 复制BOM
     */
    boolean copyBom(Long sourceBomId, String newBomCode, String newVersion);

    /**
     * 计算物料需求
     */
    List<BomVO> calculateMaterialRequirement(Long productId, Double quantity);

    /**
     * 检查BOM编码是否唯一
     */
    boolean checkBomCodeUnique(String bomCode, Long excludeId);
}

package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.dto.MaterialQueryDTO;
import com.mes.entity.Material;
import com.mes.util.TableDataInfo;
import com.mes.vo.MaterialVO;


import java.util.List;

/**
 * 物料主数据服务接口
 */
public interface MaterialService extends IService<Material> {

    /**
     * 分页查询物料列表
     */
    TableDataInfo<MaterialVO> selectMaterialPage(MaterialQueryDTO queryDTO);

    /**
     * 查询物料列表
     */
    List<MaterialVO> selectMaterialList(MaterialQueryDTO queryDTO);

    /**
     * 根据物料编码获取物料
     */
    Material getByMaterialCode(String materialCode);

    /**
     * 新增物料
     */
    boolean insertMaterial(Material material);

    /**
     * 修改物料
     */
    boolean updateMaterial(Material material);

    /**
     * 批量删除物料
     */
    boolean deleteMaterialByIds(List<Long> ids);

    /**
     * 检查物料编码是否唯一
     */
    boolean checkMaterialCodeUnique(String materialCode, Long excludeId);

    /**
     * 根据类型查询物料
     */
    List<Material> selectByMaterialType(String materialType);

    /**
     * 更新物料状态
     */
    boolean updateMaterialStatus(Long id, String status);
}
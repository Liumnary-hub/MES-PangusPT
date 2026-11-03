package com.mes.converter;

import com.mes.dto.MaterialDTO;
import com.mes.entity.Material;
import com.mes.enums.MaterialType;
import com.mes.vo.MaterialVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 物料对象转换器
 */
@Mapper(componentModel = "spring")
public interface MaterialConverter {

    MaterialConverter INSTANCE = Mappers.getMapper(MaterialConverter.class);

    /**
     * DTO 转 Entity
     */
    Material dtoToEntity(MaterialDTO dto);

    /**
     * Entity 转 VO
     */
    MaterialVO entityToVo(Material entity);

    /**
     * Entity 列表转 VO 列表
     */
    List<MaterialVO> entityListToVoList(List<Material> entityList);

    /**
     * 设置物料类型描述
     */
    default String getMaterialTypeDesc(String materialType) {
        if (materialType == null) {
            return "";
        }
        try {
            return MaterialType.valueOf(materialType).getDescription();
        } catch (IllegalArgumentException e) {
            return materialType;
        }
    }

    /**
     * 设置状态描述
     */
    default String getStatusDesc(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "active":
                return "启用";
            case "inactive":
                return "停用";
            default:
                return status;
        }
    }
}
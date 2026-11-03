package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductDefinitionMapper extends BaseMapper<ProductDefinition> {

    @Select("<script>" +
            "SELECT * FROM mes_product_definition WHERE is_deleted = 0 " +
            "<if test='productCode != null and productCode != \"\"'>" +
            "   AND product_code LIKE CONCAT('%', #{productCode}, '%')" +
            "</if>" +
            "<if test='productName != null and productName != \"\"'>" +
            "   AND product_name LIKE CONCAT('%', #{productName}, '%')" +
            "</if>" +
            "<if test='productType != null and productType != \"\"'>" +
            "   AND product_type = #{productType}" +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "   AND status = #{status}" +
            "</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    IPage<ProductDefinition> selectByCondition(Page<ProductDefinition> page,
                                               @Param("productCode") String productCode,
                                               @Param("productName") String productName,
                                               @Param("productType") String productType,
                                               @Param("status") String status);

    @Select("SELECT COUNT(*) FROM mes_product_definition WHERE product_code = #{productCode} AND is_deleted = 0")
    int countByProductCode(@Param("productCode") String productCode);

}

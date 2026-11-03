package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductionResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface ProductionResourceMapper extends BaseMapper<ProductionResource> {
    @Select("<script>" +
            "SELECT * FROM mes_production_resource WHERE is_deleted = 0 " +
            "<if test='resourceCode != null and resourceCode != \"\"'>" +
            "   AND resource_code LIKE CONCAT('%', #{resourceCode}, '%')" +
            "</if>" +
            "<if test='resourceName != null and resourceName != \"\"'>" +
            "   AND resource_name LIKE CONCAT('%', #{resourceName}, '%')" +
            "</if>" +
            "<if test='resourceType != null and resourceType != \"\"'>" +
            "   AND resource_type = #{resourceType}" +
            "</if>" +
            "<if test='department != null and department != \"\"'>" +
            "   AND department LIKE CONCAT('%', #{department}, '%')" +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "   AND status = #{status}" +
            "</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    IPage<ProductionResource> selectByCondition(Page<ProductionResource> page,
                                                @Param("resourceCode") String resourceCode,
                                                @Param("resourceName") String resourceName,
                                                @Param("resourceType") String resourceType,
                                                @Param("department") String department,
                                                @Param("status") String status);

    @Select("SELECT COUNT(*) FROM mes_production_resource WHERE resource_code = #{resourceCode} AND is_deleted = 0")
    int countByResourceCode(@Param("resourceCode") String resourceCode);
}

package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.WorkShift;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WorkShiftMapper extends BaseMapper<WorkShift> {

    /**
     * 分页查询班次定义 - 使用注解
     */
    @Select("<script>" +
            "SELECT * FROM mes_work_shift WHERE is_deleted = 0 " +
            "<if test='shiftCode != null and shiftCode != \"\"'>" +
            "AND shift_code LIKE CONCAT('%', #{shiftCode}, '%') " +
            "</if>" +
            "<if test='shiftName != null and shiftName != \"\"'>" +
            "AND shift_name LIKE CONCAT('%', #{shiftName}, '%') " +
            "</if>" +
            "<if test='isActive != null'>" +
            "AND is_active = #{isActive} " +
            "</if>" +
            "ORDER BY create_time DESC" +
            "</script>")
    IPage<WorkShift> selectShiftPage(Page<WorkShift> page,
                                     @Param("shiftCode") String shiftCode,
                                     @Param("shiftName") String shiftName,
                                     @Param("isActive") Boolean isActive);

    /**
     * 查询所有启用的班次 - 使用注解
     */
    @Select("SELECT * FROM mes_work_shift WHERE is_deleted = 0 AND is_active = 1 ORDER BY start_time ASC")
    List<WorkShift> selectActiveShifts();

    /**
     * 根据班次编码查询 - 使用注解
     */
    @Select("SELECT * FROM mes_work_shift WHERE is_deleted = 0 AND shift_code = #{shiftCode}")
    WorkShift selectByShiftCode(@Param("shiftCode") String shiftCode);

    /**
     * 检查班次编码是否存在 - 使用注解
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM mes_work_shift WHERE is_deleted = 0 AND shift_code = #{shiftCode} " +
            "<if test='excludeId != null'>" +
            "AND id != #{excludeId}" +
            "</if>" +
            "</script>")
    int countByShiftCode(@Param("shiftCode") String shiftCode,
                         @Param("excludeId") Long excludeId);

    /**
     * 更新班次启用状态 - 使用注解
     */
    @Update("UPDATE mes_work_shift SET is_active = #{isActive}, update_time = NOW() WHERE id = #{id}")
    int updateActiveStatus(@Param("id") Long id, @Param("isActive") Boolean isActive);

    /**
     * 获取默认班次（第一个启用的班次） - 使用注解
     */
    @Select("SELECT * FROM mes_work_shift WHERE is_deleted = 0 AND is_active = 1 ORDER BY start_time ASC LIMIT 1")
    WorkShift selectDefaultShift();
}
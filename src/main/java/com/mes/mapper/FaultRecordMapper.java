package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.mes.dto.FaultQueryDTO;
import com.mes.entity.FaultRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface FaultRecordMapper extends BaseMapper<FaultRecord> {

    /**
     * 根据设备ID查询故障记录
     */
    List<FaultRecord> selectByEquipmentId(@Param("equipmentId") Long equipmentId);

    /**
     * 根据故障单号查询
     */
    FaultRecord selectByFaultNo(@Param("faultNo") String faultNo);

    /**
     * 根据故障状态查询
     */
    List<FaultRecord> selectByFaultStatus(@Param("faultStatus") Integer faultStatus);

    /**
     * 根据故障等级查询
     */
    List<FaultRecord> selectByFaultLevel(@Param("faultLevel") Integer faultLevel);

    /**
     * 查询未解决的故障
     */
    List<FaultRecord> selectUnresolvedFaults();

    /**
     * 查询超期未处理的故障
     */
    List<FaultRecord> selectOverdueFaults();

    /**
     * 条件分页查询故障记录
     */
    Page<FaultRecord> selectFaultPage(Page<FaultRecord> page, @Param("query") FaultQueryDTO query);

    /**
     * 统计各类型故障数量
     */
    List<Map<String, Object>> countByFaultType();

    /**
     * 统计各等级故障数量
     */
    List<Map<String, Object>> countByFaultLevel();

    /**
     * 统计各状态故障数量
     */
    List<Map<String, Object>> countByFaultStatus();

    /**
     * 统计设备故障次数
     */
    List<Map<String, Object>> countFaultByEquipment();

    /**
     * 统计故障损失（按月）
     */
    List<Map<String, Object>> sumLossByMonth(@Param("year") Integer year);

    /**
     * 更新故障状态
     */
    int updateFaultStatus(@Param("faultId") Long faultId,
                          @Param("faultStatus") Integer faultStatus,
                          @Param("updateTime") LocalDateTime updateTime);

    /**
     * 开始处理故障
     */
    int startTreatment(@Param("faultId") Long faultId,
                       @Param("handler") String handler,
                       @Param("handlerId") String handlerId,
                       @Param("treatmentStartTime") LocalDateTime treatmentStartTime);

    /**
     * 完成故障处理
     */
    int completeTreatment(@Param("faultId") Long faultId,
                          @Param("treatmentEndTime") LocalDateTime treatmentEndTime,
                          @Param("treatmentMeasures") String treatmentMeasures,
                          @Param("rootCause") String rootCause,
                          @Param("preventiveMeasures") String preventiveMeasures);

    /**
     * 关闭故障记录
     */
    int closeFault(@Param("faultId") Long faultId,
                   @Param("closer") String closer);

    /**
     * 查询重复故障
     */
    List<FaultRecord> selectRecurringFaults(@Param("equipmentId") Long equipmentId);

    /**
     * 查询故障详情（包含设备信息）
     */
    FaultRecord selectDetailById(@Param("faultId") Long faultId);
}
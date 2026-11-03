package com.mes.vo;

import com.mes.dto.MaterialImportDTO;
import lombok.Data;

import java.util.List;

@Data
public class MaterialImportResultVO {

    /** 总记录数 */
    private Integer totalCount;

    /** 成功数量 */
    private Integer successCount;

    /** 失败数量 */
    private Integer failureCount;

    /** 失败详情 */
    private List<MaterialImportDTO> failureList;
}
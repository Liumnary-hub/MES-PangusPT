package com.mes.vo;

import lombok.Data;
import java.util.List;

/**
 * 物料分页响应结果
 */
@Data
public class MaterialPageVO {

    /** 总记录数 */
    private Long total;

    /** 当前页 */
    private Integer pageNum;

    /** 页大小 */
    private Integer pageSize;

    /** 数据列表 */
    private List<MaterialVO> list;
}
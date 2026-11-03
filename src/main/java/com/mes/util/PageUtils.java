package com.mes.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 分页工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtils {

    /**
     * 将PageHelper的分页信息转换成TableDataInfo
     */
    public static <T> TableDataInfo<T> build(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(page.getRecords());  // 数据列表
        rspData.setTotal(page.getTotal());   // 总记录数
        return rspData;
    }

    /**
     * 将MyBatis Plus的分页信息转换成TableDataInfo
     */
    public static <T> TableDataInfo<T> build(IPage<T> page) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(page.getRecords());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    /**
     * 将列表数据转换成TableDataInfo
     */
    public static <T> TableDataInfo<T> build(List<T> list) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 将列表数据和总数转换成TableDataInfo
     */
    public static <T> TableDataInfo<T> build(List<T> list, long total) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(200);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(total);
        return rspData;
    }
}
package com.mes.common;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Integer pages;
    private List<T> list;

    public static <T> PageResult<T> of(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setPages(pageSize > 0 ? (int) Math.ceil((double) total / pageSize) : 0);
        return result;
    }

    public static <T> PageResult<T> empty() {
        return of(List.of(), 0L, 1, 20);
    }
}
package com.mes.util;

import lombok.Data;
import java.io.Serializable;

/**
 * 分页查询基类
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderByColumn;

    /**
     * 排序方向 "desc" 或者 "asc"
     */
    private String isAsc = "desc";

    /**
     * 获取排序语句
     */
    public String getOrderBy() {
        if (orderByColumn == null || orderByColumn.trim().isEmpty()) {
            return "";
        }

        String orderBy = "";
        if (orderByColumn.contains(",")) {
            String[] columns = orderByColumn.split(",");
            String[] orders = isAsc.split(",");

            for (int i = 0; i < columns.length; i++) {
                String column = columns[i].trim();
                String order = (i < orders.length) ? orders[i].trim() : "desc";
                if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
                    order = "desc";
                }
                orderBy += column + " " + order + ",";
            }
            orderBy = orderBy.substring(0, orderBy.length() - 1);
        } else {
            orderBy = orderByColumn + " " + ("asc".equals(isAsc) ? "asc" : "desc");
        }

        return orderBy;
    }
}
package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础查询参数DTO
 * 所有查询DTO的基类
 */
@Data
@Schema(description = "基础查询参数")
public class BaseQueryDTO {

    @Schema(description = "当前页码，从1开始", defaultValue = "1", example = "1")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @Schema(description = "每页显示数量", defaultValue = "20", example = "20")
    @Min(value = 1, message = "每页数量最小为1")
    @Max(value = 500, message = "每页数量最大为500")
    private Integer pageSize = 20;

    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "排序方向: ASC-升序, DESC-降序", allowableValues = {"ASC", "DESC"}, defaultValue = "DESC")
    private String orderDirection = "DESC";

    @Schema(description = "是否统计总数", defaultValue = "true")
    private Boolean countTotal = true;

    @Schema(description = "关键字搜索")
    private String keyword;

    @Schema(description = "状态过滤")
    private String status;

    @Schema(description = "创建时间开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    @Schema(description = "是否启用")
    private Boolean enabled;

    /**
     * 获取排序SQL片段
     */
    public String getOrderByClause() {
        if (orderBy == null || orderBy.trim().isEmpty()) {
            return null;
        }

        String direction = "DESC".equalsIgnoreCase(orderDirection) ? "DESC" : "ASC";
        return orderBy + " " + direction;
    }

    /**
     * 获取分页偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }

    /**
     * 验证分页参数
     */
    public void validate() {
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1) {
            pageSize = 20;
        }
        if (pageSize > 500) {
            pageSize = 500;
        }
    }

    /**
     * 创建新的查询实例
     */
    public static BaseQueryDTO of(Integer pageNum, Integer pageSize) {
        BaseQueryDTO query = new BaseQueryDTO();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        return query;
    }

    /**
     * 创建关键字查询
     */
    public static BaseQueryDTO keywordSearch(String keyword) {
        BaseQueryDTO query = new BaseQueryDTO();
        query.setKeyword(keyword);
        return query;
    }
}
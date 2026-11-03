package com.mes.util;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 */
@Data
public class TableDataInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 构造方法
     */
    public TableDataInfo() {
    }

    /**
     * 构造方法
     *
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, long total) {
        this.rows = list;
        this.total = total;
        this.code = 200;
        this.msg = "查询成功";
    }

    /**
     * 构造方法
     *
     * @param list 列表数据
     * @param total 总记录数
     * @param code 状态码
     * @param msg 消息
     */
    public TableDataInfo(List<T> list, long total, int code, String msg) {
        this.rows = list;
        this.total = total;
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构建成功响应
     */
    public static <T> TableDataInfo<T> build(List<T> list) {
        return new TableDataInfo<>(list, list != null ? list.size() : 0);
    }

    /**
     * 构建成功响应
     */
    public static <T> TableDataInfo<T> build(List<T> list, long total) {
        return new TableDataInfo<>(list, total);
    }

    /**
     * 构建空数据响应
     */
    public static <T> TableDataInfo<T> empty() {
        return new TableDataInfo<>(null, 0);
    }

    /**
     * 构建失败响应
     */
    public static <T> TableDataInfo<T> error(String msg) {
        return error(500, msg);
    }

    /**
     * 构建失败响应
     */
    public static <T> TableDataInfo<T> error(int code, String msg) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(code);
        rspData.setMsg(msg);
        return rspData;
    }

    /**
     * 删除这个空的 builder() 方法，或者实现它
     * 如果你想要 Builder 模式，可以添加 Lombok 的 @Builder 注解
     * 或者手动实现 Builder 模式
     */


    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return code == 200;
    }
}
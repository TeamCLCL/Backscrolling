package com.model;


/**
 * 分页实体类
 * @param <T>
 */
public class Page<T> {
    // 页码
    private Integer pageNo;
    // 每页显示记录数
    private Integer pageSize;
    // 总记录数
    private Integer totalSize;
    // 总页数 (由每页显示记录数和总记录数决定)
    private Integer pageCount;

    public Page() {
    }


}

package com.model;

import java.util.List;

/**
 * 分页实体类
 * @param <T>
 */
public class Page<T> {
    // 页码
    private Integer pageno;
    // 每页显示记录数
    private Integer pagesize = 3;
    // 总记录数
    private Long totalsize;
    // 数据集合
    private List<T> dataList;

    public Page() {
    }

    public Page(Integer pageno) {
        this.pageno = pageno;
    }

    public Page(Integer pageno, Long totalsize, List<T> dataList) {
        this.pageno = pageno;
        this.totalsize = totalsize;
        this.dataList = dataList;
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public Long getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(Long totalsize) {
        this.totalsize = totalsize;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageno=" + pageno +
                ", pagesize=" + pagesize +
                ", totalsize=" + totalsize +
                ", dataList=" + dataList +
                '}';
    }
}

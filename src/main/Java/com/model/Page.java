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
    private Integer pagesize = 10;
    // 总记录数
    private Integer totalsize;
    // 数据集合
    private List<T> dataList;

    public Page() {
    }

    /**
     * 用于加载主页面时展示
     * 分页查询传入参数时使用
     * @param pageno
     */
    public Page(Integer pageno) {
        this.pageno = pageno;
    }

    /**
     * 用于用户收藏列表展示
     * 用于获取总资源数（普通、关键字、资源类别）
     * @param pageno
     * @param pagesize
     */
    public Page(Integer pageno, Integer pagesize) {
        this.pageno = pageno;
        this.pagesize = pagesize;
    }

    /**
     * 用于返回给前端进行主页面展示
     * @param pageno
     * @param totalsize
     * @param dataList
     */
    public Page(Integer pageno, Integer totalsize, List<T> dataList) {
        this.pageno = pageno;
        this.totalsize = totalsize;
        this.dataList = dataList;
    }

    /**
     * 用于返回给前端进行用户收藏列表展示
     * @param pageno
     * @param pagesize
     * @param totalsize
     * @param dataList
     */
    public Page(Integer pageno, Integer pagesize, Integer totalsize, List<T> dataList) {
        this.pageno = pageno;
        this.pagesize = pagesize;
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

    public Integer getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(Integer totalsize) {
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

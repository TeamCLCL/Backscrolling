package com.model;

/**
 *  资源类
 */
public class Resource {
    // 标识
    private Integer id;
    // 标题
    private String title;
    // 链接
    private String link;
    // 收藏量
    private Integer collect;

    public Resource() {
    }

    public Resource(Integer id, String title, String link, Integer collect) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.collect = collect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getCollect() {
        return collect;
    }

    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", collect=" + collect +
                '}';
    }
}

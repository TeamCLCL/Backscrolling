package com.model;

/**
 *  标准资源类
 *      查询条件很多时候和实体类并不相同
 *      所以要做成一个单独的类 —— CriteriaResource类
 *      其中里面的getter方法做了修改，是为了正确的填充占位符
 */
public class CriteriaResource {
    // 标题
    private String title;

    public CriteriaResource(){
    }

    public CriteriaResource(String title) {
        this.title = title;
    }

    public String getTitle() {
        if(this.title == null){
            this.title = "%%";
        }else{
            this.title = "%" + this.title + "%";
        }
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

package com.dao.impl;

import com.model.*;
import com.dao.Dao;
import com.dao.ResourceDao;
import com.utils.JsonUtil;

import java.util.List;

public class ResourceDaoImpl extends Dao<Resource> implements ResourceDao {
    public static void main(String[] args) {
        List<Resource> list1 = new ResourceDaoImpl().getAllResource(new Page(1));
        for(Resource resource:list1){
            System.out.println(JsonUtil.toJson(resource));
        }

        /*List<Resource> list2 = new ResourceDaoImpl().getResourceByKeyword(new CriteriaResource("百"), 0, 10);
        for(Resource resource:list2){
            System.out.println(JsonUtil.toJson(resource));
        }

        new ResourceDaoImpl().delete(2);
        List<Resource> list3 = new ResourceDaoImpl().getResourceByKeyword(new CriteriaResource("百"), 0, 10);
        for(Resource resource:list3){
            System.out.println(JsonUtil.toJson(resource));
        }*/
    }

    /**
     * 获取所有资源（分页查询）
     * @return
     */
    @Override
    public List<Resource> getAllResource(Page page) {
        String sql = "select * from t_resource order by collect limit ?,?";
        return getForBeanList(sql, (page.getPageno()-1)*page.getPagesize(), page.getPagesize());
    }

    /**
     * 根据标题关键字模糊查询（分页查询）
     * @param cr 封装了查询条件
     * @return
     */
    @Override
    public List<Resource> getResourceByKeyword(CriteriaResource cr, Page page) {
        String sql = "select * from t_resource where title like ? order by collect limit ?,?";
        // 修改了CriteriaResource的getter方法，使其返回的字符串中有"%%",
        // 若参数为空则返回"%%"，否则返回 "%" + 字段本身的值 + "%"
        return getForBeanList(sql, cr.getTitle(), (page.getPageno()-1)*page.getPagesize(), page.getPagesize());
    }

    /**
     * 根据资源类别获取资源（分页查询）
     * @param type
     * @return
     */
    @Override
    public List<Resource> getResourceByType(Type type, Page page) {
        String sql = "select tr.id,tr.title,tr.link,tr.collect from t_resource tr"
                        + " join t_resource_type trt on tr.id = trt.resource_id"
                        + " where trt.type_id in (select id from t_type where type = ?) order by collect limit ?,?";
        return getForBeanList(sql, type.getType(), (page.getPageno()-1)*page.getPagesize(), page.getPagesize());
    }

    /**
     * 查询用户所收藏的资源（分页查询）
     * @return
     */
    @Override
    public List<Resource> getUserCollects(User user, Page page) {
        String sql = "select * from t_resource where resource_id ="
                        + " (select resource_id from t_user_collect where user_id = ?)"
                        + " order by collect limit ?,?";
        return getForBeanList(sql, user.getId(), (page.getPageno()-1)*page.getPagesize(), page.getPagesize());
    }

    /**
     * 管理员添加资源
     * @param resource
     */
    @Override
    public void insert(Resource resource) {
        String sql = "insert into t_resource(title,link) values(?,?)";
        update(sql, resource.getTitle(), resource.getLink());
    }

    /**
     * 管理员删除资源
     * @param id
     */
    @Override
    public void delete(Integer id) {
        String sql = "delete from t_resource where id = ?";
        update(sql, id);
    }

    /**
     * 管理员修改资源信息
     * @param id
     */
    @Override
    public void update(Integer id) {
        String sql = "";
        update(sql);
    }
}

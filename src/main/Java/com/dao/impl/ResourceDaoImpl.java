package com.dao.impl;

import com.model.CriteriaResource;
import com.dao.Dao;
import com.dao.ResourceDao;
import com.model.Resource;
import com.utils.JsonUtil;

import java.util.List;

public class ResourceDaoImpl extends Dao<Resource> implements ResourceDao {
    public static void main(String[] args) {
        List<Resource> list1 = new ResourceDaoImpl().getAllResource();
        for(Resource resource:list1){
            System.out.println(JsonUtil.toJson(resource));
        }

        List<Resource> list2 = new ResourceDaoImpl().getResourceByKeyword(new CriteriaResource("百"));
        for(Resource resource:list2){
            System.out.println(JsonUtil.toJson(resource));
        }

        new ResourceDaoImpl().delete(2);
        List<Resource> list3 = new ResourceDaoImpl().getResourceByKeyword(new CriteriaResource("百"));
        for(Resource resource:list3){
            System.out.println(JsonUtil.toJson(resource));
        }
    }

    /**
     * 获取所有资源
     * @return
     */
    @Override
    public List<Resource> getAllResource() {
        String sql = "select id,title,link,collect from t_resource order by collect";
        return getForBeanList(sql);
    }

    /**
     * 根据关键字获取资源
     * @param cr 封装了查询条件
     * @return
     */
    @Override
    public List<Resource> getResourceByKeyword(CriteriaResource cr) {
        String sql = "select id,title,link,collect from t_resource where title like ? order by collect";
        // 修改了CriteriaResource的getter方法，使其返回的字符串中有"%%",
        // 若参数为空则返回"%%"，否则返回 "%" + 字段本身的值 + "%"
        return getForBeanList(sql, cr.getTitle());
    }

    /**
     * 根据资源类别获取资源
     * @param type
     * @return
     */
    @Override
    public List<Resource> getResourceByType(String type) {
        String sql = "select tr.id,tr.title,tr.link,tr.collect from t_resource tr"
                        + " join t_resource_type trt on tr.id = trt.resource_id"
                        + " where trt.type_id in (select id from t_type where type = ?) order by collect";
        return getForBeanList(sql, type);
    }

    /**
     * 查询用户所收藏的资源
     * @param user_id
     * @param resource_id
     * @return
     */
    @Override
    public List<Resource> getUserCollects(Integer user_id, Integer resource_id) {
        String sql = "select * from t_resource where resource_id = (select resource_id from t_user_collect where user_id = ? and resource_id = ?)";
        return getForBeanList(sql, user_id, resource_id);
    }

    /**
     * @param resource
     */
    @Override
    public void insert(Resource resource) {
        String sql = "insert into t_resource(title,link) values(?,?)";
        update(sql, resource.getTitle(), resource.getLink());
    }

    /**
     * @param id
     */
    @Override
    public void delete(Integer id) {
        String sql = "delete from t_resource where id = ?";
        update(sql, id);
    }

    /**
     * @param id
     */
    @Override
    public void update(Integer id) {
        String sql = "";
        update(sql);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public long getCountById(Integer id) {
        return 0;
    }
}

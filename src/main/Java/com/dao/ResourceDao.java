package com.dao;

import com.model.*;

import java.util.List;

/**
 *  资源模块，dao接口设计
 */
public interface ResourceDao {
    /**
     * 查询所有资源（分页查询）
     * @return
     */
    List<Resource> getAllResource(Page page);

    /**
     * 根据标题关键字模糊查询（分页查询）
     * @param cr  封装了查询条件
     * @return
     */
    List<Resource> getResourceByKeyword(CriteriaResource cr, Page page);

    /**
     * 按资源类别查询（分页查询）
     * @param type
     * @return
     */
    List<Resource> getResourceByType(Type type, Page page);

    /**
     * 获取用户所收藏的资源（分页查询）
     * @param user
     * @param page
     * @return
     */
    List<Resource> getUserCollects(User user, Page page);

    /**
     * 管理员添加资源
     * @param resource
     */
    void insert(Resource resource);

    /**
     * 管理员删除资源
     * @param id
     */
    void delete(Integer id);

    /**
     * 管理员修改资源信息
     * @param id
     * @return
     */
    void update(Integer id);

    /**
     * 获取不同检索类型的总资源数
     * @param selectBy
     * @param type
     * @return
     */
    Long getNum(String selectBy, String type);
}

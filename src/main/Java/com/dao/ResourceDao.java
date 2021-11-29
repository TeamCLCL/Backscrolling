package com.dao;

import com.model.CriteriaResource;
import com.model.Resource;

import java.util.List;

/**
 *  资源模块，dao接口设计
 */
public interface ResourceDao {
    /**
     * 查询所有资源（分页查询）
     * @return
     */
    List<Resource> getAllResource(Integer pageno, Integer pagesize);

    /**
     * 根据标题关键字模糊查询
     * @param cr  封装了查询条件
     * @return
     */
    List<Resource> getResourceByKeyword(CriteriaResource cr);

    /**
     * 按资源类别查询
     * @param type
     * @return
     */
    List<Resource> getResourceByType(String type);

    /**
     * 获取用户收藏资源
     * @param user_id
     * @param resource_id
     * @return
     */
    List<Resource> getUserCollects(Integer user_id, Integer resource_id);

    /**
     * 添加资源
     * @param resource
     */
    void insert(Resource resource);

    /**
     * 删除资源
     * @param id
     */
    void delete(Integer id);

    /**
     * 修改资源信息
     *      更新时候的查询（根据id）显示
     * @param id
     * @return
     */
    void update(Integer id);

    /**
     * 获取总资源数
     * @return
     */
    long getNum();
}

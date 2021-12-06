package com.dao;

import com.model.User;

import java.util.List;

/**
 *  用户接口
 */
public interface UserDao {
    /**
     * 用户名唯一性验证
     * @return
     */
    long nameCheck(String name);

    /**
     * 邮箱唯一性验证
     * @return
     */
    long emailCheck(String email);

    /**
     * 用户注册
     * @param user
     */
    int logon(User user);

    /**
     * 用户登录验证
     * @param user
     * @param accountType
     * @return
     */
    User loginCheck(User user, String accountType);

    /**
     * 用户重置密码
     * @param user
     * @return
     */
    int resetPassword(User user);

    /**
     * 获取id对应的用户对象
     * @param user_id
     * @return
     */
    User getUser(Integer user_id);

    /**
     * 管理员查询所有用户
     * @return
     */
    List<User> getAllUser();

    /**
     * 用户收藏资源
     * @param user_id
     * @param resource_id
     * @return
     */
    boolean collect(Integer user_id, Integer resource_id);

    /**
     * 用户移除所收藏的资源
     * @param user_id
     * @param resource_id
     * @return
     */
    boolean removeCollect(Integer user_id, Integer resource_id);

    /**
     * 用户修改个人信息
     * @param user
     * @return
     */
    int updateMessage(User user, String ...ages);
}

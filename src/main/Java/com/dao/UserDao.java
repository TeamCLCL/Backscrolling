package com.dao;

import com.model.User;

import java.util.List;

/**
 *  用户接口
 */
public interface UserDao {
    /**
     * 查询所有用户
     * @return
     */
    List<User> getAllUser();

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
     * 修改用户
     * @param user
     * @return
     */
    int resetPassword(User user);

    /**
     * 用户修改个人信息
     * @param user
     * @return
     */
    int updateMessage(User user);

    /**
     * 收藏资源
     * @param user_id
     * @param resource_id
     * @return
     */
    boolean collectResource(Integer user_id, Integer resource_id);

    /**
     * 移除所收藏的资源
     * @param user_id
     * @param resource_id
     * @return
     */
    boolean notCollectResource(Integer user_id, Integer resource_id);
}

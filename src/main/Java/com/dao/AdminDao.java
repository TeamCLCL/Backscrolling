package com.dao;

import com.model.Admin;

/**
 *  管理员接口
 */
public interface AdminDao {
    /**
     * 用户登录验证
     * @param admin
     * @return
     */
    Admin loginCheck(Admin admin);
}

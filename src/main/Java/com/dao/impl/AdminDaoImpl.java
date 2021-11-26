package com.dao.impl;

import com.dao.AdminDao;
import com.dao.Dao;
import com.model.Admin;

/**
 *  管理员接口实现类
 */
public class AdminDaoImpl extends Dao<Admin> implements AdminDao {

    /**
     * 管理员登录验证
     * @param admin
     * @return
     */
    @Override
    public Admin loginCheck(Admin admin) {
        String sql = "select * from t_admin where name = ? and password = ?";
        return getForObj(sql, admin.getName(), admin.getPassword());
    }
}

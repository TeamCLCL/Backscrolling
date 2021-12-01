package com.dao.impl;

import com.dao.Dao;
import com.dao.TypeDao;
import com.model.Type;

import java.util.List;

/**
 * 资源类别接口实现类
 */
public class TypeDaoImpl extends Dao<Type> implements TypeDao {

    /**
     * 获取所有资源类别
     * @return
     */
    @Override
    public List<Type> getAllType() {
        String sql = "select * from t_type";
        return getForBeanList(sql);
    }
}

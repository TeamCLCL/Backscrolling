package com.dao;

import com.model.Type;

import java.util.List;

/**
 * 资源类别接口
 */
public interface TypeDao {

    /**
     * 获取所有资源类别
     * @return
     */
    List<Type> getAllType();
}

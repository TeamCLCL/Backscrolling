package com.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.utils.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 *  泛型类
 *      封装了基本的增删改查的方法，以供子类继承使用
 *      当前DAO直接在方法中获取数据库连接
 *      整个DAO采取DBUtils解决方案
 * @param <T> 当前DAO处理的实体类的类型是什么
 */
public class Dao<T> {
    // 创建核心类，没有提供数据源dataSource，在进行具体操作时，需要手动提供Connection
    private QueryRunner queryRunner = new QueryRunner();
    // 获得超类的泛型参数的实际类型
    private Class<T> clazz;

    /**
     * 需要确定clazz，获得超类的泛型参数的实际类型
     */
    public Dao(){
        // getClass()得到子类Class，再getGenericSuperclass()得到带有泛型的父类
        Type superClass = getClass().getGenericSuperclass();
        // 先判断是不是那个类型参数化类型(泛型)
        if(superClass instanceof ParameterizedType){
            // 强转为参数化类型
            ParameterizedType parameterizedType = (ParameterizedType)superClass;
            // 获取参数化类型的数组，泛型可能有多个
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            if(typeArgs != null && typeArgs.length > 0){
                if(typeArgs[0] instanceof Class){
                    clazz = (Class<T>)typeArgs[0];
                }
            }
        }
    }

    /**
     * 查询 —— 返回一个T的实体类对象
     * @param sql
     * @param args
     * @return
     */
    public T getForObj(String sql, Object...args){
        Connection connection = null;
        try {
            // 获取空闲的数据库连接对象
            connection = JdbcUtil.getConnection();
            // 进行查询操作
            return queryRunner.query(connection, sql, new BeanHandler<>(clazz), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 将数据库连接对象放回数据库连接池
            JdbcUtil.releaseConnection(connection);
        }
        return null;
    }

    /**
     * 查询 —— 返回某一个字段的值
     * @param sql
     * @param args
     * @return
     * @param <E>
     */
    public <E> E getForValue(String sql, Object...args){
        Connection connection = null;
        try {
            // 获取空闲的数据库连接对象
            connection = JdbcUtil.getConnection();
            // 执行查询操作
            return (E) queryRunner.query(connection, sql, new ScalarHandler(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 将数据库连接对象放回数据库连接池
            JdbcUtil.releaseConnection(connection);
        }
        return null;
    }

    /**
     * 查询 —— 返回多个T所组成的List
     * @param sql
     * @param args
     * @return
     */
    public List<T> getForBeanList(String sql, Object...args){
        Connection connection = null;
        try {
            // 获取空闲的数据库连接对象
            connection = JdbcUtil.getConnection();
            // 执行查询操作
            return queryRunner.query(connection, sql, new BeanListHandler<>(clazz), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 将数据库连接对象放回数据库连接池
            JdbcUtil.releaseConnection(connection);
        }
        return null;
    }

    /**
     * 通用的insert、delete、update方法（不涉及事务）
     * @param sql
     * @param args
     * @return 更新的行数
     */
    public int update(String sql, Object...args){
        Connection connection = null;
        try {
            // 获取空闲的数据库连接对象
            connection = JdbcUtil.getConnection();
            // 执行增、删、改操作
            return queryRunner.update(connection, sql, args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 将数据库连接对象放回数据库连接池
            JdbcUtil.releaseConnection(connection);
        }
        return 0;
    }

    /**
     * 特殊的包含事务的insert、delete、update方法
     * （处理用户收藏资源与取消资源收藏）
     * @param sql
     * @param args
     * @return 更新的行数
     */
    public boolean update(String[] sql, Object...args){
        Connection connection = null;
        try {
            // 获取空闲的数据库连接对象
            connection = JdbcUtil.getConnection();
            // 数据库自动提交关闭
            connection.setAutoCommit(false);
            // 执行增、删、改操作
            queryRunner.update(connection, sql[0], args);
            queryRunner.update(connection, sql[1], args[1]);
            // 手动提交
            connection.commit();
            return true;
        } catch (Exception e) {
            // 回滚
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // 将数据库连接对象放回数据库连接池
            JdbcUtil.releaseConnection(connection);
        }
        return false;
    }
}
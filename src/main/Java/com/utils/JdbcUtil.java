package com.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * JDBC操作的工具类
 */
public class JdbcUtil {
    // 初始化连接池
    private static DataSource dataSource;

    static{
        // 传入: c3p0-config.xml文件中<named-config name="gyw">
        dataSource = new ComboPooledDataSource("gyw");
    }

    private JdbcUtil() {
    }

    /**
     * 获取数据库连接对象（从数据库连接池中获取空闲的数据库连接对象）
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

    /**
     * 释放数据库连接对象（其实是将数据库连接对象放会到数据库连接池中）
     * @param connection
     */
    public static void releaseConnection(Connection connection){
        try {
            if(connection != null){
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
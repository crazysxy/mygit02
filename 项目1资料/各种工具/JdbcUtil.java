package com.stedu.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC工具类 -- 使用连接池
 *
 * 每个线程拥有自己独立的进行事务处理的连接
 */
public class JdbcUtil {
    private static DataSource dataSource;
    //private static Connection connection; //进行事务处理的连接，开启事务之后就有值了
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    static  {
        try {
            //解析配置文件
            Properties prop = new Properties();
            //加载配置文件
            prop.load(JdbcUtil.class.getResourceAsStream("/jdbc.properties"));

            //创建连接池
            dataSource = DruidDataSourceFactory.createDataSource(prop);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    //获取连接
    public static Connection getConnection() throws SQLException {
        Connection connection = tl.get();
        if(connection != null) {
            return connection;
        }
        //通过连接池获取连接
        connection = dataSource.getConnection();
        return connection;
    }

    //开启事务
    public static void beginTransaction() throws Exception {
        Connection connection = tl.get();
        if(connection != null) {
            throw new Exception("已经处于事务中，不能重复开启事务");
        }

        connection = getConnection();
        //开启事务
        connection.setAutoCommit(false);
        //将连接保存到ThreadLocal中
        tl.set(connection);
    }

    //提交
    public static void commitTransaction() throws Exception {
        Connection connection = tl.get();
        if(connection == null) {
            throw new Exception("没有开启事务，无法提交");
        }
        //提交
        connection.commit();
        //将连接从ThreadLocal中删除
        tl.remove();
    }

    //回滚
    public static void rollbackTransaction() throws Exception {
        Connection connection = tl.get();
        if(connection == null) {
            throw new Exception("没有开启事务，无法回滚");
        }
        //回滚
        connection.rollback();
        //将连接从ThreadLocal中删除
        tl.remove();
    }

    //关闭
    public static void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rset) throws SQLException {
        if(rset != null) {
            rset.close();
        }

        if(pstmt != null) {
            pstmt.close();
        }

        if(conn != null) {
            conn.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(JdbcUtil.getConnection());
    }
}

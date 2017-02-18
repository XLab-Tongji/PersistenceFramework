package com.tongji409.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author lijiechu
 * @create on 17/2/18
 * @description 数据库连接的实现类
 */
public class DbConnectionImpl implements DbConnection {

    private String DB_DRIVER = "com.mysql.jdbc.Driver";
    private String DB_USER = "root";
    private String DB_PASSWD = "1234";
    private String DB_URL = "jdbc:mysql://localhost:3306/test";

    private Connection conn;
    private Object objId;
    private boolean isClosed = false;

    public DbConnectionImpl(Object obj){
        this.objId = obj;
    }

    /**
     * 该方法用来获取数据库连接
     * @param obj
     * @return conn
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getConnection(Object obj) throws SQLException, ClassNotFoundException {
        try {
            if(this.objId == obj && this.conn != null && !this.isClosed()){
                return conn;
            } else {
                Class.forName(DB_DRIVER);
                conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            }
        } catch (SQLException e) {
            throw new SQLException("获取连接失败" + obj.getClass().getName());
        }
        return conn;
    }

    /**
     * 开启一个事务
     * @param obj
     * @throws SQLException
     */
    public void beginTransaction(Object obj) throws SQLException {
        try {
            if(this.objId == obj && this.conn!= null && !this.conn.isClosed()){
                this.conn.setAutoCommit(false);
            }
        } catch (SQLException e) {
            throw new SQLException("开启事务失败" + obj.getClass().getName() + e);
        }
    }

    /**
     * 提交数据操作
     * @param obj
     * @throws SQLException
     */
    public void commit(Object obj) throws SQLException {
        try {
            if(this.objId == obj && this.conn!= null && !this.conn.isClosed()){
                this.conn.commit();
            }
        } catch (SQLException e) {
            throw new SQLException("提交数据失败" + obj.getClass().getName() + e);
        }
    }

    /**
     * 对事务进行回滚
     * @param obj
     * @throws SQLException
     */
    public void rollback(Object obj) throws SQLException {
        try {
            if(this.objId == obj && this.conn!= null && !this.conn.isClosed()){
                this.conn.rollback();
            }
        } catch (SQLException e) {
            throw new SQLException("回滚事务失败" + obj.getClass().getName() + e);
        }
    }

    /**
     * 关闭数据库连接
     * @param obj
     * @throws SQLException
     */
    public void close(Object obj) throws SQLException {
        try {
            if(this.objId == obj && this.conn!= null && !this.conn.isClosed()){
                this.conn.close();
                isClosed = true;
            }
        } catch (SQLException e) {
            throw new SQLException("关闭数据库失败" + obj.getClass().getName() + e);
        }
    }

    /**
     * @return isClosed
     */
    public boolean isClosed(){
        return this.isClosed;
    }

}

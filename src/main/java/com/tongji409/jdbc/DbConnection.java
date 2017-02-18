package com.tongji409.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lijiechu
 * @create on 17/2/18
 * @description 数据库连接的数据接口
 */
public interface DbConnection {
    /**
     * 该方法用来获取数据库连接
     */
    public Connection getConnection(Object obj) throws SQLException, ClassNotFoundException;

    /**
     * 开始一个事务
     */
    public void beginTransaction(Object obj) throws SQLException;

    /**
     * 提交数据操作
     */
    public void commit(Object obj) throws SQLException;

    /**
     * 对事务进行回滚
     */
    public void rollback(Object obj) throws SQLException;

    /**
     * 关闭数据库连接
     */
    public void close(Object obj) throws SQLException;
}

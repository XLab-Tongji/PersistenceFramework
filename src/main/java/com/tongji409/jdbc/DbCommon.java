package com.tongji409.jdbc;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author lijiechu
 * @create on 17/2/18
 * @description 数据库结果集接口,用来定义获取和更新数据的方法
 */
public interface DbCommon {

    /**
     * 该方法用来设定下面的方法是否使用PreparedStatement
     */
    public void setPstmt();

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @return ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getResultSet(String sql) throws SQLException;

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @return CachedRowSet
     * @throws java.sql.SQLException
     */
    public CachedRowSet getCachedRowSet(String sql) throws SQLException;

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @param limit 用来限制查询笔数,<=0则代表不限制
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet getAllResultSet(String sql, int limit) throws SQLException;

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @param limit 用来限制查询笔数,<=0则代表不限制
     * @return CachedRowSet
     * @throws SQLException
     */
    public CachedRowSet getAllCachedRowSet(String sql, int limit) throws SQLException;

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @return Map
     * @throws java.sql.SQLException
     */
    public Map queryMap(String sql) throws SQLException;

    /**
     * 用来获取单笔数据结果集,希望返回的是对象,可以转换为Javabean
     * 该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @return CachedRowSet
     * @throws java.sql.SQLException
     */
    public Object queryObj(String sql) throws SQLException;

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @param limit 用来限制查询笔数,<=0则代表不限制
     * @return Map[]
     * @throws SQLException
     */
    public Map[] queryAllMap(String sql, int limit) throws SQLException;

    /**
     * 用来获取多笔数据结果集,希望返回的是对象,可以转换为Javabean
     * 该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @param limit 用来限制查询笔数,<=0则代表不限制
     * @return List
     * @throws SQLException
     */
    public List queryAllObj(String sql, int limit) throws SQLException;


    /**
     * 该方法用来更新数据库的资料,该方法默认使用Statement, 如果要使用PreparedStatement,则要先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @param sql
     * @return int 返回值为0时,表示更新失败
     * @throws SQLException
     */
    public int executeUpdate(String sql) throws SQLException;

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @return ResultSet
     * @throws java.sql.SQLException
     */
    public ResultSet getResultSet() throws SQLException;

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @return CachedRowSet
     * @throws java.sql.SQLException
     */
    public CachedRowSet getCachedRowSet() throws SQLException;

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet getAllResultSet() throws SQLException;

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @return CachedRowSet
     * @throws SQLException
     */
    public CachedRowSet getAllCachedRowSet() throws SQLException;

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @return Map
     * @throws java.sql.SQLException
     */
    public Map queryMap() throws SQLException;

    /**
     * 用来获取单笔数据结果集,希望返回的是对象,可以转换为Javabean
     * 该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @return CachedRowSet
     * @throws java.sql.SQLException
     */
    public Object queryObj() throws SQLException;

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @return Map[]
     * @throws SQLException
     */
    public Map[] queryAllMap() throws SQLException;

    /**
     * 用来获取多笔数据结果集,希望返回的是对象,可以转换为Javabean
     * 该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @return List
     * @throws SQLException
     */
    public List queryAllObj() throws SQLException;


    /**
     * 该方法用来更新数据库的资料,该方法默认使用Statement, 如果要使用PreparedStatement,则要先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     * @return int 返回值为0时,表示更新失败
     * @throws SQLException
     */
    public int executeUpdate() throws SQLException;

    /**
     * 用来通过PreparedStatement设定多笔数据结果集
     * @param sql
     * @param limit, 用来限制查询笔数,<=0则代表不限制
     * @throws SQLException
     */
    public void PreparedStatement(String sql, int limit) throws SQLException;


}

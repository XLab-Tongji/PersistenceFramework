package com.tongji409.jdbc;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * @author lijiechu
 * @create on 17/2/18
 * @description DbCommon实现类,用来实现具体的获取和更新数据的方法
 */
public class DbCommonImpl implements DbCommon {

    private Connection conn;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;
    private boolean isStmt = true;
    private String sql = "";
    //private InfoAndOut

    /**
     * 构造函数
     */
    public DbCommonImpl(){

    }

    /**
     * 构造函数
     */
    public DbCommonImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * 该方法用来设定下面的方法是否使用PreparedStatement
     */
    public void setPstmt() {
        this.isStmt = false;
    }

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql) throws SQLException {
        ResultSet rs = null;
        try {
            this.getAllResultSet(sql,1);
        } catch (SQLException e) {
            throw new SQLException("执行getResultSet失败" + sql + e);
        } finally {
            return rs;
        }
    }

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @return CachedRowSet
     * @throws SQLException
     */
    public CachedRowSet getCachedRowSet(String sql) throws SQLException {
        CachedRowSetImpl crs = null;
        try {
            crs = new CachedRowSetImpl();
            crs.populate(this.getResultSet(sql));
        } catch (SQLException e) {
            throw new SQLException("执行getCachedRowSet失败" + sql + e);
        } finally {
            return crs;
        }
    }

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @param limit 用来限制查询笔数,<=0则代表不限制
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet getAllResultSet(String sql, int limit) throws SQLException {
        this.sql = sql;
        stmt = null;
        pstmt = null;
        ResultSet rs = null;
        //判断连接是否为空或已关闭
        if(conn == null || conn.isClosed()){
            throw new SQLException("请首先创建或获取一个连接");
        }

        if(sql == null || "".equals(sql)){
            throw new SQLException("sql语句为空");
        }

        try {
            if(isStmt) { //使用Statement
                synchronized (this.conn) {
                    stmt = this.conn.createStatement();
                }
                if(limit > 0) {
                    stmt.setMaxRows(limit);
                }
                rs = stmt.executeQuery(sql);
            } else { //使用PreparedStatement
                this.PreparedStatement(sql,limit);
                rs = pstmt.executeQuery();
            }
        } catch (SQLException e) {
            throw new SQLException("执行getAllResultSet失败" + sql + e);
        } finally {
            return rs;
        }
    }

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @param limit 用来限制查询笔数,<=0则代表不限制
     * @return CachedRowSet
     * @throws SQLException
     */
    public CachedRowSet getAllCachedRowSet(String sql, int limit) throws SQLException {
        CachedRowSetImpl crs = null;
        try {
            crs = new CachedRowSetImpl();
            crs.populate(this.getAllResultSet(sql,limit));
        } catch (SQLException e) {
            throw new SQLException("执行getAllCachedRowSet失败" + sql + e);
        } finally {
            return crs;
        }
    }

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @return Map
     * @throws SQLException
     */
    public Map queryMap(String sql) throws SQLException {
        Map map = null;
        try {
            Map[] maps = this.queryAllMap(sql,1);
            if(maps != null && maps.length == 1){
                map = maps[0];
            }
        } catch (SQLException e) {
            throw new SQLException("执行queryMap失败" + sql + e);
        } finally {
            return map;
        }
    }

    /**
     * 用来获取单笔数据结果集,希望返回的是对象,可以转换为Javabean
     * 该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @return CachedRowSet
     * @throws SQLException
     */
    public Object queryObj(String sql) throws SQLException {
        Object obj = null;
        try {
            List list = this.queryAllObj(sql,1);
            if(list != null && list.size() == 1){
                obj = list.get(0);
            }
        } catch (SQLException e) {
            throw new SQLException("执行queryObj失败" + sql + e);
        } finally {
            return obj;
        }
    }

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @param limit 用来限制查询笔数,<=0则代表不限制
     * @return Map[]
     * @throws SQLException
     */
    public Map[] queryAllMap(String sql, int limit) throws SQLException {
        Map[] map = null;
        CachedRowSet rs = null;
        List list = new ArrayList();
        try {
            //用过getAllCachedRowSet获取rs,然后循环将每笔转换为map
            for(rs = this.getAllCachedRowSet(sql,limit);rs.next();){
                list.add(getMapFromRs(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("执行queryAllmap失败" + sql + e);
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(list.size() !=0 ){
                map = new Map[list.size()];
                //将list转换为map数组
                list.toArray(map);
            }
            return map;
        }
    }

    /**
     * 用来获取多笔数据结果集,希望返回的是对象,可以转换为Javabean
     * 该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @param limit 用来限制查询笔数,<=0则代表不限制
     * @return List
     * @throws SQLException
     */
    public List queryAllObj(String sql, int limit) throws SQLException {
        CachedRowSet rs = null;
        List list = null;
        try {
            list = new ArrayList();
            for(rs = this.getAllCachedRowSet(sql,limit);rs.next();){
                list.add(getObjFromRs(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("执行queryAllObj失败" + sql + e);
        } finally {
            if(rs != null) {
                rs.close();
            }
            return list;
        }
    }

    /**
     * 该方法用来更新数据库的资料,该方法默认使用Statement, 如果要使用PreparedStatement,则要先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @param sql
     * @return int 返回值为0时,表示更新失败
     * @throws SQLException
     */
    public int executeUpdate(String sql) throws SQLException {
        this.sql = sql;
        int numRow = 0;
        stmt = null;
        pstmt = null;
        //判断连接是否为空或已经关闭
        if(conn == null || conn.isClosed())
            throw new SQLException("请首先创建或获取一个连接");
        //判断传进来的sql是否为空
        if(sql ==null || "".equals(sql.trim()))
            throw new SQLException("SQL为空");
        try {
            //判断是否使用Statement
            if(isStmt) { //使用Statement
                //用来保证一个对象在多个线程中访问该方法时是线性安全的
                synchronized (this.conn) {
                    stmt = this.conn.createStatement();
                }
                numRow = stmt.executeUpdate(sql);
            } else {
                synchronized (this.conn) {
                    pstmt = this.conn.prepareStatement(sql);
                }
                numRow = pstmt.executeUpdate();
                if(pstmt != null) {
                    pstmt.close();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("执行executeUpdate失效" + sql + e);
        } finally {
            return numRow;
        }
    }

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet getResultSet() throws SQLException {
        ResultSet rs = null;
        try {
            rs = this.getAllResultSet();
        } catch (SQLException e) {
            throw new SQLException("执行getResultSet失败" + sql + e);
        } finally {
            return rs;
        }
    }

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @return CachedRowSet
     * @throws SQLException
     */
    public CachedRowSet getCachedRowSet() throws SQLException {
        CachedRowSetImpl crs = null;
        try {
            crs = new CachedRowSetImpl();
            crs.populate(this.getResultSet());
        } catch (SQLException e) {
            throw new SQLException("执行getCachedRowSet失败" + e);
        } finally {
            return crs;
        }
    }

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     *
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet getAllResultSet() throws SQLException {
        ResultSet rs = null;
        if(pstmt == null )
            throw new SQLException("请首先创建或者获取一个PreparedStatement");
        try {
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            throw new SQLException("执行getAllResultSet失败" + sql + e);
        } finally {
            return rs;
        }
    }

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @return CachedRowSet
     * @throws SQLException
     */
    public CachedRowSet getAllCachedRowSet() throws SQLException {
        CachedRowSetImpl crs = null;
        try {
            crs = new CachedRowSetImpl();
            crs.populate(this.getAllResultSet());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("执行getAllCachedRowSet失败" + e);
        } finally {
            return crs;
        }
    }

    /**
     * 用来获取单笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @return Map
     * @throws SQLException
     */
    public Map queryMap() throws SQLException {
        Map map = null;
        try {
            Map[] maps = this.queryAllMap();
            if(maps != null && maps.length == 1){
                map = maps[0];
            }
        } catch (SQLException e) {
            throw new SQLException("执行queryMap失败" + sql + e);
        } finally {
            return map;
        }
    }

    /**
     * 用来获取单笔数据结果集,希望返回的是对象,可以转换为Javabean
     * 该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时,SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @return CachedRowSet
     * @throws SQLException
     */
    public Object queryObj() throws SQLException {
        Object obj = null;
        try {
            List list = this.queryAllObj();
            if(list != null && list.size() == 1){
                obj = list.get(0);
            }
        } catch (SQLException e) {
            throw new SQLException("执行queryObj失败" + sql + e);
        } finally {
            return obj;
        }
    }

    /**
     * 用来获取多笔数据结果集,该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @return Map[]
     * @throws SQLException
     */
    public Map[] queryAllMap() throws SQLException {
        Map[] map = null;
        CachedRowSet rs = null;
        List list = new ArrayList();
        try {
            for(rs = this.getAllCachedRowSet();rs.next();){
                list.add(getMapFromRs(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("执行queryAllMap失败" + sql + e);
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(list.size() != 0) {
                map = new Map[list.size()];
                list.toArray(map);
            }
            return map;
        }
    }

    /**
     * 用来获取多笔数据结果集,希望返回的是对象,可以转换为Javabean
     * 该方法默认使用Statement, 如果要使用PreparedStatement,则要首先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @return List
     * @throws SQLException
     */
    public List queryAllObj() throws SQLException {
        CachedRowSet rs = null;
        List list = null;
        try {
            for(rs = this.getAllCachedRowSet();rs.next();){
                list.add(getObjFromRs(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("执行queryAllObj失败" + sql + e);
        } finally {
            if(rs != null) {
                rs.close();
            }
            return list;
        }
    }

    /**
     * 该方法用来更新数据库的资料,该方法默认使用Statement, 如果要使用PreparedStatement,则要先调用setPstmt()方法
     * 并且使用PreparedStatement时, SQL语句必须是完整的,而不是带有?的,即中途不能设定参数
     *
     * @return int 返回值为0时,表示更新失败
     * @throws SQLException
     */
    public int executeUpdate() throws SQLException {
        return pstmt.executeUpdate();
    }

    /**
     * 用来通过PreparedStatement设定多笔数据结果集
     * @param sql
     * @param limit
     * @throws SQLException
     */
    public void PreparedStatement(String sql, int limit) throws SQLException {
        this.PreparedStatement(sql,limit,0);
    }

    /**
     * 用来通过PreparedStatement设定多笔数据结果集
     * @param sql
     * @throws SQLException
     */
    public void PreparedStatement(String sql) throws SQLException {
        this.PreparedStatement(sql,0);
    }

    /**
     * 该方法用来将获取的栏位名称和栏位内容相对应
     * @param rs
     * @return Map
     */
    private Map getMapFromRs(CachedRowSet rs) throws SQLException {
        Map map = new HashMap();
        List columnNamesList = new ArrayList();
        int columnCount = 0;
        try {
            //获取ResultSetMetaData的字段数目
            columnCount = rs.getMetaData().getColumnCount();
            //获取每个字段的名称
            columnNamesList = setColumnNameByMeta(rs.getMetaData());
            for(int i = 0; i < columnCount; i++) {
                //将字段名和对应的值存入Map
                map.put((String)columnNamesList.get(i),rs.getString(i+1));
            }
        } catch (SQLException e){
            throw new SQLException("执行getMapFromRs失败" + e);
        } finally {
            return map;
        }
    }

    /**
     * 该方法用来获取查询结果集中每一项数据的栏位名称
     * @param rsMetadata
     * @return List
     */
    private List setColumnNameByMeta(ResultSetMetaData rsMetadata) throws SQLException {
        List columnNamesList = new ArrayList();
        try {
            //获取ResultSetMetaData的字段数目
            for(int i=0; i<rsMetadata.getColumnCount(); i++){
                columnNamesList.add(rsMetadata.getColumnName(i+1));
            }
        } catch (SQLException e) {
            throw new SQLException("执行setColumnNameByMeta失败" + e);
        } finally {
            return columnNamesList;
        }
    }

    /**
     * 用来设定PreparedStatement的时间戳参数
     * @param parameterIndex
     * @param x
     * @param cal
     * @throws SQLException
     */
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException{
        pstmt.setTimestamp(parameterIndex, x, cal);
    }

    /**
     * 用来设定PreparedStatement的int型参数
     * @param parameterIndex
     * @param x
     * @throws SQLException
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
        pstmt.setInt(parameterIndex, x);
    }

    /**
     * 用来设定PreparedStatement的Short型参数
     * @param parameterIndex
     * @param x
     * @throws SQLException
     */
    public void setShort(int parameterIndex, short x) throws SQLException {
        pstmt.setShort(parameterIndex, x);
    }

    /**
     * 用来设定PreparedStatement的Long型参数
     * @param parameterIndex
     * @param x
     * @throws SQLException
     */
    public void setLong(int parameterIndex, Long x) throws SQLException {
        pstmt.setLong(parameterIndex, x);
    }

    /**
     * 用来设定PreparedStatement的String型参数
     * @param parameterIndex
     * @param x
     * @throws SQLException
     */
    public void setString(int parameterIndex, String x) throws SQLException {
        pstmt.setString(parameterIndex, x);
    }

    /**
     * 用来设定PreparedStatement的Object型参数
     * @param parameterIndex
     * @param x
     * @param targetSqlType
     * @param scale
     * @throws SQLException
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        pstmt.setObject(parameterIndex, x, targetSqlType, scale);
    }

    /**
     * 用来设定PreparedStatement的日期型参数
     * @param parameterIndex
     * @param x
     * @throws SQLException
     */
    public void setDate(int parameterIndex, Date x) throws SQLException {
        pstmt.setDate(parameterIndex, x);
    }

    /**
     * 用来设定PreparedStatement的日期型参数
     * @param parameterIndex
     * @param x
     * @param cal
     * @throws SQLException
     */
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        pstmt.setDate(parameterIndex, x, cal);
    }

    /**
     * 用来设定PreparedStatement的ASCII流型参数
     * @param parameterIndex
     * @param x
     * @param length
     * @throws SQLException
     */
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        pstmt.setAsciiStream(parameterIndex, x, length);
    }

    /**
     * 用来设定PreparedStatement的null值,根据类型名
     * @param parameterIndex
     * @param sqlType
     * @param typeName
     * @throws SQLException
     */
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        pstmt.setNull(parameterIndex, sqlType, typeName);
    }

    /**
     * 用来设定PreparedStatement的FetchSize的大小, 表示每次从数据库中读取的数量
     * @param rows
     * @throws SQLException
     */
    public void setFetchSize(int rows) throws SQLException {
        pstmt.setFetchSize(rows);
    }

    /**
     * 用来获取PreparedStatement的Fetch Size大小
     * @return int
     * @throws SQLException
     */
    public int getFetchSize() throws SQLException{
        return pstmt.getFetchSize();
    }

    /**
     * @description 用来设定PreparedStatement的字节型参数
     * @date  2016年1月13日
     */
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        pstmt.setBytes(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement的字节流型参数
     * @date  2016年1月13日
     */
    public void setCharacterStream(int parameterIndex, Reader reader, int length)
            throws SQLException {
        pstmt.setCharacterStream(parameterIndex, reader, length);
    }

    /**
     * @description  用来设定PreparedStatement的Double型参数
     * @date  2016年1月13日
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
        pstmt.setDouble(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement中参数的null值
     * @date  2016年1月13日
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        pstmt.setNull(parameterIndex, sqlType);
    }

    /**
     * @description  用来设定PreparedStatement的URL型参数
     * @date  2016年1月13日
     */
    public void setURL(int parameterIndex, URL x) throws SQLException {
        pstmt.setURL(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement的Object型参数，根据目标的类型
     * @date  2016年1月13日
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType)
            throws SQLException {
        pstmt.setObject(parameterIndex, x, targetSqlType);
    }

    /**
     * @description  用来设定PreparedStatement的二进制型参数
     * @date  2016年1月13日
     */
    public void setBinaryStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        pstmt.setBinaryStream(parameterIndex, x, length);
    }

    /**
     * @description  用来设定PreparedStatement的Boolean型参数
     * @date  2016年1月13日
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        pstmt.setBoolean(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement的BigDecimal型参数
     * @date  2016年1月13日
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x)
            throws SQLException {
        pstmt.setBigDecimal(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement的Float型参数
     * @date  2016年1月13日
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
        pstmt.setFloat(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement的时间型参数
     * @date  2016年1月13日
     */
    public void setTime(int parameterIndex, Time x) throws SQLException {
        pstmt.setTime(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement的字节型参数
     * @date  2016年1月13日
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
        pstmt.setByte(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement的时间型参数
     * @date  2016年1月13日
     */
    public void setTime(int parameterIndex, Time x, Calendar cal)
            throws SQLException {
        pstmt.setTime(parameterIndex, x, cal);
    }

    /**
     * @description  用来设定PreparedStatement的Object型参数
     * @date  2016年1月13日
     */
    public void setObject(int parameterIndex, Object x) throws SQLException {
        pstmt.setObject(parameterIndex, x);
    }

    /**
     * @description  用来设定PreparedStatement的时间戳型参数，INSERT INTO User(username, CREATEDATETIME) VALUES(?,CURRENT TIMESTAMP)
     * @date  2016年1月13日
     */
    public void setTimestamp(int parameterIndex, Timestamp x)
            throws SQLException {
        pstmt.setTimestamp(parameterIndex, x);
    }

    /**
     * 用来通过PreparedStatement设定多笔数据结果集
     * @param sql
     * @param limit ,用来限制查询笔数, <=0则代表不限制
     * @param gk, 用来自动生成主键值, >表示自动生成主键
     * @throws SQLException
     */
    public void PreparedStatement(String sql, int limit, int gk) throws SQLException {
        this.sql = sql;
        pstmt = null;
        //判断连接是否为空或已关闭
        if(conn == null || conn.isClosed())
            throw new SQLException("请首先创建或获取一个连接");
        //判断传进来的sql是否为空
        if(sql == null || "".equals(sql)){
            throw new SQLException("sql语句为空");
        }
        try {
            synchronized (this.conn) {
                if(gk > 0) {
                    pstmt = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                } else {
                    pstmt = this.conn.prepareStatement(sql);
                }
            }
            if(limit > 0) {
                pstmt.setMaxRows(limit);
            }
        } catch (SQLException e) {
            throw new SQLException("执行PreparedStatement失败" + sql + e);
        }
    }

    /**
     * 返回自动生成的主键值
     * @throws SQLException
     */
    public int getGeneratedKeys() throws SQLException {
        ResultSet rs = pstmt.getGeneratedKeys();
        int gk = Integer.MIN_VALUE;
        if(rs.next())
            gk = rs.getInt(1);
        rs.close();
        rs = null;
        return gk;
    }

    /**
     * 用来增加Batch
     * @throws SQLException
     */
    public void addBatch() throws SQLException{
        pstmt.addBatch();
    }

    /**
     * 用来执行Batch
     * @throws SQLException
     */
    public int[] executeBatch() throws SQLException{
        return pstmt.executeBatch();
    }

    /**
     * 用来清空Batch
     * @throws SQLException
     */
    public void clearBatch() throws SQLException{
        pstmt.clearBatch();
    }

    /**
     * 设定conn
     * @param conn
     */
    public void setConn(Connection conn){
        this.conn = conn;
    }

    /**
     * 获取conn
     */
    public Connection getConn(){
        return this.conn;
    }
}

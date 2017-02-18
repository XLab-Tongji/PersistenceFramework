package com.tongji409.jdbc;

import java.sql.SQLException;

/**
 * @author lijiechu
 * @create on 17/2/18
 * @description
 */
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //启动obj
        Object seedObj = new Object();
        DbConnection conn = new DbConnectionImpl(seedObj);
        DbCommon comm = new DbCommonImpl(conn.getConnection(seedObj));
        String sql = "select * from user";

        User user = (User)comm.queryObj(sql,User.class);
        System.out.println("Age: " + user.getAge()+ "Name:" + user.getName());

    }
}

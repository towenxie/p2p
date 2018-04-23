package edu.xmh.p2p.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.xmh.p2p.server.config.ServerAppConfigHolder;


public class DBBase {
    String url = ServerAppConfigHolder.getAppConfig().getJdbcUrl();// 连接字符串
    String name = ServerAppConfigHolder.getAppConfig().getUserName();
    String pwd = ServerAppConfigHolder.getAppConfig().getPassWord();// 用户名和密码
    Connection conn = null;

    /*
     * 数据库连接
     */

    public Connection getConn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, name, pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("没有找到数据库加载类");
        } catch (SQLException e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
        return conn;
    }

    /*
     * 关闭连接127.0.0.1:3306 127.0.0.1:54861 ESTABLISHED 3200
     */
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("流关闭失败");
                e.printStackTrace();
            }
        }
    }

    /*
     * 进行添加，删除，修改等操作
     */
    public int ExcuteSql(String sql) throws Exception {
        int re = -1;
        Connection conn = getConn();
        Statement st = null;
        try {
            st = conn.createStatement();
            int cou = st.executeUpdate(sql);
            if (cou > 0) {
                re = 1;
            }
        } catch (Exception e) {
            System.out.println("数据库执行插入，删除，更新操作错误");
        } finally {
            st.close();
            conn.close();
        }
        return re;
    }

    /*
     * 执行数据库查询功能
     */
    public ResultSet ExcuteSqlToSet(String sql) throws Exception {
        Connection conn = getConn();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            return rs;
        } catch (Exception e) {
            System.out.println("数据库执行查询操作错误");
        } finally {
            // conn.close();
        }
        return null;
    }

    public static void main(String[] args) {
        new DBBase().getConn();
    }
}

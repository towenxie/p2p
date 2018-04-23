package edu.xmh.p2p.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.xmh.p2p.server.config.ServerAppConfigHolder;


public class DBBase {
    String url = ServerAppConfigHolder.getAppConfig().getJdbcUrl();// �����ַ���
    String name = ServerAppConfigHolder.getAppConfig().getUserName();
    String pwd = ServerAppConfigHolder.getAppConfig().getPassWord();// �û���������
    Connection conn = null;

    /*
     * ���ݿ�����
     */

    public Connection getConn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, name, pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("û���ҵ����ݿ������");
        } catch (SQLException e) {
            System.out.println("���ݿ�����ʧ��");
            e.printStackTrace();
        }
        return conn;
    }

    /*
     * �ر�����127.0.0.1:3306 127.0.0.1:54861 ESTABLISHED 3200
     */
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("���ر�ʧ��");
                e.printStackTrace();
            }
        }
    }

    /*
     * ������ӣ�ɾ�����޸ĵȲ���
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
            System.out.println("���ݿ�ִ�в��룬ɾ�������²�������");
        } finally {
            st.close();
            conn.close();
        }
        return re;
    }

    /*
     * ִ�����ݿ��ѯ����
     */
    public ResultSet ExcuteSqlToSet(String sql) throws Exception {
        Connection conn = getConn();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            return rs;
        } catch (Exception e) {
            System.out.println("���ݿ�ִ�в�ѯ��������");
        } finally {
            // conn.close();
        }
        return null;
    }

    public static void main(String[] args) {
        new DBBase().getConn();
    }
}

package edu.xmh.p2p.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理客户端请求信息
 * 
 * @author SGL
 * 
 */

public class DealRequest {

    private static DealRequest request = null;
    private DBBase base = new DBBase();

    public static DealRequest getInstance() {
        if (request == null) {
            request = new DealRequest();
        }
        return request;
    }

    /**
     * 访问数据库是否存在目标文件
     * 
     * @author SGL
     * @param=fname 文件名
     */
    public List<FileInfoEntity> getDataByFileName(String fname) {
        String sql = "select * from filenameInfo where filename like '%" + fname + "%'";
        List<FileInfoEntity> list = new ArrayList<FileInfoEntity>();
        Connection conn = base.getConn();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            int i = -1;
            while (rs.next()) {
                FileInfoEntity fi = new FileInfoEntity();
                fi.setId(rs.getInt(1));
                fi.setFilename(rs.getString(2));
                fi.setFileContent(rs.getString(3));
                list.add(fi);
                i++;
                if (i > 5) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        /*
         * DealRequest dr = new DealRequest(); dr.DealRequestCmd("btfile");
         */
        /*
         * System.out.println("btfile.rar".substring(0,"btfile.rar".length() - 4));
         */
        String ip = "/125.219.39.120";
        System.out.println("ip---" + ip.substring(1, ip.length()));
    }

}

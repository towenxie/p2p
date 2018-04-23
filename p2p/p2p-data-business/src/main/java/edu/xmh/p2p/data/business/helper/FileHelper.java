package edu.xmh.p2p.data.business.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import edu.xmh.p2p.data.business.constant.P2PConstants;

public class FileHelper {
    private static String filePath = P2PConstants.appConfig.getTempFile();

    /**
     * 功能：写到本地文件
     */
    public static void write(String message) {

        try {
            FileWriter fout = new FileWriter(filePath, true);
            BufferedWriter dout = new BufferedWriter(fout);

            dout.write(message);
            dout.newLine();
            dout.close();
            fout.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 读取客户端之前关闭时，没有下载完的文件
    public static List<String> readTxtFile() {
        List<String> filelist = new LinkedList<String>();
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                    filelist.add(lineTxt);
                }
                read.close();
                file.delete();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return filelist;
    }
}

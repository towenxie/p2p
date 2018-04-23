package edu.xmh.p2p.data.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class.getName());

    public static Properties loadProperties(String fileName) {
        Properties pro = null;
        try {
            pro = new Properties();
            FileInputStream fin = new FileInputStream(FileUtils.getFilePath(fileName + ".properties"));
            pro.load(fin);
            fin.close();
        } catch (Exception e) {
            logger.error("Error to load the conf.", e);
        }
        return pro;
    }

    public static String loadFile(String fileName, String suffix) {
        String result = "";
        try {
            // String encoding = "GBK";
            String filePath = FileUtils.getFilePath(fileName + "." + suffix);
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    result += lineTxt;
                }
                read.close();
            }
        } catch (Exception e) {
            logger.error("Error to load the conf.", e);
        }
        return result;
    }

    public static String getFilePath(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + fileName;
    }
    
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (!file.exists()) {
            return flag;
        } else {
            if (file.isFile()) {  
                return deleteFile(sPath);
            } else {  
                return deleteDirectory(sPath);
            }
        }
    }
    
    public static boolean deleteFile(String sPath) {
    	boolean flag = false;
        File file = new File(sPath);

        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    
    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } 
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
}

package edu.xmh.p2p.server.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

    public static List<String> getContents(String fileName) {
        List<String> contents = new ArrayList<String>();
        try {
            File file = new File(FileUtils.getFilePath(fileName));
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    contents.add(lineTxt);
                }
                read.close();
            } else {
                logger.error("cannot found!");
            }
        } catch (Exception e) {
            logger.error("read error!", e);
        }

        return contents;
    }

    public static String getFilePath(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + fileName;
    }
}

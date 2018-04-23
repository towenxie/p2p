package edu.xmh.p2p.client.thrift;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import edu.xmh.p2p.client.common.FileUtils;
import edu.xmh.p2p.client.config.ClientAppConfig;
import edu.xmh.p2p.client.config.ThriftConfig;
import edu.xmh.p2p.client.ftp.FTPDispatcherApp;
import edu.xmh.p2p.client.thrift.TNodeService.Client;

public class ThriftClient {
    private static final Logger logger = LogManager.getLogger(ThriftClient.class.getName());
    private final int timeout = 30000;
    private static ThriftClient thriftClient = null;
    private static ClientAppConfig appConfig = null;

    public static ThriftClient getInstance() {
        if (thriftClient == null) {
            thriftClient = new ThriftClient();
        }
        if (appConfig == null) {
            appConfig = loadAppConf();
        }
        return thriftClient;
    }

 /**
  * 单文件下载
  * 查询数据库，获取该文件所有的ftp地址。
  * 
  * @param fileName
  */
    public void downLoadFile(String fileName) {
        List<String> ftpUrls = new ArrayList<String>();

        for (ThriftConfig config : appConfig.getThriftConfigs()) {
            List<String> results = startUp(config, fileName);
            ftpUrls.addAll(results);
        }
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            logger.error("Waiting result error");
        }
        String localPath = appConfig.getDownloadPath();
        logger.info("Count ftpUrls: " + ftpUrls.size());
        FTPDispatcherApp.downloadForWeb(ftpUrls, localPath);
    }

/**
 * 瓦片下载
 * 查询数据库，获取该文件名的，所有瓦片数据的的ftp地址。
 * 
 * @param fileName
 */
    public void multipleDownload(String fileName) {
        List<String> ftpUrls = new ArrayList<String>();

        for (ThriftConfig config : appConfig.getThriftConfigs()) {
            List<String> results = startUpMultipleDownload(config, fileName);
            ftpUrls.addAll(results);
        }
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            logger.error("Waiting result error");
        }
        String localPath = appConfig.getDownloadPath();
        logger.info("Count ftpUrls: " + ftpUrls.size());
        FTPDispatcherApp.multipleDownload(ftpUrls, localPath);
    }


    private List<String> startUp(ThriftConfig config, String fileName) {
        List<String> ftpUrls = new ArrayList<String>();
        TTransport transport = null;
        try {
            transport = new TFramedTransport(new TSocket(config.getHost(), config.getPort(), this.timeout));

            Client client = new TNodeService.Client(new TCompactProtocol(transport));
            transport.open();
            logger.info("Client queryFileUrls: " + fileName);
            ftpUrls = client.queryFileUrls(fileName);
            transport.close();
        } catch (TException x) {
            logger.error("Cannot connect server: " + config.toString(), x);
        } finally {
            if (null != transport) {
                transport.close();
            }
        }

        return ftpUrls;
    }
    
    private List<String> startUpMultipleDownload(ThriftConfig config, String fileName) {
        List<String> ftpUrls = new ArrayList<String>();
        TTransport transport = null;
        try {
            transport = new TFramedTransport(new TSocket(config.getHost(), config.getPort(), this.timeout));

            Client client = new TNodeService.Client(new TCompactProtocol(transport));
            transport.open();
            logger.info("Client queryFileUrls: " + fileName);
            ftpUrls = client.queryFilePatchUrls(fileName);
            transport.close();
        } catch (TException x) {
            logger.error("Cannot connect server: " + config.toString(), x);
        } finally {
            if (null != transport) {
                transport.close();
            }
        }

        return ftpUrls;
    }

    private static ClientAppConfig loadAppConf() {
        ClientAppConfig appConfig = null;
        Properties pro = FileUtils.loadProperties("app");
        if (null != pro) {
            appConfig = new ClientAppConfig();
            appConfig.setDownloadPath(pro.get("download_path").toString());
            appConfig.setThriftConfigName(pro.get("thirift_config_name").toString());
        }
        List<String> contents = FileUtils.getContents(appConfig.getThriftConfigName());
        if (null != contents) {
            List<ThriftConfig> thiriftConfigs = new ArrayList<>(contents.size());
            for (String content : contents) {
                String[] config = content.split(",");
                if (config.length > 2) {
                    ThriftConfig thiriftConfig = new ThriftConfig();
                    thiriftConfig.setName(config[0]);
                    thiriftConfig.setHost(config[1]);
                    thiriftConfig.setPort(Integer.parseInt(config[2]));
                    thiriftConfigs.add(thiriftConfig);
                }
            }
            appConfig.setThriftConfigs(thiriftConfigs);
        }
        return appConfig;
    }
}

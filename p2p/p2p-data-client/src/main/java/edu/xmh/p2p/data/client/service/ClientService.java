package edu.xmh.p2p.data.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import edu.xmh.p2p.data.business.conf.ClientAppConfig;
import edu.xmh.p2p.data.business.constant.ClientConstants;
import edu.xmh.p2p.data.business.ftp.ftp.FTPDispatcherApp;
import edu.xmh.p2p.data.business.service.ClientManager;
import edu.xmh.p2p.data.common.util.SpringUtils;
import edu.xmh.p2p.data.common.util.XmlUtils;
import edu.xmh.p2p.data.platform.BaseApplication;
import edu.xmh.p2p.data.platform.PlatformConstant;
import edu.xmh.p2p.data.platform.application.ThriftApplication;

@Service
public class ClientService {
    @Resource
    private ClientAppConfig clientAppConfig;

    public void startUp() {
        ClientConstants.appConfig = clientAppConfig;
        // String serverConfig = XmlUtils.loadXml(PlatformConstant.THRIFT_SERVERS_CONFIG_KEY);
        String clientConfig = XmlUtils.loadXml(PlatformConstant.THRIFT_CLIENT_CONFIG_KEY);
        Map<String, String> configItems = new HashMap<String, String>();
        // configItems.put(PlatformConstant.THRIFT_SERVERS_CONFIG_KEY, serverConfig);
        configItems.put(PlatformConstant.THRIFT_CLIENT_CONFIG_KEY, clientConfig);
        BaseApplication thriftApp = new ThriftApplication(configItems);
        thriftApp.startup();


        System.out.println(clientAppConfig.getToolType() + "=============");
        downLoadFile("test");
        // downLoadMultipleFile("test");

        // EventQueue.invokeLater(new Runnable() {
        // public void run() {
        // try {
        // ClientView window = new ClientView();
        // window.frame.setVisible(true);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // });
    }

    private void downLoadFile(String fileName) {
        ClientManager clientManager = SpringUtils.getBean(ClientManager.class);
        List<String> infos = clientManager.getDataByFileName(fileName);
        String localPath = ClientConstants.appConfig.getDownloadPath();
        FTPDispatcherApp.downloadForWeb(infos, localPath);
    }

    private void downLoadMultipleFile(String fileName) {
        ClientManager clientManager = SpringUtils.getBean(ClientManager.class);
        List<String> infos = clientManager.queryFilePatchsByFileName(fileName);
        String localPath = ClientConstants.appConfig.getDownloadPath();
        FTPDispatcherApp.multipleDownload(infos, localPath);
    }
}

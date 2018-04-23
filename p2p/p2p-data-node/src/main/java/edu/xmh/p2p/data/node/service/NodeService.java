package edu.xmh.p2p.data.node.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import edu.xmh.p2p.data.business.conf.NodeAppConfig;
import edu.xmh.p2p.data.business.constant.NodeConstants;
import edu.xmh.p2p.data.common.util.XmlUtils;
import edu.xmh.p2p.data.platform.BaseApplication;
import edu.xmh.p2p.data.platform.PlatformConstant;
import edu.xmh.p2p.data.platform.application.ThriftApplication;

@Service
public class NodeService {
//    @Resource
//    private NodeAppConfig appConfig;

    public void startUp() {
//        NodeConstants.appConfig = appConfig;
        String serverConfig = XmlUtils.loadXml(PlatformConstant.THRIFT_SERVERS_CONFIG_KEY);
//        String clientConfig = XmlUtils.loadXml(PlatformConstant.THRIFT_CLIENT_CONFIG_KEY);
        Map<String, String> configItems = new HashMap<String, String>();
        configItems.put(PlatformConstant.THRIFT_SERVERS_CONFIG_KEY, serverConfig);
//        configItems.put(PlatformConstant.THRIFT_CLIENT_CONFIG_KEY, clientConfig);
        BaseApplication thriftApp = new ThriftApplication(configItems);
        thriftApp.startup();
    }
}

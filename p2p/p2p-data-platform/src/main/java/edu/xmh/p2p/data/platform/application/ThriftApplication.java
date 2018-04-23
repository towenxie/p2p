/*
 * Copyright (c) 2015 Forte Tradebook, Inc. All rights reserved.
 */
package edu.xmh.p2p.data.platform.application;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import edu.xmh.p2p.data.platform.BaseApplication;
import edu.xmh.p2p.data.platform.PlatformConstant;
import edu.xmh.p2p.data.platform.thrift.client.ThriftClient;
import edu.xmh.p2p.data.platform.thrift.server.ThriftServerManager;

public class ThriftApplication extends BaseApplication {

    private ThriftServerManager thriftManager = new ThriftServerManager();

    public ThriftApplication(Map<String, String> configItems) {
        super(configItems);
    }

    @Override
    public void startup() {
        Map<String, String> configItems = getConfigItems();
        String serverConfigValue = configItems.get(PlatformConstant.THRIFT_SERVERS_CONFIG_KEY);
        if (StringUtils.isNotBlank(serverConfigValue)) {
            thriftManager.startServersFromConfig(serverConfigValue);
        }

        String clientConfigValue = configItems.get(PlatformConstant.THRIFT_CLIENT_CONFIG_KEY);
        if (StringUtils.isNotBlank(clientConfigValue)) {
            ThriftClient.configure(clientConfigValue);
        }
    }

    @Override
    public void shutdown() {
        thriftManager.stopServers();
    }
}

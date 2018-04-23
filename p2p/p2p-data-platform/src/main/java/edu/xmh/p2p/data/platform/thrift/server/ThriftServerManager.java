package edu.xmh.p2p.data.platform.thrift.server;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.xmh.p2p.data.common.util.XmlUtils;

public class ThriftServerManager {
    private List<ThriftServer> thriftServers = new ArrayList<ThriftServer>();
    private static final Logger logger = LogManager.getLogger(ThriftServerManager.class.getName());

    public void startServersFromConfig(String configValue) {
        ThriftServerConfig config = XmlUtils.getBean(configValue, ThriftServerConfig.class);

        for (ThriftServerConfig.Server server : config.getServers()) {
            ThriftServer thriftServer = null;
            try {
                thriftServer = new ThriftServer(server);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                logger.error(e.getMessage(), e);
            }
            thriftServer.startServer();
            thriftServers.add(thriftServer);
        }
    }

    public void stopServers() {
        for (ThriftServer server : thriftServers) {
            server.stopServer();
        }
    }

}

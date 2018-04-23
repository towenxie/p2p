/*
 * Copyright (c) 2015 Forte Tradebook, Inc. All rights reserved.
 */
package edu.xmh.p2p.data.platform.thrift.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import edu.xmh.p2p.data.common.util.XmlUtils;
import edu.xmh.p2p.data.platform.PlatformConstant;
import edu.xmh.p2p.data.platform.thrift.server.ThriftServerConfig;

public class ThriftClient {

    private static final Logger logger = LogManager.getLogger(ThriftClient.class.getName());

    private static Map<String, ThriftPoolManager> thriftPoolManagerMap = new HashMap<String, ThriftPoolManager>();
    private static ThriftPoolConfig thriftPoolConfig = new ThriftPoolConfig();

    public static void configure(String configValue) {
        logger.debug("config thrift pool...");

        ThriftServerConfig thriftServerConfig = XmlUtils.getBean(configValue, ThriftServerConfig.class);

        if (null == thriftServerConfig || null == thriftServerConfig.getServers()
                || thriftServerConfig.getServers().isEmpty()) {
            return;
        }

        for (ThriftServerConfig.Server server : thriftServerConfig.getServers()) {
            ThriftPoolManager thriftPoolManager =
                    new ThriftPoolManager(server.getHost(), server.getPort(),
                            PlatformConstant.THRIFT_CONNECTION_TIME_OUT, thriftPoolConfig);
            String serverProfile = server.getServerProfile();
            String prefix = "";
            if (StringUtils.isNotBlank(serverProfile)) {
            	prefix = serverProfile;
            }

            Set<ThriftServerConfig.Service> services = server.getServices();
            if (null != services && !services.isEmpty()) {
                for (ThriftServerConfig.Service service : services) {
                    thriftPoolManagerMap.put(prefix + service.getName(), thriftPoolManager);
                }
            }
        }
    }


    private static ThriftPoolManager getThriftPoolManager(String seriviceName) {
        return StringUtils.isNotBlank(seriviceName) ? thriftPoolManagerMap.get(seriviceName) : null;
    }

    private static ThriftPoolManager getThriftPoolManager(String brokerCode, String seriviceName) {
        return StringUtils.isNotBlank(seriviceName) ? thriftPoolManagerMap.get(brokerCode + seriviceName) : null;
    }

    public static TProtocol getProtocol(String seriviceName) {
        TProtocol protocol = null;

        ThriftPoolManager thriftPoolManager = getThriftPoolManager(seriviceName);
        if (null != thriftPoolManager) {
            TTransport tTransport = thriftPoolManager.getSocket();
            if (null != tTransport) {
                protocol = new TMultiplexedProtocol(new TCompactProtocol(tTransport), seriviceName);
            }
        }

        return protocol;
    }

    public static TProtocol getProtocol(String brokerCode, String seriviceName) {
        TProtocol protocol = null;

        ThriftPoolManager thriftPoolManager = getThriftPoolManager(brokerCode, seriviceName);
        if (null != thriftPoolManager) {
            TTransport tTransport = thriftPoolManager.getSocket();
            if (null != tTransport) {
                protocol = new TMultiplexedProtocol(new TCompactProtocol(tTransport), seriviceName);
            }
        }

        return protocol;
    }

    public static void returnProtocol(TProtocol protocol, String seriviceName) {
        ThriftPoolManager thriftPoolManager = getThriftPoolManager(seriviceName);
        if (null != thriftPoolManager && null != protocol) {
            TTransport tTransport = protocol.getTransport();
            if (null != tTransport) {
                thriftPoolManager.returnSocket(tTransport);
            }
        }
    }

    public static void returnProtocol(TProtocol protocol, String brokerCode, String seriviceName) {
        ThriftPoolManager thriftPoolManager = getThriftPoolManager(brokerCode, seriviceName);
        if (null != thriftPoolManager && null != protocol) {
            TTransport tTransport = protocol.getTransport();
            if (null != tTransport) {
                thriftPoolManager.returnSocket(tTransport);
            }
        }
    }

    public static void closeProtocol(TProtocol protocol) {
        TTransport tTransport = protocol.getTransport();
        if (null != tTransport && tTransport.isOpen()) {
            tTransport.close();
        }
    }
}

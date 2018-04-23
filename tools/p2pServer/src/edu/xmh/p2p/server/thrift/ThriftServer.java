package edu.xmh.p2p.server.thrift;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.server.TThreadedSelectorServer.Args;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;

import edu.xmh.p2p.server.common.FileUtils;
import edu.xmh.p2p.server.config.ServerAppConfig;
import edu.xmh.p2p.server.config.ServerAppConfigHolder;
import edu.xmh.p2p.server.thrift.TNodeService.Processor;


public class ThriftServer {
    private static final Logger logger = LogManager.getLogger(ThriftServer.class.getName());
    private static ThriftServer thriftServer = null;
    private static ServerAppConfig appConfig = null;

    public static ThriftServer getInstance() {
        if (thriftServer == null) {
            thriftServer = new ThriftServer();
        }
        if (appConfig == null) {
            appConfig = loadAppConf();
            ServerAppConfigHolder.setAppConfig(appConfig);
        }
        return thriftServer;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void startUp() {
        try {
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(appConfig.getThriftPort());
            Processor processor = new TNodeService.Processor(new TNodeServiceImpl());
            Args args = new Args(serverTransport).processor(processor);
            args.protocolFactory(new TCompactProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());
            TServer server = new TThreadedSelectorServer(args);
            logger.info("Starting server on port: " + appConfig.getThriftPort());
            server.serve();
        } catch (TException x) {
            logger.error("Cannot connect server: " + appConfig.toString(), x);
        }
    }

    private static ServerAppConfig loadAppConf() {
        ServerAppConfig appConfig = null;
        Properties pro = FileUtils.loadProperties("app");
        if (null != pro) {
            appConfig = new ServerAppConfig();
            appConfig.setUserName(pro.get("db.userName").toString());
            appConfig.setPassWord(pro.get("db.passWord").toString());
            appConfig.setJdbcUrl(pro.get("db.jdbcUrl").toString());
            appConfig.setThriftPort(Integer.parseInt(pro.get("thirift.port").toString()));
        }

        return appConfig;
    }
}

package edu.xmh.p2p.data.business.service.client;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.protocol.TProtocol;

import edu.xmh.p2p.data.contract.service.node.TNodeService;
import edu.xmh.p2p.data.contract.service.node.TNodeService.Client;
import edu.xmh.p2p.data.platform.thrift.client.ThriftClient;

public class NodeClient {
    private static final Logger logger = LogManager.getLogger(NodeClient.class.getName());

    public static List<String> queryFileUrls(String fileName, String nodeService) {
        List<String> fileUrls = new LinkedList<String>();
        TProtocol protocol = ThriftClient.getProtocol(nodeService);
        try {
            Client client = new TNodeService.Client(protocol);

            if (StringUtils.isNotBlank(fileName)) {
                fileUrls.addAll(client.queryFileUrls(fileName));
            }
        } catch (Exception e) {
            logger.error("failed to transport log " + fileName, e);
        } finally {
            ThriftClient.returnProtocol(protocol, nodeService);
        }
        return fileUrls;
    }
    

    public static List<String> queryFilePatchUrls(String fileName, String nodeService) {
        List<String> fileUrls = new LinkedList<String>();
        TProtocol protocol = ThriftClient.getProtocol(nodeService);
        try {
            Client client = new TNodeService.Client(protocol);

            if (StringUtils.isNotBlank(fileName)) {
                fileUrls.addAll(client.queryFilePatchUrls(fileName));
            }
        } catch (Exception e) {
            logger.error("failed to transport log " + fileName, e);
        } finally {
            ThriftClient.returnProtocol(protocol, nodeService);
        }
        return fileUrls;
    }
}

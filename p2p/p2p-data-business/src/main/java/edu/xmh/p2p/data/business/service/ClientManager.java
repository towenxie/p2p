package edu.xmh.p2p.data.business.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.xmh.p2p.data.business.service.client.NodeClient;

@Service
public class ClientManager {


    public List<String> getDataByFileName(String fileName) {
        List<String> fileUrls = new LinkedList<String>();
        for (String node : getAllNodes()) {
            fileUrls.addAll(NodeClient.queryFileUrls(fileName, node));
        }

        return fileUrls;
    }

    public List<String> queryFilePatchsByFileName(String fileName) {
        List<String> fileUrls = new LinkedList<String>();
        for (String node : getAllNodes()) {
            fileUrls.addAll(NodeClient.queryFilePatchUrls(fileName, node));
        }

        return fileUrls;
    }

    public List<String> getAllNodes() {
        List<String> nodes = new LinkedList<String>();
        nodes.add("tnodeService");
        return nodes;
    }
}

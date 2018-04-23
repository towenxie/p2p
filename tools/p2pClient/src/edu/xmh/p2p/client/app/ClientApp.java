package edu.xmh.p2p.client.app;

import edu.xmh.p2p.client.thrift.ThriftClient;

public class ClientApp 
{

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        ThriftClient thriftClient = ThriftClient.getInstance();
//        thriftClient.downLoadFile("fileName");
        thriftClient.multipleDownload("fileName");

    }
}
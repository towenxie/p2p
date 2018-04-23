package edu.xmh.p2p.server.app;

import java.util.Scanner;

import edu.xmh.p2p.server.thrift.ThriftServer;

public class ServerApp {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        ThriftServer thriftServer = ThriftServer.getInstance();
        thriftServer.startUp();

        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equalsIgnoreCase("stop")) {
            // do nothing
        }
        scanner.close();
        System.exit(0);
    }
}

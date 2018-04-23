package edu.xmh.p2p.server.config;

public class ServerAppConfigHolder {
    private static ServerAppConfig appConfig = null;

    public static ServerAppConfig getAppConfig() {
        return appConfig;
    }

    public static void setAppConfig(ServerAppConfig appConfig) {
        ServerAppConfigHolder.appConfig = appConfig;
    }
}

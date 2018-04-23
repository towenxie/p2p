package edu.xmh.p2p.client.config;

import java.util.List;

public class ClientAppConfig {
    private String downloadPath;
    private String thriftConfigName;
    private List<ThriftConfig> thriftConfigs;

    public List<ThriftConfig> getThriftConfigs() {
        return thriftConfigs;
    }

    public void setThriftConfigs(List<ThriftConfig> thriftConfigs) {
        this.thriftConfigs = thriftConfigs;
    }

    public String getThriftConfigName() {
        return thriftConfigName;
    }

    public void setThriftConfigName(String thriftConfigName) {
        this.thriftConfigName = thriftConfigName;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }
}

package edu.xmh.p2p.data.platform;

import java.util.Map;

public abstract class BaseApplication {

    Map<String, String> configItems;

    public BaseApplication() {
        super();
    }

    public BaseApplication(Map<String, String> configItems) {
        this.configItems = configItems;
    }

    public abstract void startup();

    public abstract void shutdown();

    public Map<String, String> getConfigItems() {
        return configItems;
    }

}

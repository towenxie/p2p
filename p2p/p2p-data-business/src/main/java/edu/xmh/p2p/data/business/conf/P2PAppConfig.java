/*
 * Copyright (c) 2016 Augmentum, Inc. All rights reserved.
 */
package edu.xmh.p2p.data.business.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:app.properties")
public class P2PAppConfig {

    @Value("${tool.type}")
    private String toolType;

    @Value("${tool.temp.file}")
    private String tempFile;
    
    @Value("${tool.download.size}")
    private int downloadSize;
    
    @Value("${tool.download.path}")
    private String downloadPath;

    public boolean isFromDB() {
        return "db".equals(toolType);
    }
    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }
    public String getTempFile() {
        return tempFile;
    }
    public void setTempFile(String tempFile) {
        this.tempFile = tempFile;
    }
    public int getDownloadSize() {
        return downloadSize;
    }
    public void setDownloadSize(int downloadSize) {
        this.downloadSize = downloadSize;
    }
    public String getDownloadPath() {
        return downloadPath;
    }
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }
}

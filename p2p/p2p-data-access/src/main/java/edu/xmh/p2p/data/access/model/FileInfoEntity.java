package edu.xmh.p2p.data.access.model;

import java.util.Date;

public class FileInfoEntity {
    private int id;
    private String fileName;
    private String fileContent;
    private boolean isEnabled;
    private Date creacteDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Date getCreacteDate() {
        return creacteDate;
    }

    public void setCreacteDate(Date creacteDate) {
        this.creacteDate = creacteDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

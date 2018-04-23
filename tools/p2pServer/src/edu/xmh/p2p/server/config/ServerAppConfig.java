package edu.xmh.p2p.server.config;

public class ServerAppConfig {
    private String userName;
    private String passWord;
    private String jdbcUrl;
    private int thriftPort;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public int getThriftPort() {
        return thriftPort;
    }

    public void setThriftPort(int thriftPort) {
        this.thriftPort = thriftPort;
    }
}

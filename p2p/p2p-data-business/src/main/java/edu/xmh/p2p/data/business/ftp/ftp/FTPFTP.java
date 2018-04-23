package edu.xmh.p2p.data.business.ftp.ftp;

/*
 * FTp服务器的信息封装
 */
public class FTPFTP {

	private String ipAddr; // FTP服务器的IP地址

	private Integer port; // FTP服务器的端口号

	private String userName;// 登录FTP服务器的用户名

	private String pwd; // 登录FTP服务器的用户名的口令

	private String path; // FTP服务器上的路径

	private String localPath; // 本地路径

	private String dataName; // 文件名字

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public static FTPFTP getFTP() {
		FTPFTP ftp = new FTPFTP();
		ftp.setIpAddr("");
		ftp.setUserName("wl");
		ftp.setPwd("123");
		ftp.setPath("/");

		return ftp;
	}
}

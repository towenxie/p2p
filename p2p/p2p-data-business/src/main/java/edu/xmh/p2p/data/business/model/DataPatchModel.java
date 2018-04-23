package edu.xmh.p2p.data.business.model;
/**
 * 下载过程中数据块的记�?
 * @author Xiongminghao 
 *
 */
public class DataPatchModel {

	private String siteIp = "";
	private int threadNumber;   //线程�?
	private int number;   //块的编号
	private long startByte;
	private long endByte;
	private int status = 0; // 0 未开�?, 1 正在下载, 2 下载结束

	public String getSiteIp() {
		return siteIp;
	}

	public void setSiteIp(String siteIp) {
		this.siteIp = siteIp;
	}

	public long getStartByte() {
		return startByte;
	}

	public void setStartByte(long startByte) {
		this.startByte = startByte;
	}

	public long getEndByte() {
		return endByte;
	}

	public void setEndByte(long endByte) {
		this.endByte = endByte;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	@Override
	public String toString() {
		return "DataPatchModel [siteIp=" + siteIp + ", threadNumber="
				+ threadNumber + ", number=" + number + ", startByte="
				+ startByte + ", endByte=" + endByte + ", status=" + status
				+ "]";
	}
}

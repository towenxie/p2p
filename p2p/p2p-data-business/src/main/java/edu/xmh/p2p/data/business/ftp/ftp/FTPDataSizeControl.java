package edu.xmh.p2p.data.business.ftp.ftp;

/*
 * 下载数据的控
 */
public class FTPDataSizeControl {
	private long size = 0;

	public FTPDataSizeControl() {

	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public synchronized void sizeAdd(long readLength) {
		size += readLength;
	}

}

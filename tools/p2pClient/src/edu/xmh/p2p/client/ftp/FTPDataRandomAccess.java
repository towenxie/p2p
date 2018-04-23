package edu.xmh.p2p.client.ftp;

import java.io.*;
/**
 *文件的整合（写操作）
 */
public class FTPDataRandomAccess implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private RandomAccessFile randomAccessFile;
	private long startByte;
	
	public long getStartByte() {
		return startByte;
	}

	public void setStartByte(long startByte) {
		this.startByte = startByte;
	}

	public FTPDataRandomAccess(String localFile, long startByte) throws IOException {
		randomAccessFile = new RandomAccessFile(localFile, "rw");
		this.setStartByte(startByte);
		randomAccessFile.seek(startByte);		
	}

	//文件的拼接（按起始位置）
	public void write(byte[] b, int start, int readLength) {
		try {
			randomAccessFile.write(b, start, readLength);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			randomAccessFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

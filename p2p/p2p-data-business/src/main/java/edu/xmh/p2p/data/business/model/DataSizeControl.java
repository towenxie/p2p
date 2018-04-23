package edu.xmh.p2p.data.business.model;
/*
 * 下载数据的控制
 */
public class DataSizeControl {
	private long size = 0;
	
	public DataSizeControl(){
		
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public synchronized void sizeAdd(long readLength){
		size += readLength;
	}

}

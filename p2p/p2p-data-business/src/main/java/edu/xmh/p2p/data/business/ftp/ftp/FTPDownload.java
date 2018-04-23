package edu.xmh.p2p.data.business.ftp.ftp;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTPDownload extends Thread {
    private static final Logger logger = LogManager.getLogger(FTPDownload.class);
	private static int blockSize = 1024 * 1024 * 5;
	private long start;
	private long end;
	public double speed = 0; // +"M/s"
	public FTPFTP ftp;
	public boolean downloadOver = false;
	public boolean threadDead = false;
	private boolean stop = false;
	private FTPDataRandomAccess dataRandomAccess;
	private FTPDataSizeControl sizeControl;
	private long startTime;
	private long datasize;

	public FTPDownload(FTPFTP ftp) {
		this.ftp = ftp;
	}

	public FTPDownload(FTPFTP ftp, long startByte, long endByte,
			FTPDataSizeControl sizeControl) throws IOException {
		this.ftp = ftp;
		this.start = startByte;
		this.end = endByte;
		this.sizeControl = sizeControl;
		this.dataRandomAccess = new FTPDataRandomAccess(ftp.getLocalPath()
				+ ftp.getDataName(), startByte);
	}

	/**
	 * 判断是否连接到ftp服务
	 * 
	 * @param f
	 * @param ftpClient
	 * @return
	 * @throws Exception
	 */
	public boolean connectFTP(FTPClient ftpClient) throws Exception {
		boolean flag = false;
		int reply;
		if (ftp.getPort() == null) {
			ftpClient.connect(ftp.getIpAddr(), 21);
		} else {
			ftpClient.connect(ftp.getIpAddr(), ftp.getPort());
		}

		ftpClient.login(ftp.getUserName(), ftp.getPwd()); // 登陆ftp 服务
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
			return flag;
		}
		ftpClient.changeWorkingDirectory(ftp.getPath()); // 转移到FTP服务器目
		flag = true;
		return flag;
	}

	/**
	 * 关闭ftp服务
	 * 
	 * @param ftpClient
	 */
	public void closeFtp(FTPClient ftpClient) {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 利用ftp协议进行下载
	 * 
	 * @param f
	 * @param ftpClient
	 * @param startByte
	 * @param endByte
	 * @throws Exception
	 */
	public void startDown(FTPClient ftpClient) throws Exception {
		if (connectFTP(ftpClient)) {
			System.out.println(ftp.getIpAddr() + "连接成功,开始下载");
			try {
				FTPFile[] files = null;
				// 转移到FTP服务器目
				boolean changedir = ftpClient
						.changeWorkingDirectory(new String(ftp.getPath()
								.getBytes("UTF-8"), "iso-8859-1"));

				if (changedir) {
					ftpClient.setControlEncoding("UTF-8");
					files = ftpClient.listFiles();
					logger.info("files.length---" + files.length);
					for (int i = 0; i < files.length; i++) {
						// 0627
						datasize = files[i].getSize();
						try {
							if (files[i].getName().equals(ftp.getDataName())) {
								logger.info(files[i].getName() + "\t"
										+ datasize); // 记录下载的文件名
								startTime = System.currentTimeMillis();
								downloadFile(ftpClient);
								break;
							}
						} catch (Exception e) {
							threadDead = true;
							logger.error("下载异常");
						}
					}
				}
			} catch (Exception e) {
				threadDead = true;
				logger.error("startDown异常");
			}
		} else {
			threadDead = true;
			logger.error("连接FTP失败");
		}
	}

	// 进行断点续传 ，停止下
	public void stopDown() {
		stop = true;
	}

	/**
	 * 上述注释程序之所以出现问题，是因为多写了个os流，os = new FileOutputStream(localFile);
	 * 该流与dataRandomAccess指向的是同一个本地文件localFile
	 * 以，当我们dataRandomAccess.write(...)结束后，os.flush()继续将空字节写入了文件，
	 * 导致的结果就是其他线程写入的数据（从接头处）被其覆盖 但问题是：os是空流，os.flush()就能写入
	 */
	private void downloadFile(FTPClient ftpClient) {
		String remotePath = ftp.getPath() + "/" + ftp.getDataName(); // FTP服务器上的相对路
																		// （根目录
		try {
			ftpClient.setRestartOffset(start);
			InputStream is = ftpClient.retrieveFileStream(new String(remotePath
					.getBytes("UTF-8"), "iso-8859-1"));
			byte[] b = new byte[blockSize];
			int readLength;
			long mStartTime = System.currentTimeMillis();
			long mEndTime = 0;
			while ((readLength = is.read(b)) > 0 && start < end && !stop) {
				mStartTime = System.currentTimeMillis();
				start += readLength;
				dataRandomAccess.write(b, 0, readLength);
				sizeControl.sizeAdd(readLength);
				mEndTime = System.currentTimeMillis();
				if (((mEndTime - mStartTime) / 1000.0) != 0) {
					speed = ((readLength / (1024.0 * 1024)) / ((mEndTime - mStartTime) / 1000.0)); // m/s
				}
			}

			is.close();
			dataRandomAccess.close();
			downloadOver = true;
			logger.info(ftp.getIpAddr() + "下载完成");
			long endTime = System.currentTimeMillis(); // 获取结束时间
			logger.info("从" + ftp.getIpAddr() + "下载完成所用的时间： "
					+ (endTime - startTime) / 1000.0 + "s");
			logger.info("从" + ftp.getIpAddr() + "下载的速度为： " + datasize
					/ (1024.0 * 1024) / (endTime - startTime) / 1000.0 + "m/s");
			double temp = ((sizeControl.getSize() / (1024.0 * 1024)) / ((endTime - startTime) / 1000.0));
			if (temp != 0) {
				// speed = temp;
			}
		} catch (Exception e) {
			logger.error("下载流异常！" + e);
		}
	}

	public void run() {
		FTPClient ftpClient = new FTPClient();
		try {
			connectFTP(ftpClient); // 判断是否连接到ftp服务
			startDown(ftpClient);
			closeFtp(ftpClient);
		} catch (Exception e) {
			threadDead = true;
		}
	}

	public long getFileSize() {
		long fileSize = 0;
		FTPClient ftpClient = new FTPClient();
		try {
			fileSize = startGetFileSize(ftpClient);
			closeFtp(ftpClient);
		} catch (Exception e) {
		}

		return fileSize;
	}

	public long startGetFileSize(FTPClient ftpClient) throws Exception {
		if (connectFTP(ftpClient)) {
			System.out.println(ftp.getIpAddr() + "连接成功,开始下载");
			try {
				FTPFile[] files = null;
				// 转移到FTP服务器目
				boolean changedir = ftpClient
						.changeWorkingDirectory(new String(ftp.getPath()
								.getBytes("UTF-8"), "iso-8859-1"));

				if (changedir) {
					ftpClient.setControlEncoding("UTF-8");
					files = ftpClient.listFiles();
					logger.info("files.length---" + files.length);
					for (int i = 0; i < files.length; i++) {
						// 0627
						datasize = files[i].getSize();
						try {
							if (files[i].getName().equals(ftp.getDataName())) {
								logger.info(files[i].getName() + "\t"
										+ datasize); // 记录下载的文件名
								return datasize;
							}
						} catch (Exception e) {
							threadDead = true;
							logger.error("下载异常");
						}
					}
				}
			} catch (Exception e) {
				logger.error("startDown异常");
			}
		} else {
			logger.error("连接FTP失败");
		}

		return 0;
	}
}

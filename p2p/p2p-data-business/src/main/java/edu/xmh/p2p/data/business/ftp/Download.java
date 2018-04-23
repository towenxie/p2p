package edu.xmh.p2p.data.business.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.xmh.p2p.data.business.model.DataRandomAccess;
import edu.xmh.p2p.data.business.model.DataSizeControl;
import edu.xmh.p2p.data.business.model.FTP;

public class Download extends Thread {
    private static final Logger logger = LogManager.getLogger(Download.class);
    public long start;
    public long end;
    public boolean downloadOver = false;
    public boolean threadDead = false;

    private String IP; // FTP服务器hostname
    private String username; // FTP登录账号 s
    private String password; // FTP登录密码
    private String relativePath;// FTP服务器上的相对路径
    private String filename; // 要下载的文件名

    private DataRandomAccess dataRandomAccess;
    private DataSizeControl sizeControl;
    private boolean stop = false;

    private static int blockSize = 1024;
    SimpleDateFormat df;

    public Download(String IP, String username, String password, String localPath, String relativePath,
            String filename, long startByte, long endByte, DataSizeControl sizeControl) throws IOException {
        this.IP = IP; // FTP服务器hostname
        this.username = username;
        this.password = password;
        this.relativePath = relativePath;
        this.filename = filename;
        this.start = startByte;
        this.end = endByte;
        this.sizeControl = sizeControl;
        this.dataRandomAccess = new DataRandomAccess(localPath + filename, startByte);
    }

    /**
     * 判断是否连接到ftp服务器
     * 
     * @param f
     * @param ftpClient
     * @return
     * @throws Exception
     */
    public boolean connectFTP(FTP f, FTPClient ftpClient) throws Exception {
        boolean flag = false;
        int reply;
        if (f.getPort() == null) {
            ftpClient.connect(f.getIpAddr(), 21);
        } else {
            ftpClient.connect(f.getIpAddr(), f.getPort());
        }

        ftpClient.login(f.getUserName(), f.getPwd()); // 登陆ftp 服务器
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            return flag;
        }
        ftpClient.changeWorkingDirectory(f.getPath()); // 转移到FTP服务器目录
        flag = true;
        return flag;
    }

    /**
     * 关闭ftp服务器
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
    public void startDown(FTP f, FTPClient ftpClient) throws Exception {
        if (connectFTP(f, ftpClient)) {
            System.out.println(IP + "连接成功,开始下载");
            try {
                FTPFile[] files = null;
                // 转移到FTP服务器目录
                boolean changedir =
                        ftpClient.changeWorkingDirectory(new String(relativePath.getBytes("UTF-8"), "iso-8859-1"));

                if (changedir) {
                    ftpClient.setControlEncoding("UTF-8");
                    files = ftpClient.listFiles();
                   logger.info("files.length---" + files.length);
                    for (int i = 0; i < files.length; i++) {
                        try {
                           logger.info(files[i].getName()); // 记录下载的文件名
                            if (files[i].getName().equals(filename)) {
                                downloadFile(ftpClient);
                                break;
                            }
                        } catch (Exception e) {
                            threadDead = true;
                            logger.error("下载异常！");
                        }
                    }
                }
            } catch (Exception e) {
                threadDead = true;
                logger.error("startDown异常！");
            }
        } else {
            threadDead = true;
            logger.error("连接FTP失败！");
        }
    }

    // 进行断点续传 ，停止下载
    public void stopDown() {
        stop = true;
    }

    /**
     * 上述注释程序之所以出现问题，是因为多写了一个os流，os = new FileOutputStream(localFile); 该流与dataRandomAccess指向的是同一个本地文件localFile。
     * 所以，当我们dataRandomAccess.write(...)结束后，os.flush()继续将空字节写入了文件，导致的结果就是其他线程写入的数据（从接头处）被其覆盖。 但问题是：os是空流，os.flush()就能写入
     */
    private void downloadFile(FTPClient ftpClient) {
        String remotePath = relativePath + "/" + filename; // FTP服务器上的相对路径 （根目录）
        try {
            ftpClient.setRestartOffset(start);
            InputStream is = ftpClient.retrieveFileStream(new String(remotePath.getBytes("UTF-8"), "iso-8859-1"));
            byte[] b = new byte[blockSize];
            int readLength;
            while ((readLength = is.read(b)) > 0 && start < end && !stop) {
                dataRandomAccess.write(b, 0, readLength);
                start += readLength;
                sizeControl.sizeAdd(readLength);
            }
            is.close();
            dataRandomAccess.close();
            downloadOver = true;
           logger.info(IP + "下载完成！");
            // long endTime = System.currentTimeMillis(); // 获取结束时间
            // DownloadUtil.infoLog("从" + IP + "下载完成所用的时间： " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            logger.error("下载流异常！");
        }
    }

    public void run() {
        FTP f = new FTP();
        FTPClient ftpClient = new FTPClient();
        f.setIpAddr(IP);
        f.setUserName(username);
        f.setPwd(password);
        try {
            // df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            // startTime = System.currentTimeMillis(); //获取开始时间
            connectFTP(f, ftpClient); // 判断是否连接到ftp服务器
            startDown(f, ftpClient);
            closeFtp(ftpClient);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            threadDead = true;
        }
    }

    public String getIP() {
        return IP;
    }

    public static void main(String[] args) {
        try {
            new Download("125.219.39.150", "wl", "123", "e:/", "/", "a.zip", 256, 4096, new DataSizeControl()).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

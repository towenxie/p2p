package edu.xmh.p2p.data.business.ftp.ftp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * 实现多线程下
 */
public class FTPDispatcherApp extends Thread {
    private static final Logger logger = LogManager.getLogger(FTPDispatcherApp.class);
    public double speed = 0; // +"M/s"
    private String localPath = "e://";
    private String dataName = "";
    private List<FTPFTP> ftps = new ArrayList<FTPFTP>();
    private static final String NOT_EXIST_DATA = "does not exist BT data";
    private FTPDataSizeControl dataSizeControl = new FTPDataSizeControl();
    private FTPDownload[] downloadThreads;
    private long dataSize;
    // private int siteNum = 0; // 节点的数
    // 断点续传相关参数
    private boolean first = true;
    public boolean stop = false;
    public boolean downOver = false;
    private File tmpFile;
    private RandomAccessFile randomFile;
    private FTPDataPatchHelper helper = new FTPDataPatchHelper();

    public FTPDispatcherApp(List<String> ftpUrls, String path) {
        this.ftps.clear();
        if (!path.isEmpty()) {
            this.localPath = path;
        }
        for (String ftpUrl : ftpUrls) {
            String[] data = strParse(ftpUrl);
            if (data.length < 5) {
                continue;
            }
            FTPFTP ftp = new FTPFTP();
            ftp.setIpAddr(data[0]);
            ftp.setUserName(data[1]);
            ftp.setPwd(data[2]);
            ftp.setPath(data[3]);
            ftp.setDataName(data[4]);
            ftp.setLocalPath(localPath);
            ftps.add(ftp);
            dataName = data[4];
        }

        this.tmpFile = new File(this.localPath + dataName + ".info");
        if (tmpFile.exists()) {
            first = false;
        }
        // RandomAccessFile提供对文件的读写功能
        try {
            this.randomFile = new RandomAccessFile(this.localPath + dataName + ".info", "rw");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.dataSize = getFileSizeByFTP();
    }

    // 0627
    public long getFileSizeByFTP() {
        long fileSize = 0;
        for (FTPFTP ftp : ftps) {
            FTPDownload thread = new FTPDownload(ftp);
            fileSize = thread.getFileSize();
            if (fileSize > 0) {
                break;
            }
        }

        return fileSize;
    }

    /**
     * 读取服务节点的个数并判断文件是否是第次下,如果是第次下载返回OK；如果不是第次啊下载返回RESUME
     * 
     * @return
     */
    public String checkResource() {
        if (ftps.size() > 0) {
            if (!first) { // 如果不是第一次下
                return "RESUME";
            } else {
                return "OK";
            }
        } else {
            return NOT_EXIST_DATA;
        }
    }

    /**
     * 写入断点（循环写入数据快的起始位置终止位置和数据快的状?
     */
    public void writeBreakpoint() {
        try {
            randomFile.seek(0);
            randomFile.writeInt(helper.dataPatchs.length);
            logger.info("---writeBreakpoint---");
            for (FTPDataPatchModel dataPatchModel : helper.dataPatchs) {
                randomFile.writeLong(dataPatchModel.getStartByte());
                randomFile.writeLong(dataPatchModel.getEndByte());
                randomFile.writeInt(dataPatchModel.getStatus());
            }
            randomFile.writeLong(dataSizeControl.getSize());
            Thread.sleep(1000);
        } catch (IOException e) {
            logger.error("写断点错误！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取断点（循环读取数据块的起始终止位置和数据块的状） 如果当时中断的时候，数据块已下完，则标记2，从下一个数据块始下载；如果中断时数据块没有下载完，则标记为0，重新下
     */
    public void readBreakpoint() {
        try {
            DataInputStream is = new DataInputStream(new FileInputStream(tmpFile));
            int patchNum = is.readInt(); // 数据块的个数
            helper.dataPatchs = new FTPDataPatchModel[patchNum];
            logger.info(tmpFile.getPath() + "---patchNum---" + patchNum);
            for (int i = 0; i < patchNum; i++) {
                FTPDataPatchModel patch = new FTPDataPatchModel();
                patch.setNumber(i);
                patch.setStartByte(is.readLong());
                patch.setEndByte(is.readLong());
                patch.setStatus(is.readInt() == 2 ? 2 : 0);
                logger.info("数据" + i + "---startByte---" + patch.getStartByte() + "---endByte---" + patch.getEndByte());
                helper.dataPatchs[i] = patch;
            }
            dataSizeControl.setSize(is.readLong());
            is.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            logger.info("临时文件不存在！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("读断点错误！");
        }

    }

    /**
     * 遍历服务节点，并为每个节点，分配个下载任
     * 
     * @throws DocumentException
     */
    public void startDownload() {
        downloadThreads = new FTPDownload[ftps.size()];

        for (int i = 0; i < ftps.size(); i++) {
            if (i > helper.dataPatchs.length - 1) {
                break;
            }
            try {
                helper.dataPatchs[i].setSiteIp(ftps.get(i).getIpAddr());
                helper.dataPatchs[i].setThreadNumber(i);
                helper.dataPatchs[i].setStatus(1);
                downloadThreads[i] = new FTPDownload(ftps.get(i), helper.dataPatchs[i].getStartByte(),
                        helper.dataPatchs[i].getEndByte(), dataSizeControl);
                logger.info("线程" + i + "开始始启动！");
                downloadThreads[i].start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error("启动下载线程异常！");
            }
        }
    }

    /**
     * 停止下载
     */
    public void stopDownload(boolean isDelete) {
        stop = true;
        for (int i = 0; i < ftps.size(); i++) {
            if (downloadThreads[i] != null) {
                downloadThreads[i].stopDown();
            }
        }

        if (isDelete) { // 删除文件
            tmpFile.delete();
            // downloadModel.setIsDelete(isDelete);
            try {
                randomFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (tmpFile.exists() && tmpFile.isFile()) {
                logger.info("tmpFile文件删除失败！！");
            }
        } else { // 暂停
            writeBreakpoint(); // 写入断点
            try {
                randomFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean haveThreads() {
        boolean result = false;
        for (FTPDownload download : downloadThreads) {
            if (download != null) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void run() {
        String checkResource = checkResource(); // 查文件是否是第一次下载并获取服务节点的个
        if (checkResource.equals("RESUME")) {
            logger.info("读取断点");
            readBreakpoint(); // 读取断点
        } else if (checkResource.equals("OK")) {
            helper.divideData(dataSize); // 拆分数据
        } else {
            logger.info(NOT_EXIST_DATA);
        }

        long startTime = System.currentTimeMillis(); // 获取始时
        startDownload(); // 始下
        /**
         * 1.完善下载完毕进程 2.删除本地临时文件
         */
        while (!stop) {
            writeBreakpoint(); // 写入断点
            for (int i = 0; i < ftps.size(); i++) {

                if (downloadThreads[i] == null) {
                    continue;
                }
                if (downloadThreads[i].threadDead) { // 表示线程死掉了，但是数据没有下载完毕
                    logger.info("节点" + downloadThreads[i].ftp.getIpAddr() + "下载失败，将重新分配节点");
                    helper.changePatchStatus(downloadThreads[i].ftp.getIpAddr(), i, 0); // 重新标记该条数据状为未下
                    downloadThreads[i] = null;
                } else if (!downloadThreads[i].downloadOver) { // 判断是否下载完毕
                    logger.info("节点" + downloadThreads[i].ftp.getIpAddr() + " 正在下载");
                } else { // 某一块下载完
                    helper.changePatchStatus(downloadThreads[i].ftp.getIpAddr(), i, 2); // 表示该数据已经下载完
                    FTPDataPatchModel dataPatchModel = helper.pickUpPatch(); // 从数组里面取出一条新的数据块进行下载
                    writeBreakpoint(); // 写入断点
                    if (dataPatchModel != null) {
                        try {
                            dataPatchModel.setSiteIp(ftps.get(i).getIpAddr());
                            dataPatchModel.setThreadNumber(i);
                            downloadThreads[i] = new FTPDownload(ftps.get(i), dataPatchModel.getStartByte(),
                                    dataPatchModel.getEndByte(), dataSizeControl);
                            downloadThreads[i].start(); // FTP下载
                        } catch (IOException e) {
                            logger.error("启动下载线程异常");
                        }
                    }
                    logger.info("下载进度" + helper.getCompleteCount() + "/" + helper.dataPatchs.length + " 正在下载");
                }
            }
            if (!haveThreads()) {
                break;
            }
            // 遍历数据块dataPatchs数组里面的数据是否全部下载完
            if (helper.checkCompleteStatus()) {
                try {
                    randomFile.close();
                    long endTime = System.currentTimeMillis(); // 获取结束时间
                    logger.info("下载完成总用的时间 " + (endTime - startTime) + "ms");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tmpFile.delete();
                if (tmpFile.exists() && tmpFile.isFile()) {
                    logger.info("tmpFile文件删除失败");
                }
                downOver = true;
                logger.info("删除临时文件");
                break;
            } else {
                logger.info("正在下载");
            }
        }
    }

    // 0701
    // 解析字符 获取ip,用户名，密码
    public static String[] strParse(String str) {
        String[] s = new String[5];
        String a = str.substring(0, str.lastIndexOf("//"));
        System.out.println(a);
        String b = str.substring(str.indexOf("//") + 2, str.indexOf("@"));
        System.out.println(b);
        // s[0]=str.substring(0,
        // str.indexOf("//")+2)+a.substring(a.indexOf("@")+1); //ip ftp://
        s[0] = a.substring(a.indexOf("@") + 1);
        s[1] = b.substring(0, b.indexOf(":")); // 用户
        s[2] = b.substring(b.indexOf(":") + 1); // 密码
        s[3] = "/" + str.substring(str.lastIndexOf("//") + 2, str.indexOf("#")); // 相对路径
        s[4] = str.substring(str.lastIndexOf('#') + 1); // 文件
        return s;
    }

    // 0703获取实时下载速度
    public String getSpeed() {
        double evSpeed = 0;
        if (downloadThreads == null || downloadThreads.length == 0) {
            return "-m/s";
        }
        int index = 0;
        for (FTPDownload download : downloadThreads) {
            if (download != null) {
                if (download.speed != 0) {
                    evSpeed += download.speed;
                    index++;
                }
            }
        }
        if (index == 0) {
            return "-m/s";
        }
        speed = evSpeed / index;
        return speed + "m/s";
    }

    public static void downloadForWeb(List<String> ftpUrls, String localPath) {
        FTPDispatcherApp dispatcher = new FTPDispatcherApp(ftpUrls, localPath);
        dispatcher.start();

        while (!dispatcher.downOver) {
            String speed = dispatcher.getSpeed();
            System.out.println(speed);
        }
    }

    public static void multipleDownload(List<String> ftpFileUrls, String localPath) {

        for (String ftpFileUrl : ftpFileUrls) {
            List<String> ftpUrls = new ArrayList<String>();
            ftpUrls.add(ftpFileUrl);
            FTPDispatcherApp.downloadForWeb(ftpUrls, localPath);
        }
    }

    public static boolean checkFileExists(String ftpFileUrl, String localPath) {

        String[] data = FTPDispatcherApp.strParse(ftpFileUrl);
        if (data.length < 5) {
            return false;
        }
        String fileName = data[4];
        File file = new File(localPath + fileName);
        if (file.exists()) {
            return true;
        } else {
            List<String> ftpUrls = new ArrayList<String>();
            ftpUrls.add(ftpFileUrl);
            FTPDispatcherApp.downloadForWeb(ftpUrls, localPath);

            return false;
        }
    }

    public static void main(String[] args) {
        String localPath = "e://";
        String ftpUrl =
                "ftp://jcgx_241:jcgx_241@10.12.52.241//site_yx/1010020002/20160628105922/0001-EVDB-32-4#GF1_WFV3_E117.0_N38.9_20160324_L1A0001485797.tar.gz";
        List<String> ftpUrls = new ArrayList<String>();
        ftpUrls.add(ftpUrl);
        FTPDispatcherApp.downloadForWeb(ftpUrls, localPath);
    }
}

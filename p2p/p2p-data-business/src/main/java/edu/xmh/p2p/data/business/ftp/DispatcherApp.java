package edu.xmh.p2p.data.business.ftp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import edu.xmh.p2p.data.access.model.FileInfoEntity;
import edu.xmh.p2p.data.business.helper.*;
import edu.xmh.p2p.data.business.model.*;
import edu.xmh.p2p.data.business.view.*;

/*
 * 实现多线程下载
 */
public class DispatcherApp extends Thread {

    private static final Logger logger = LogManager.getLogger(DispatcherApp.class);
    private static final String NOT_EXIST_DATA = "does not exist BT data";
    public static final String loaclPath = "e://";
    private Element rootElement;
    private List<Node> list;
    private DataSizeControl dataSizeControl;
    private Download[] downloadThreads;
    public File file = null;
    public String fileName = null;
    public String dataName;
    private long dataSize;
    private int siteNum = 0; // 节点的数目
    // 断点续传相关参数
    private boolean first = true;
    public boolean stop = false;
    public boolean downOver = false;
    private File tmpFile;
    private RandomAccessFile randomFile;
    private DataPatchHelper helper = new DataPatchHelper();
    public DownloadModel downloadModel = new DownloadModel();

    public DispatcherApp(Document configDoc) {
        try {
            Document conditionDocument = configDoc;
            this.rootElement = conditionDocument.getRootElement();
            this.dataSize = Long.parseLong(rootElement.elementText("dataSize"));
            this.dataName = rootElement.elementText("dataName");
            this.list = (List<Node>) rootElement.selectNodes("//siteFile");
            this.dataSizeControl = new DataSizeControl();
            this.tmpFile = new File(loaclPath + dataName + ".info");
            if (tmpFile.exists()) {
                first = false;
            }
            // RandomAccessFile提供对文件的读写功能
            this.randomFile = new RandomAccessFile(loaclPath + dataName + ".info", "rw");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 读取服务节点的个数并判断文件是否是第一次下载,如果是第一次下载返回OK；如果不是第一次啊下载返回RESUME
     * 
     * @return
     */
    public String checkResource() {

        for (Node node : list) {
            String absolutePath = node.valueOf("absolutePath");
            if (absolutePath != null && !absolutePath.isEmpty())
                siteNum++;
            System.out.println(node.asXML());
        }

        if (siteNum > 0) {
            if (!first) { // 如果不是第一次下载
                return "RESUME";
            } else {
                return "OK";
            }
        } else {
            return NOT_EXIST_DATA;
        }
    }

    /**
     * 写入断点（循环写入数据快的起始位置、终止位置和数据快的状态 ）
     */
    public void writeBreakpoint() {
        try {
            randomFile.seek(0);
            randomFile.writeInt(helper.dataPatchs.length);
            logger.info("---writeBreakpoint---");
            for (DataPatchModel dataPatchModel : helper.dataPatchs) {
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
     * 读取断点（循环读取数据块的起始、终止位置和数据块的状态） 如果当时中断的时候，数据块已下完，则标记为2，从下一个数据块开始下载；如果中断时数据块没有下载完，则标记为0，重新下载
     */
    public void readBreakpoint() {
        try {
            DataInputStream is = new DataInputStream(new FileInputStream(tmpFile));
            int patchNum = is.readInt(); // 数据块的个数
            helper.dataPatchs = new DataPatchModel[patchNum];
            logger.info(tmpFile.getPath() + "---patchNum---" + patchNum);
            for (int i = 0; i < patchNum; i++) {
                DataPatchModel patch = new DataPatchModel();
                patch.setNumber(i);
                patch.setStartByte(is.readLong());
                patch.setEndByte(is.readLong());
                patch.setStatus(is.readInt() == 2 ? 2 : 0);
                logger.info("数据块" + i + "---startByte---" + patch.getStartByte() + "---endByte---"
                        + patch.getEndByte());
                helper.dataPatchs[i] = patch;
            }
            dataSizeControl.setSize(is.readLong());
            is.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("临时文件不存在！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("读断点错误！");
        }

    }

    /**
     * 遍历服务节点，并为每一个节点，分配一个下载任务
     * 
     * @throws DocumentException
     */
    public void startDownload() throws DocumentException {
        downloadThreads = new Download[siteNum];
        for (int i = 0; i < siteNum; i++) {
            if (i > helper.dataPatchs.length - 1) {
                break;
            }
            try {
                helper.dataPatchs[i].setSiteIp(list.get(i).valueOf("siteIp"));
                helper.dataPatchs[i].setThreadNumber(i);
                helper.dataPatchs[i].setStatus(1);
                downloadThreads[i] =
                        new Download(list.get(i).valueOf("siteIp"), "wl", "123", loaclPath, "/", dataName,
                                helper.dataPatchs[i].getStartByte(), helper.dataPatchs[i].getEndByte(), dataSizeControl);
                logger.info("线程" + i + "开始启动！");
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
        for (int i = 0; i < siteNum; i++) {
            if (downloadThreads[i] != null) {
                downloadThreads[i].stopDown();
            }
        }

        if (isDelete) { // 删除文件
        // tmpFile.deleteOnExit();
            downloadModel.setIsDelete(isDelete);
            try {
                randomFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // 暂停
            downloadModel.setIsPause(true);
            downloadModel.setStatus("已经暂停");
            TableView.DownloadModels.add(downloadModel);
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
        for (Download download : downloadThreads) {
            if (download != null) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void run() {
        try {
            String checkResource = checkResource(); // 检查文件是否是第一次下载并获取服务节点的个数
            if (checkResource.equals("RESUME")) {
               logger.info("读取断点！");
                readBreakpoint(); // 读取断点
            } else if (checkResource.equals("OK")) {
                helper.divideData(dataSize); // 拆分数据
            } else {
               logger.info(NOT_EXIST_DATA);
            }
            // 表格中的设置
            downloadModel.setName(dataName);
            downloadModel.setStatus("开始下载");
            // 下载的进度： 已下载的块数/总的数据块数
            downloadModel.setProgress(helper.getCompleteCount() + "/" + helper.dataPatchs.length);
            TableView.DownloadModels.add(downloadModel);

            long startTime = System.currentTimeMillis(); // 获取开始时间

            startDownload(); // 开始下载
            /**
             * 1.完善下载完毕进程 2.删除本地临时文件
             */
            while (!stop) {
                writeBreakpoint(); // 写入断点
                for (int i = 0; i < siteNum; i++) {

                    if (downloadThreads[i] == null) {
                        continue;
                    }
                    if (downloadThreads[i].threadDead) { // 表示线程死掉了，但是数据没有下载完毕
                       logger.info("节点【" + downloadThreads[i].getIP() + "】 下载失败，将重新分配节点！");
                        helper.changePatchStatus(downloadThreads[i].getIP(), i, 0); // 重新标记该条数据状态为未下载
                        downloadThreads[i] = null;
                    } else if (!downloadThreads[i].downloadOver) { // 判断是否下载完毕
                       logger.info("节点【" + downloadThreads[i].getIP() + "】 正在下载！");
                    } else { // 某一块下载完毕
                        helper.changePatchStatus(downloadThreads[i].getIP(), i, 2); // 表示该数据已经下载完毕
                        DataPatchModel dataPatchModel = helper.pickUpPatch(); // 从数组里面取出一条新的数据块进行下载
                        writeBreakpoint(); // 写入断点
                        if (dataPatchModel != null) {
                            try {
                                dataPatchModel.setSiteIp(list.get(i).valueOf("siteIp"));
                                dataPatchModel.setThreadNumber(i);
                                downloadThreads[i] =
                                        new Download(list.get(i).valueOf("siteIp"), "wl", "123", loaclPath, "/",
                                                dataName, dataPatchModel.getStartByte(), dataPatchModel.getEndByte(),
                                                dataSizeControl);
                                downloadThreads[i].start(); // FTP下载
                            } catch (IOException e) {
                                logger.error("启动下载线程异常！");
                            }
                        }
                       logger.info("下载进度【" + helper.getCompleteCount() + "/" + helper.dataPatchs.length
                                + "】 正在下载！");
                        downloadModel.setProgress(helper.getCompleteCount() + "/" + helper.dataPatchs.length);
                        downloadModel.setStatus("正在下载");
                        TableView.DownloadModels.add(downloadModel);
                    }
                }
                if (!haveThreads()) {
                    downloadModel.setStatus("下载失败");
                    TableView.DownloadModels.add(downloadModel);
                    break;
                }
                // 遍历数据块dataPatchs数组里面的数据是否全部下载完毕
                if (helper.checkCompleteStatus()) {
                    try {
                        randomFile.close();
                        long endTime = System.currentTimeMillis(); // 获取结束时间
                       logger.info("下载完成所用的时间： " + (endTime - startTime) + "ms");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tmpFile.delete();
                    if (tmpFile.exists() && tmpFile.isFile()) {
                       logger.info("tmpFile文件删除失败！");
                    }
                    downOver = true;
                   logger.info("删除临时文件！");
                    downloadModel.setStatus("下载完成");
                    downloadModel.setIsFinished(true);
                    TableView.DownloadModels.add(downloadModel);
                    break;
                } else {
                    // DownloadUtil.infoLog("下载失败，请检查网络，或服务器问题。");
                    downloadModel.setStatus("正在下载");
                    TableView.DownloadModels.add(downloadModel);
                }

            }
        } catch (DocumentException e) {
            logger.error("启动下载异常！");
            downloadModel.setStatus("下载失败");
            TableView.DownloadModels.add(downloadModel);
        }
    }


    // 从配置文件中读取要下载文件的信息，并进行下载
    public static DispatcherApp appService(File file) {
        // String file = "config.xml";
        SAXReader reader = new SAXReader();
        Document configDoc = null;
        DispatcherApp dispatcher = null;
        try {
            configDoc = reader.read(file);
        } catch (DocumentException e) {
           logger.info("加载xml文件失败。");
            e.printStackTrace();
        } // 加载xml文件
        if (configDoc != null) {
            dispatcher = new DispatcherApp(configDoc);
            dispatcher.file = file;
            dispatcher.start();
        }
        return dispatcher;
    }

    // 从配置文件中读取要下载文件的信息，并进行下载
    public static DispatcherApp appServiceFormDB(FileInfoEntity filenInfo) {
        Document configDoc = null;
        DispatcherApp dispatcher = null;
        try {
            configDoc = DocumentHelper.parseText(filenInfo.getFileContent());
        } catch (DocumentException e) {
           logger.info("加载xml文件失败。");
            e.printStackTrace();
        } // 加载xml文件
        if (configDoc != null) {
            dispatcher = new DispatcherApp(configDoc);
            dispatcher.fileName = filenInfo.getFileName();
            dispatcher.start();
        }
        return dispatcher;
    }
}

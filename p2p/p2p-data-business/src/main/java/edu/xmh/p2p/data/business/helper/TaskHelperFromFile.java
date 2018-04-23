package edu.xmh.p2p.data.business.helper;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.xmh.p2p.data.business.constant.P2PConstants;
import edu.xmh.p2p.data.business.ftp.DispatcherApp;

/**
 * 下载任务设置
 * 
 * @author Xiongminghao
 *
 */
public class TaskHelperFromFile {
    private static final Logger logger = LogManager.getLogger(TaskHelperFromDB.class);
    private final static int taskNum = P2PConstants.appConfig.getDownloadSize(); // 最大下载队列数
    public static DispatcherApp[] tasks = new DispatcherApp[taskNum]; // 下载队列

    // 新建下载任务，开始下载
    public static void createTask(File file) {
        DispatcherApp dispatcher = DispatcherApp.appService(file);
        TaskHelperFromFile.append(dispatcher);
    }

    /**
     * 1可以下载，2表示已存在，3表示已达到最大数
     * 
     * @return
     */
    public static int canAddTask(String fileName) {
        int result = 3;
        for (int i = 0; i < taskNum; i++) {
            if (tasks[i] == null) {
                result = 1;
            } else if (tasks[i] != null && fileName.equals(tasks[i].file.getName())) {
                result = 2;
                break;
            }
        }
        return result;
    }

    // 下载客户端之前关闭时，没下载完的文件
    public static void setUpAllTasks() {
        // 读取上次没有下载完的文件，并把其存在列表中
        List<String> filelist = FileHelper.readTxtFile();
        for (String fileName : filelist) {
            // 新建下载任务，开始下载
            createTask(new File(fileName));
        }
    }

    // 记录中断的文件
    public static void saveTask() {
        for (int i = 0; i < taskNum; i++) {
            if (tasks[i] != null && tasks[i].downOver) {
                FileHelper.write(tasks[i].file.getAbsolutePath());
            }
        }
    }

    // 添加到下载任务中
    public static void append(DispatcherApp task) {
        for (int i = 0; i < taskNum; i++) {
            if (tasks[i] == null) {
                tasks[i] = task;
                break;
            }
        }
    }

    public static void finished(String name) {
        for (int i = 0; i < taskNum; i++) {
            if (tasks[i] != null && name.equals(tasks[i].dataName) && tasks[i].downOver) {
                tasks[i] = null;
                break;
            }
        }
    }

    // 暂停或者删除文件
    public static void deleteOrPause(String name, boolean isDelete) {
        for (int i = 0; i < taskNum; i++) {
            if (tasks[i] != null && name.equals(tasks[i].dataName) && !tasks[i].downOver) {
                tasks[i].stopDownload(isDelete);
                if (isDelete) {
                    File temFile = new File(tasks[i].loaclPath + tasks[i].dataName + ".info");
                    tasks[i] = null;
                    temFile.delete();

                    if (temFile.exists() && temFile.isFile()) {
                        logger.info(temFile.getName() + "文件删除失败！");
                    }
                }

                break;
            }
        }
    }

    // 继续下载
    public static void continueDownload(String name) {
        for (int i = 0; i < taskNum; i++) {
            if (tasks[i] != null && name.equals(tasks[i].dataName)) {
                if (tasks[i].stop) {
                    DispatcherApp dispatcher = DispatcherApp.appService(tasks[i].file);
                    tasks[i] = dispatcher;
                } else {
                    System.out.println("当前没有暂停，还正在下载");
                }
                break;
            }
        }
    }
}

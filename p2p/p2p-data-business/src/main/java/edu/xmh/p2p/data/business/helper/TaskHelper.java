package edu.xmh.p2p.data.business.helper;

import java.io.File;

import edu.xmh.p2p.data.access.model.FileInfoEntity;
import edu.xmh.p2p.data.business.conf.P2PAppConfig;
import edu.xmh.p2p.data.business.constant.P2PConstants;
import edu.xmh.p2p.data.business.ftp.DispatcherApp;

/**
 * 下载任务设置
 * 
 * @author Xiongminghao
 *
 */
public class TaskHelper {
    private final static int taskNum = 5; // 最大下载队列数
    public static DispatcherApp[] tasks = new DispatcherApp[taskNum]; // 下载队列
    
    // 新建下载任务，开始下载
    public static void createTask(File file, FileInfoEntity filenInfo) {
        if (P2PConstants.appConfig.isFromDB()) {
            TaskHelperFromDB.createTask(filenInfo);
        } else {
            TaskHelperFromFile.createTask(file);
        }
    }

    /**
     * 1可以下载，2表示已存在，3表示已达到最大数
     * 
     * @return
     */
    public static int canAddTask(String fileName) {
        if (P2PConstants.appConfig.isFromDB()) {
            return TaskHelperFromDB.canAddTask(fileName);
        } else {
            return TaskHelperFromFile.canAddTask(fileName);
        }
    }

    // 下载客户端之前关闭时，没下载完的文件
    public static void setUpAllTasks() {
        if (P2PConstants.appConfig.isFromDB()) {
            TaskHelperFromDB.setUpAllTasks();
        } else {
            TaskHelperFromFile.setUpAllTasks();
        }
    }

    // 记录中断的文件
    public static void saveTask() {
        if (P2PConstants.appConfig.isFromDB()) {
            TaskHelperFromDB.saveTask();
        } else {
            TaskHelperFromFile.saveTask();
        }
    }

    // 添加到下载任务中
    public static void append(DispatcherApp task) {
        if (P2PConstants.appConfig.isFromDB()) {
            TaskHelperFromDB.append(task);
        } else {
            TaskHelperFromFile.append(task);
        }
    }

    public static void finished(String name) {
        if (P2PConstants.appConfig.isFromDB()) {
            TaskHelperFromDB.finished(name);
        } else {
            TaskHelperFromFile.finished(name);
        }
    }

    // 暂停或者删除文件
    public static void deleteOrPause(String name, boolean isDelete) {
        if (P2PConstants.appConfig.isFromDB()) {
            TaskHelperFromDB.deleteOrPause(name, isDelete);
        } else {
            TaskHelperFromFile.deleteOrPause(name, isDelete);
        }
    }

    // 继续下载
    public static void continueDownload(String name) {
        if (P2PConstants.appConfig.isFromDB()) {
            TaskHelperFromDB.continueDownload(name);
        } else {
            TaskHelperFromFile.continueDownload(name);
        }
    }
}

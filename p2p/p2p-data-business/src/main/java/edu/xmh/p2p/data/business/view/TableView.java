package edu.xmh.p2p.data.business.view;

import java.util.LinkedList;
import java.util.Vector;

import edu.xmh.p2p.data.business.helper.*;
import edu.xmh.p2p.data.business.model.DownloadModel;

public class TableView {
	public static Vector<String> TableTitles = new Vector<String>();
	// 存放下载过程中的消息
	public static LinkedList<DownloadModel> DownloadModels = new LinkedList<DownloadModel>();
	// 存放客户端的消息（在下方显示部分）
	public static Vector DownloadInfo = new Vector();

	public static void setTitleVector() {
		TableTitles.add("编号");
		TableTitles.add("名称");
		TableTitles.add("进度");
		TableTitles.add("状态");
	}

	// 删除表格中的数据
	public static void deleteTableData(String name) {
		for (Object object : DownloadInfo) {
			Vector item = (Vector) object;

			if (item.contains(name)) {
				DownloadInfo.removeElement(item);

				break;
			}
		}
	}

	// 把接收到的数据绑定到界面上(取数据的时候对这个方法加锁，先把那个列表固定一下)
	public static void updateTableData() {
		synchronized (DownloadModels) {
			while (DownloadModels.size() != 0) {
				DownloadModel model = DownloadModels.removeFirst();
				if (model.getIsFinished()) {
					TaskHelper.finished(model.getName());
				}

				if (model.getIsDelete()) {
					continue;
				}

				boolean flag = false; // flag 标记信息是否存在DownloadInfo列表中
				for (Object object : DownloadInfo) {
					Vector item = (Vector) object;

					if (item.contains(model.getName())) {
						flag = true;
						item.set(2, model.getProgress()); // 进度条
						item.set(3, model.getStatus()); // 状态
						break;
					}
				}

				if (!flag) { // 不存在列表中
					Vector newFile = new Vector();
					newFile.add(DownloadInfo.size() + 1); // 标号
					newFile.add(model.getName()); // 文件名
					newFile.add(model.getProgress()); // 进度条
					newFile.add(model.getStatus()); // 状态

					DownloadInfo.add(newFile); // 有多少个下载文件就在列表中显示
				}
			}
		}
	}
}

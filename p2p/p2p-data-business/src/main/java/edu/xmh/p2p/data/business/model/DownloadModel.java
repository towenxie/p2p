package edu.xmh.p2p.data.business.model;
/**
 * 用于表格中文件下载状态的标记
 * @author Xiongminghao 
 *
 */
public class DownloadModel {

	private String name;   //文件�?
	private String progress;
	private String status;
	private Boolean isPause = false;
	private Boolean isDelete = false;
	private Boolean isFinished = false;

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getStatus() {
		return isPause ? "已经暂停" : status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsPause() {
		return isPause;
	}

	public void setIsPause(Boolean isPause) {
		this.isPause = isPause;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Boolean getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}
}

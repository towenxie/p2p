package edu.xmh.p2p.data.business.helper;

import edu.xmh.p2p.data.business.model.DataPatchModel;

/**
 * 下载过程的相关操作（数据拆分，标记数据块状态等）
 * 
 * @author Xiongminghao
 *
 */
public class DataPatchHelper {
    private static final long PATCH_SIZE = 2048000; // 每一块的大小
    public DataPatchModel[] dataPatchs; // 拆分数据后的 数据块数组

    /**
     * 拆分数据
     */
    public void divideData(long dataSize) {
        int patchNum = (int) (dataSize / PATCH_SIZE);
        patchNum = dataSize % PATCH_SIZE == 0 ? patchNum : patchNum + 1;
        dataPatchs = new DataPatchModel[patchNum];
        long startByte = 0;
        long endByte = 0;
        for (int i = 0; i < patchNum; i++) {
            DataPatchModel patch = new DataPatchModel();
            patch.setNumber(i);
            if (i == 0) {
            } else if ((endByte) < dataSize) {
                startByte = endByte + 1; // 开始位置
            }

            if ((startByte + PATCH_SIZE) < dataSize) {
                endByte = startByte + PATCH_SIZE;
            } else {
                endByte = dataSize;
            }

            patch.setEndByte(endByte);
            patch.setStartByte(startByte);
            dataPatchs[i] = patch;
        }
    }

    /**
     * 遍历dataPatchs数组里面的数据是否全部下载完毕
     * 
     * @return
     */
    public boolean checkCompleteStatus() {
        for (DataPatchModel dataPatchModel : dataPatchs) {
            System.out.println(dataPatchModel.toString());
            if (dataPatchModel.getStatus() != 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取已下载数据块的个数
     * 
     * @return
     */
    public int getCompleteCount() {
        int count = 0;
        for (DataPatchModel dataPatchModel : dataPatchs) {
            System.out.println(dataPatchModel.toString());
            if (dataPatchModel.getStatus() == 2) {
                count++;
            }
        }
        return count;
    }

    /**
     * 更改 DataPatchModel 的数据状态
     * 
     * @param siteIp
     * @param threadNumber 线程号
     * @param status 0 表示setStatus(0)，2 表示setStatus(2)
     */
    public void changePatchStatus(String siteIp, int threadNumber, int status) {
        for (DataPatchModel dataPatchModel : dataPatchs) {
            if (dataPatchModel.getSiteIp().equals(siteIp) && dataPatchModel.getThreadNumber() == threadNumber
                    && dataPatchModel.getStatus() == 1) {
                if (status == 0) {
                    dataPatchModel.setStatus(0);
                    dataPatchModel.setSiteIp("");
                    dataPatchModel.setThreadNumber(0);
                } else if (status == 2) {
                    dataPatchModel.setStatus(2);
                }
                break;
            }
        }
    }

    /**
     * 任意的从数组里面取出 一个未下载的数据
     * 
     * @return
     */
    public DataPatchModel pickUpPatch() {
        for (DataPatchModel dataPatchModel : dataPatchs) {
            if (dataPatchModel.getStatus() == 0) {
                dataPatchModel.setStatus(1);
                return dataPatchModel;
            }
        }
        return null;
    }
}

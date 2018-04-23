package edu.xmh.p2p.client.ftp;

/**
 * 下载过程的相关操作（数据拆分，标记数据块状�?�等�?
 * 
 * @author Xiongminghao
 *
 */
public class FTPDataPatchHelper {
    private static final long PATCH_SIZE = 1024 * 1024 * 20; // 每一块的大小
    public FTPDataPatchModel[] dataPatchs; // 拆分数据后的 数据块数�?

    /**
     * 拆分数据
     */
    public void divideData(long dataSize) {
        int patchNum = (int) (dataSize / PATCH_SIZE);
        patchNum = dataSize % PATCH_SIZE == 0 ? patchNum : patchNum + 1;
        dataPatchs = new FTPDataPatchModel[patchNum];
        long startByte = 0;
        long endByte = 0;
        for (int i = 0; i < patchNum; i++) {
            FTPDataPatchModel patch = new FTPDataPatchModel();
            patch.setNumber(i);
            if (i == 0) {
            } else if ((endByte) < dataSize) {
                startByte = endByte + 1; // �?始位�?
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
     * 遍历dataPatchs数组里面的数据是否全部下载完�?
     * 
     * @return
     */
    public boolean checkCompleteStatus() {
        for (FTPDataPatchModel dataPatchModel : dataPatchs) {
            System.out.println(dataPatchModel.toString());
            if (dataPatchModel.getStatus() != 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取已下载数据块的个�?
     * 
     * @return
     */
    public int getCompleteCount() {
        int count = 0;
        for (FTPDataPatchModel dataPatchModel : dataPatchs) {
            System.out.println(dataPatchModel.toString());
            if (dataPatchModel.getStatus() == 2) {
                count++;
            }
        }
        return count;
    }

    /**
     * 更改 DataPatchModel 的数据状�?
     * 
     * @param siteIp
     * @param threadNumber 线程�?
     * @param status 0 表示setStatus(0)�?2 表示setStatus(2)
     */
    public void changePatchStatus(String siteIp, int threadNumber, int status) {
        for (FTPDataPatchModel dataPatchModel : dataPatchs) {
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
     * 任意的从数组里面取出 �?个未下载的数�?
     * 
     * @return
     */
    public FTPDataPatchModel pickUpPatch() {
        for (FTPDataPatchModel dataPatchModel : dataPatchs) {
            if (dataPatchModel.getStatus() == 0) {
                dataPatchModel.setStatus(1);
                return dataPatchModel;
            }
        }
        return null;
    }
}

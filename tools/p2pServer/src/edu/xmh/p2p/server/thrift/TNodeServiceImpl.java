/*
 * Copyright (c) 2016 p2p, Inc. All rights reserved.
 */
package edu.xmh.p2p.server.thrift;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;

import edu.xmh.p2p.server.dao.DealRequest;
import edu.xmh.p2p.server.dao.FileInfoEntity;

public class TNodeServiceImpl implements TNodeService.Iface {
    private static final Logger logger = LogManager.getLogger(TNodeServiceImpl.class.getName());

    @Override
    public List<String> queryFileUrls(String fileName) throws TException {
        // List<String> fileContents = getDataByFileName(fileName);
        List<String> fileContents = getDownLoadFile(fileName);
        logger.info("queryFileUrls fileName: " + fileName + ", results size: " + fileContents.size());
        return fileContents;
    }

    private List<String> getDataByFileName(String fileName) {
        DealRequest dr = DealRequest.getInstance();
        List<FileInfoEntity> infos = dr.getDataByFileName(fileName);
        List<String> fileContents = new ArrayList<>(infos.size());
        for (FileInfoEntity file : infos) {
            fileContents.add(file.getFileContent());
        }
        return fileContents;
    }


    private List<String> getDownLoadFile(String fileName) {
        List<String> ftpUrls = new ArrayList<String>();
        String ftpUrl =
                "ftp://jcgx_241:jcgx_241@10.12.52.241//site_yx/1010020002/20160628105922/0001-EVDB-32-4#GF1_WFV3_E117.0_N38.9_20160324_L1A0001485797.tar.gz";
        ftpUrls.add(ftpUrl);
        logger.info("Count ftpUrls: " + ftpUrls.size());

        return ftpUrls;
    }

    @Override
    public List<String> queryFilePatchUrls(String fileName) throws TException {
        // List<String> fileContents = getDataByFileName(fileName);
        List<String> fileContents = getDownLoadFile(fileName);
        logger.info("queryFileUrls fileName: " + fileName + ", results size: " + fileContents.size());
        return fileContents;
    }
}

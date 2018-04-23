/*
 * Copyright (c) 2016 p2p, Inc. All rights reserved.
 */
package edu.xmh.p2p.data.business.service.server;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import edu.xmh.p2p.data.access.dao.FileInfoDao;
import edu.xmh.p2p.data.access.model.FileInfoEntity;
import edu.xmh.p2p.data.contract.service.node.TNodeService;

@Service
public class TNodeServiceImpl implements TNodeService.Iface {

    private static final Logger logger = LogManager.getLogger(TNodeServiceImpl.class.getName());
    @Resource
    private FileInfoDao filenInfoDao;

    @Override
    public List<String> queryFileUrls(String fileName) throws TException {
        List<FileInfoEntity> filenInfoEntities = getDataByFileName(fileName);
        List<String> fileContents = new ArrayList<>(filenInfoEntities.size());
        for (FileInfoEntity file : filenInfoEntities) {
            fileContents.add(file.getFileContent());
        }
        logger.info("queryFileUrls fileName: " + fileName + ", results size: " + fileContents.size());
        return fileContents;
    }

    @Override
    public List<String> queryFilePatchUrls(String fileName) throws TException {
        List<FileInfoEntity> filenInfoEntities = getDataByFileName(fileName);
        List<String> fileContents = new ArrayList<>(filenInfoEntities.size());
        for (FileInfoEntity file : filenInfoEntities) {
            fileContents.add(file.getFileContent());
        }
        return fileContents;
    }

    private List<FileInfoEntity> getDataByFileName(String fileName) {
        System.out.println("-------");
        return filenInfoDao.getDataByFileName(fileName);
    }

    private List<String> getDownLoadFile(String fileName) {
        List<String> ftpUrls = new ArrayList<String>();
        String ftpUrl =
                "ftp://jcgx_241:jcgx_241@10.12.52.241//site_yx/1010020002/20160628105922/0001-EVDB-32-4#GF1_WFV3_E117.0_N38.9_20160324_L1A0001485797.tar.gz";
        ftpUrls.add(ftpUrl);
        logger.info("Count ftpUrls: " + ftpUrls.size());

        return ftpUrls;
    }
}

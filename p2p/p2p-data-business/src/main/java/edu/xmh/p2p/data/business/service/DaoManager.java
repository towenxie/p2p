package edu.xmh.p2p.data.business.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.xmh.p2p.data.access.dao.FileInfoDao;
import edu.xmh.p2p.data.access.model.FileInfoEntity;

@Service
public class DaoManager {
    @Autowired
    private FileInfoDao fileInfoDao;

    public List<FileInfoEntity> getDataByFileName(String fileName) {
        return fileInfoDao.getDataByFileName(fileName);
    }
}

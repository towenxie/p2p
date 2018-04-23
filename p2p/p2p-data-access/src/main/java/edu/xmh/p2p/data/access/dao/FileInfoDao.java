/*
 * Copyright (c) 2016 P2P, Inc. All rights reserved.
 */
package edu.xmh.p2p.data.access.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import edu.xmh.p2p.data.access.model.FileInfoEntity;

public interface FileInfoDao {

    List<FileInfoEntity> getDataByFileName(@Param("fileName") String fileName);
}

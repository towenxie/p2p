package edu.xmh.p2p.data.business.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import edu.xmh.p2p.data.access.model.FileInfoEntity;

public class SearchTableView {
    public static List<FileInfoEntity> infos = new ArrayList<FileInfoEntity>();
    public static Vector<String> SearchTableTitles = new Vector<String>();
    public static Vector SearchInfo = new Vector();

    public static void setSearchViewTitleVector() {
        SearchTableTitles.add("编号");
        SearchTableTitles.add("名称");
    }

    // 删除表格中的数据

    public static FileInfoEntity pickTableData(String name) {
        for (FileInfoEntity object : infos) {
            if (object.getFileName().equals(name)) {
                return object;
            }
        }
        return null;
    }

    public static void updateSearchTableData(List<FileInfoEntity> datas) {
        infos.clear();
        SearchInfo.clear();
        for (int i = 0; i < datas.size(); i++) {
            FileInfoEntity model = datas.get(i);
            Vector newFile = new Vector();
            newFile.add(model.getId());
            newFile.add(model.getFileName());
            SearchInfo.add(newFile);
            infos.add(model);
        }
    }
}

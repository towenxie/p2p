package edu.xmh.p2p.data.business.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.xmh.p2p.data.access.model.FileInfoEntity;
import edu.xmh.p2p.data.business.helper.*;
import edu.xmh.p2p.data.common.util.SpringUtils;
import edu.xmh.p2p.data.business.service.DaoManager;

public class SearchView extends JFrame {
    private static final long serialVersionUID = 3127923214799319068L;
    private JTextField nameText;
    JTable table;

    public static void main(String[] args) {
        SearchView frame = new SearchView();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public SearchView() {
        this.setTitle("搜索");
        this.setResizable(false);
        this.setBounds(150, 150, 600, 500);
        this.getContentPane().setLayout(null);

        JLabel nameLabel = new JLabel("名称:");
        nameLabel.setBounds(20, 20, 40, 35);
        this.getContentPane().add(nameLabel);

        nameText = new JTextField();
        nameText.setBounds(60, 20, 200, 35);
        this.getContentPane().add(nameText);

        JButton searchButton = new JButton("搜索");
        searchButton.setBounds(280, 20, 100, 35);
        searchButton.addActionListener(searchAction);
        this.getContentPane().add(searchButton);

        JButton sigleButton = new JButton("选中下载");
        sigleButton.setBounds(380, 20, 100, 35);
        sigleButton.addActionListener(sigleAction);
        this.getContentPane().add(sigleButton);

        JButton allButton = new JButton("全部下载");
        allButton.setBounds(480, 20, 100, 35);
        allButton.addActionListener(allAction);
        this.getContentPane().add(allButton);

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setBounds(10, 70, 570, 400);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 70, 570, 400);
        scrollPane.setViewportView(table);
        this.getContentPane().add(scrollPane);

        SearchTableView.setSearchViewTitleVector();
        table.setModel(new DefaultTableModel(SearchTableView.SearchInfo, SearchTableView.SearchTableTitles));
    }

    private ActionListener searchAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DaoManager daoService = SpringUtils.getBean(DaoManager.class);
            List<FileInfoEntity> infos = daoService.getDataByFileName(nameText.getText());
            SearchTableView.updateSearchTableData(infos);

            table.validate(); // 界面更新
            table.updateUI();
        }
    };

    private ActionListener sigleAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selIndex = table.getSelectedRow();// 得到所选行号
            String name = "";
            if (SearchTableView.SearchInfo.size() != 0 && selIndex >= 0) {
                name = table.getValueAt(selIndex, 1).toString();
                FileInfoEntity filenInfo = SearchTableView.pickTableData(name);
                TaskHelper.createTask(null, filenInfo);
            } else {
                // JOptionPane.showMessageDialog(, "未选任务");
            }
        }
    };

    private ActionListener allAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (FileInfoEntity filenInfo : SearchTableView.infos) {
                TaskHelper.createTask(null, filenInfo);
            }
        }
    };
}

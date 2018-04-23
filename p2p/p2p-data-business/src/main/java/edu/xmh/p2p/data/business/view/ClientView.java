package edu.xmh.p2p.data.business.view;

import javax.annotation.Resource;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import edu.xmh.p2p.data.business.constant.P2PConstants;
import edu.xmh.p2p.data.business.helper.*;

public class ClientView {
    public JFrame frame;
    private JTable table;
    private Timer timer = null;
    SearchView searchFrame = new SearchView();

    /**
     * 
     * * Create the application.
     */
    public ClientView() {
        // 下载客户端之前关闭时，没下载完的文件
        TaskHelper.setUpAllTasks();
        initialize();
        runUpdateTable();
    }

    // 每隔500ms把列表中的数据更新到界面中
    private void runUpdateTable() {
        timer = new Timer(500, updateTableAction);
        timer.start();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("FTP客户端");
        frame.setBounds(100, 100, 800, 700);
        frame.setResizable(false);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        // 客户端窗口关闭时，记录当前中断的文件
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                TaskHelper.saveTask();
                System.exit(0);
            }
        });

        JButton newButton = new JButton("新建任务");
        newButton.setBounds(180, 20, 100, 30);
        frame.getContentPane().add(newButton);
        if (P2PConstants.appConfig.isFromDB()) {
            newButton.addActionListener(newTaskActionFromDB);
        } else {
            newButton.addActionListener(newTaskActionFromFile);
        }

        JButton pausebutton = new JButton("暂停任务");
        pausebutton.setBounds(300, 20, 100, 30);
        // pausebutton.hide();
        frame.getContentPane().add(pausebutton);
        pausebutton.addActionListener(pauseTableAction);

        JButton jButton_start = new JButton("继续下载");
        jButton_start.setBounds(420, 20, 100, 30);
        frame.getContentPane().add(jButton_start);
        jButton_start.addActionListener(continueTableAction);

        JButton delButton = new JButton("删除任务");
        delButton.setBounds(540, 20, 100, 30);
        frame.getContentPane().add(delButton);
        delButton.addActionListener(deleteTableAction);

        // 表格
        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setBounds(10, 60, 770, 600);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 60, 770, 600);
        scrollPane.setViewportView(table);
        frame.getContentPane().add(scrollPane);

        TableView.setTitleVector();
        table.setModel(new DefaultTableModel(TableView.DownloadInfo, TableView.TableTitles));
    }

    /*
     * 触发ShowTable()方法
     */
    private Action updateTableAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            TableView.updateTableData();

            table.validate(); // 界面更新
            table.updateUI();
        }
    };

    // 暂停任务
    private Action pauseTableAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            int selIndex = table.getSelectedRow();// 得到所选行号
            String fileName = "";
            if (TableView.DownloadInfo.size() != 0 && selIndex >= 0) {
                fileName = table.getValueAt(selIndex, 1).toString();// 取出了这一行的第2列，即fileName
                // 暂停文件
                TaskHelper.deleteOrPause(fileName, false);
            } else {
                JOptionPane.showMessageDialog(frame, "未选任务");
            }

        }
    };

    // 删除任务
    private Action deleteTableAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            int selIndex = table.getSelectedRow();// 得到所选行号
            String fileName = "";
            if (TableView.DownloadInfo.size() != 0 && selIndex >= 0) {
                fileName = table.getValueAt(selIndex, 1).toString();// /取出了这一行的第2列，即fileName
                TaskHelper.deleteOrPause(fileName, true);
                TableView.deleteTableData(fileName);
            } else {
                JOptionPane.showMessageDialog(frame, "未选任务");
            }

            table.validate(); // 界面更新
            table.updateUI();
        }
    };

    // 继续下载
    private ActionListener continueTableAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selIndex = table.getSelectedRow();// 得到所选行号
            String fileName = "";
            if (TableView.DownloadInfo.size() != 0 && selIndex >= 0) {
                fileName = table.getValueAt(selIndex, 1).toString();// 取出了这一行的第2列，即fileName
                TaskHelper.continueDownload(fileName);
            } else {
                JOptionPane.showMessageDialog(frame, "未选任务");
            }

        }
    };

    private ActionListener newTaskActionFromFile = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("任务文件(*.xml)", "xml");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(true);
            chooser.setDialogTitle("选择任务");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                System.err.println(file.getName());
                // 返回能下载的最大文件数
                int result = TaskHelper.canAddTask(file.getName());
                if (result == 1) {
                    System.out.println(file.getAbsolutePath());
                    TaskHelper.createTask(file, null);
                } else if (result == 2) {
                    JOptionPane.showMessageDialog(frame, "下载任务已存在队列中");
                } else {
                    JOptionPane.showMessageDialog(frame, "下载任务已达到最大5个");
                }
            } else {
                System.out.println("No Selection ");
            }
        }
    };

    private ActionListener newTaskActionFromDB = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent arg0) {

            searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            searchFrame.setVisible(true);
        }
    };
}

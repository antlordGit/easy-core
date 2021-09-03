package com.easy.core.ui.frame;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.StatusText;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;


public class DatabaseFrame extends JBPanel {

//    public static DatabaseFrame INSTANCE = new DatabaseFrame();
    private JBTable jTable;
    private JSplitPane splitPanel;
    private JEditorPane sqlPanel;

    public DatabaseFrame() {
        super(new BorderLayout());

        DefaultActionGroup executeSqlActions = (DefaultActionGroup) ActionManager.getInstance().getAction("Execute Tool Bar action");
        ActionToolbar restToolBar = ActionManager.getInstance().createActionToolbar("executeSql Tool Bar", executeSqlActions, false);
        add(restToolBar.getComponent(), BorderLayout.WEST);

        splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPanel.setContinuousLayout(true);
        splitPanel.setDividerLocation(300);
        add(splitPanel);

        sqlPanel = new JEditorPane();
        sqlPanel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        sqlPanel.setBackground(Gray._80);
        sqlPanel.setForeground(Gray._205);
        sqlPanel.setCaretColor(Gray._205);
        JBScrollPane scrollPane = new JBScrollPane(sqlPanel);
        splitPanel.setLeftComponent(scrollPane);

        String[][] data = {};
        String[] title = {};
        TableModel model = new DefaultTableModel(data, title);
        initTable(model);
    }

    public void setTable(String[] title, String[][] data, Project project) {
        //获取EasyTool窗口
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("EasyTool");
        //启动窗口
        toolWindow.activate(() -> {
            //获取Database框内容
            Content database = toolWindow.getContentManager().findContent("Database");
            toolWindow.getContentManager().setSelectedContent(database);
            String[][] newData = null == data ? new String[0][0] : data;
            String[] newTitle = null == title ? new String[0] : title;
            //获取表格
            DefaultTableModel defaultTableModel = (DefaultTableModel) jTable.getModel();
            //清空旧数据
            defaultTableModel.getDataVector().clear();
            //设置新数据
            defaultTableModel.setDataVector(newData, newTitle);

            //获取列总数
            int columnCount = jTable.getColumnCount();
            String headerValue;
            for (int i = 0; i < columnCount; i++) {
                //设置列的宽度
                headerValue = (String) jTable.getColumnModel().getColumn(i).getHeaderValue();
                jTable.getColumnModel().getColumn(i).setPreferredWidth(headerValue.length() * 11 + 10);
            }

            jTable.updateUI();
        });
    }

    private void initTable(TableModel model) {
        jTable = new JBTable(model) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return false;
            }

            @NotNull
            @Override
            public StatusText getEmptyText() {
                StatusText statusText = new StatusText() {
                    @Override
                    protected boolean isStatusVisible() {
                        return isEmpty();
                    }
                };
                statusText.setText("无数据");
                return statusText;
            }


        };
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.setBorder(BorderFactory.createEmptyBorder());
        jTable.setGridColor(Gray._102);
        jTable.setBackground(new JBColor(new Color(19, 19, 20), new Color(19, 19, 20)));
        jTable.setForeground(new JBColor(Gray._235, Gray._235));
        jTable.setShowColumns(true);
        jTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        jTable.getTableHeader().setBackground(new JBColor(new Color(29, 29, 30), new Color(29, 29, 30)));
        jTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 14));
        jTable.getTableHeader().setForeground(new JBColor(Gray._235, Gray._235));
        jTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Gray._102));
        JBScrollPane scrollPane = new JBScrollPane(jTable);
        splitPanel.setRightComponent(scrollPane);
    }


    public void setSql(String sql){
        sqlPanel.setText(sql);
    }

    public String getSql() {
        return sqlPanel.getText();
    }

}

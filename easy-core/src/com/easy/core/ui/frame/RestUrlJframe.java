package com.easy.core.ui.frame;

import com.easy.core.entity.EasyJson;
import com.easy.core.entity.TreeNode;
import com.easy.core.icons.EasyIcon;
import com.easy.core.storage.EasyStorage;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.SimpleTree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;

public class RestUrlJframe extends JBPanel {

    private JPanel rootPanel;
    private JTree tree;
    private JEditorPane editorPane;
    private JSplitPane splitPanel;
    private DefaultTreeModel model;

//    public static RestUrlJframe INSTANCE = new RestUrlJframe();

    public RestUrlJframe() {
        super(new BorderLayout());
        rootPanel = new JBPanel(new BorderLayout());
        add(rootPanel);
        DefaultActionGroup restToolBarActions = (DefaultActionGroup) ActionManager.getInstance().getAction("rest_tool_bar_id");
        ActionToolbar restToolBar = ActionManager.getInstance().createActionToolbar("rest_tool_bar_action", restToolBarActions, false);
        add(restToolBar.getComponent(), BorderLayout.WEST);

        editorPane = new JEditorPane();
        splitPanel = new JSplitPane();
//        splitPanel.setOneTouchExpandable(true);//让分割线显示出箭头
//        splitPanel.setContinuousLayout(true);//操作箭头，重绘图形
//        splitPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);//设置分割线方向
        rootPanel.add(splitPanel);
        splitPanel.setRightComponent(editorPane);
        tree = new SimpleTree();
        model = (DefaultTreeModel) tree.getModel();

        JBScrollPane scrollPane = new JBScrollPane(tree);
        splitPanel.setLeftComponent(scrollPane);
        tree.addMouseListener(new PopupHandler() {
            @Override
            public void invokePopup(Component component, int x, int y) {

                String mapping = ((SimpleTree) component).getSelectedNode().getName();
                EasyJson easyJson = ((TreeNode) ((SimpleTree) component).getSelectedNode()).getTreeNavigationItem().getEasyJson();
                if (StringUtils.isNotBlank(mapping)) {
                    String port = EasyStorage.getPort();
                    EasyStorage.setCacheUrl("http://localhost:" +port + mapping);
                } else {
                    EasyStorage.setCacheUrl("");
                }

                if (null != easyJson) {
                    EasyStorage.setParameter(easyJson);
                }

                ActionManager manager = ActionManager.getInstance();
                ActionGroup actionGroup = (ActionGroup) manager.getAction("toolWindowRestServicePopupMenu");
                JPopupMenu newMyPopup = manager.createActionPopupMenu("NewTreePopup", actionGroup).getComponent();
                newMyPopup.show(component, x, y);
            }
        });


        editorPane.setBorder(BorderFactory.createBevelBorder(1, JBColor.magenta, JBColor.green));
        editorPane.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        splitPanel.setContinuousLayout(true);
        splitPanel.setDividerLocation(800);
    }


    public void initTree(TreeNode rootNode, Project project) {
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(rootNode);
        initTree(rootNode.getChildList(), rootTreeNode);
        model.setRoot(rootTreeNode);
        splitPanel.setDividerLocation(800);
        tree.setCellRenderer(new ColoredTreeCellRenderer() {
            @Override
            public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (tree instanceof SimpleTree) {
                    setIcon(EasyIcon.TOOL_WINDOW);
                    append(String.valueOf(value));
                }
            }
        });
    }

    private void initTree(List<TreeNode> nodeList, DefaultMutableTreeNode top) {
        for (TreeNode treeNode : nodeList) {
            if (CollectionUtils.isEmpty(treeNode.getChildList())) {
                top.add(new DefaultMutableTreeNode(treeNode));
            } else {
                DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(treeNode);
                top.add(defaultMutableTreeNode);
                initTree(treeNode.getChildList(), defaultMutableTreeNode);
            }
        }

    }

    public void setEditorPane(String text) {
        editorPane.setText(text);
    }

}



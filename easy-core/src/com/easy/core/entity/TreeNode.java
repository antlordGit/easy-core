package com.easy.core.entity;

import com.easy.core.icons.EasyIcon;
import com.easy.core.ui.EasyFrameFactory;
import com.easy.core.ui.frame.RestUrlJframe;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.util.List;

public class TreeNode extends CachingSimpleNode {
    private TreeNavigationItem treeNavigationItem;
    private List<TreeNode> childList = Lists.newArrayList();
    private String name;
    private String nodeIndex;
    private Project project;

    public TreeNode(SimpleNode parent, TreeNavigationItem treeNavigationItem, String name, String nodeIndex, Project project) {
        super(parent);
        this.treeNavigationItem = treeNavigationItem;
        this.name = name;
        this.nodeIndex = nodeIndex;
        this.project = project;
    }

    @Override
    protected SimpleNode[] buildChildren() {
        return childList.parallelStream().toArray(SimpleNode[]::new);
    }

    @Override
    public String getName() {
        return name;
    }

    public List<TreeNode> getChildList() {
        return childList;
    }

    @Override
    public void handleDoubleClickOrEnter(SimpleTree tree, InputEvent inputEvent) {
        SimpleNode selectedNode = tree.getSelectedNode();
        if (selectedNode instanceof TreeNode) {
            TreeNode treeNode = (TreeNode) selectedNode;
            if (null != treeNode.treeNavigationItem) {
                treeNode.treeNavigationItem.navigate(true);
            }
        }
        super.handleDoubleClickOrEnter(tree, inputEvent);
    }

    @Override
    public void handleSelection(SimpleTree tree) {
        SimpleNode selectedNode = tree.getSelectedNode();
        if (selectedNode instanceof TreeNode) {
            TreeNode treeNode = (TreeNode) selectedNode;
            if (null != treeNode.treeNavigationItem) {
                EasyJson easyJson = treeNode.treeNavigationItem.getEasyJson();
                RestUrlJframe instance = (RestUrlJframe) EasyFrameFactory.getComponent("RestService", treeNode.getDefaultProject());
                instance.setEditorPane(easyJson.toString(4));
            }
        }
        super.handleSelection(tree);
    }

    public TreeNavigationItem getTreeNavigationItem() {
        return treeNavigationItem;
    }

    @Override
    public void setIcon(@Nullable Icon closedIcon) {
        if (null == treeNavigationItem) {
            super.setIcon(EasyIcon.TOOL_WINDOW);
        } else {
            super.setIcon(EasyIcon.JAVA_METHOD);
        }
    }

    public String getNodeIndex() {
        return nodeIndex;
    }


    public Project getDefaultProject() {
        return project;
    }
}

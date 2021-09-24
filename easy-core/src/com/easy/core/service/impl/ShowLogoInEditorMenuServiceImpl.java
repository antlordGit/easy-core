package com.easy.core.service.impl;

import com.easy.core.service.ShowLogoInEditorMenuService;
import com.easy.core.storage.EasyStorage;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ShowLogoInEditorMenuServiceImpl implements ShowLogoInEditorMenuService {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Boolean logoShow = EasyStorage.getLogoShow();
        if (null == logoShow) {
            EasyStorage.setLogoShow(true);
            logoShow = true;
        }
        if (!logoShow) {
            EasyStorage.setLogoShow(true);
        }else {
            EasyStorage.setLogoShow(false);
        }
        showLogo(anActionEvent.getProject());
    }

    public static void showLogo(Project project) {
        try {
            // 获取当前编辑器
            Editor selectedTextEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            if (null == selectedTextEditor) {
                return;
            }
            // 获取当前编辑器里的主组件
            JComponent contentComponent = selectedTextEditor.getContentComponent();
            if (!EasyStorage.getLogoShow()) {
                contentComponent.removeAll();
                return;
            }
            if (contentComponent.getComponentCount() > 0) {
                return;
            }

            JLabel logoCenter = new JLabel();
            logoCenter.setForeground(new Color(0, 255, 0));
            logoCenter.setText("<html>\n" +
                    "<body>\n" +
                    "<p style=\"font-family: 微软雅黑;font-size: 20pt;font-weight: bold;color: yellow;\">中科闻歌</p>\n" +
                    "<p style=\"font-family: 微软雅黑;font-size: 60pt;font-weight: bold;\">陈志伟</p>\n" +
                    "</body>\n" +
                    "</html>");

            // 获取当前光标的位置
            contentComponent.setLayout(new FlowLayout(FlowLayout.LEFT, 1000, 30));
            contentComponent.removeAll();
            contentComponent.add(logoCenter);
//            // 获取滚动组件，并添加鼠标滚轮监听器
            selectedTextEditor.getScrollingModel().addVisibleAreaListener(visibleAreaEvent -> {
                int horizontalScrollOffset = visibleAreaEvent.getEditor().getScrollingModel().getHorizontalScrollOffset();
                int vorizontalScrollOffset = visibleAreaEvent.getEditor().getScrollingModel().getVerticalScrollOffset();
                contentComponent.setLayout(new FlowLayout(FlowLayout.LEFT, 1000 + horizontalScrollOffset, vorizontalScrollOffset + 30));
                contentComponent.removeAll();
                if (EasyStorage.getLogoShow()) {
                    contentComponent.add(logoCenter);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

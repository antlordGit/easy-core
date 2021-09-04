package com.easy.core.ui.component;

import com.intellij.ide.ui.LafManager;

import javax.swing.*;

/**
 * @Description: TODO
 * @Author: chenzhiwei
 * @Version: 1.0
 * @Create: 2021/8/20 22:09
 */
public class NyanApplicationComponent {
    public NyanApplicationComponent() {
        LafManager.getInstance().addLafManagerListener((__) -> {
            updateProgressBarUi();
        });
        updateProgressBarUi();

    }

//    @Override
//    public void initComponent() {
//        // 初始化监听文件监听器
//        ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter() {
//            // 监听打开项目，自定义文件变动时的功能
//            public void projectOpened(Project project) {
//                System.out.println("=======>projectOpened");
//                // 通知文件变动
//                MessageBusConnection connection = project.getMessageBus().connect();
//                connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerAdapter() {
//                    // 打开文件时
//                    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//                        System.out.println("=======>fileOpened");
//                        if (source == null) {
//                            return;
//                        }
//
//                        if (file == null) {
//                           return;
//                        }
//
//                        super.fileOpened(source, file);
//                    }
//
//                    // 取消关闭文件时
//                    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//                        if (source == null) {
//                            return;
//                        }
//
//                        if (file == null) {
//                            return;
//                        }
//                        System.out.println("=======>fileClosed");
//                        super.fileClosed(source, file);
//                    }
//
//                    // 选定文件发生变化时
//                    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
//                        System.out.println("=======>selectionChanged");
//                        if (event == null) {
//                            return;
//                        }
//
//                        super.selectionChanged(event);
//                    }
//                });
//                super.projectOpened(project);
//            }
//
//            // 关闭项目时
//            public void projectClosed(Project project) {
//                System.out.println("=======>projectClosed");
//                ActivatePowerModeManage.getInstance().destroy(project, true);
//                super.projectClosed(project);
//            }
//
//            // 正在关闭项目是
//            public void projectClosing(Project project) {
//                System.out.println("=======>projectClosing");
//                super.projectClosing(project);
//            }
//
//            // 取消关闭项目时
//            public boolean canCloseProject(Project project) {
//                System.out.println("=======>canCloseProject");
//                return super.canCloseProject(project);
//            }
//        });
//    }

    private void updateProgressBarUi() {
        UIManager.put("ProgressBarUI", NyanProgressBarUi.class.getName());
        UIManager.getDefaults().put(NyanProgressBarUi.class.getName(), NyanProgressBarUi.class);
    }
}

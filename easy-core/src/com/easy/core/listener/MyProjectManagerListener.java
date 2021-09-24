package com.easy.core.listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class MyProjectManagerListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        System.out.println("-=-=-projectOpened");
    }

    @Override
    public void projectClosed(@NotNull Project project) {
        System.out.println("-=-=-projectClosed");

    }

    @Override
    public void projectClosing(@NotNull Project project) {
        System.out.println("-=-=-projectClosing");

    }

    @Override
    public void projectClosingBeforeSave(@NotNull Project project) {
        System.out.println("-=-=-projectClosingBeforeSave");

    }
}

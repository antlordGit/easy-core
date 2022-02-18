package com.easy.core.listener;

import com.easy.core.observer.EasyObserver;
import com.easy.core.observer.LogoShowObserver;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyProjectManagerListener implements ProjectManagerListener {

    private List<EasyObserver> observerList = Lists.newArrayList();

    @Override
    public void projectOpened(@NotNull Project project) {
        System.out.println("-=-=-projectOpened");
        observerList.add(new LogoShowObserver());
        if (CollectionUtils.isNotEmpty(observerList)) {
            observerList.forEach(observer -> observer.observer("projectOpened", null));
        }
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

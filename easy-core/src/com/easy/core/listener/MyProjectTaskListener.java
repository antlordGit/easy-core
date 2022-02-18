package com.easy.core.listener;

import com.intellij.task.ProjectTaskContext;
import com.intellij.task.ProjectTaskListener;
import com.intellij.task.ProjectTaskManager;
import org.jetbrains.annotations.NotNull;

public class MyProjectTaskListener implements ProjectTaskListener {

    @Override
    public void started(@NotNull ProjectTaskContext context) {
        System.out.println("--==----started");
    }

    @Override
    public void finished(@NotNull ProjectTaskManager.Result result) {
        System.out.println("--==----finished");
    }
}

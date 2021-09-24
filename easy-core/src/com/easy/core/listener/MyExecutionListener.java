package com.easy.core.listener;

import com.easy.core.ui.component.EasyBalloon;
import com.intellij.execution.ExecutionListener;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

public class MyExecutionListener implements ExecutionListener {

    @Override
    public void processStartScheduled(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
        System.out.println("=============>processStartScheduled");
        new EasyBalloon("项目启动中", env.getProject(), JBColor.green);
    }

    @Override
    public void processStarting(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
        System.out.println("=============>processStarting");
//        new EasyBalloon("项目启动中", env.getProject(), JBColor.green);
    }

    @Override
    public void processNotStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
        System.out.println("=============>processNotStarted");
        new EasyBalloon("启动失败", env.getProject(), JBColor.red);
    }

    @Override
    public void processStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
        System.out.println("=============>processStarted3");
        new EasyBalloon("启动成功", env.getProject(), JBColor.green);
    }

    @Override
    public void processTerminating(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
        System.out.println("=============>processTerminating4");
    }

    @Override
    public void processTerminated(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler, int exitCode) {
        System.out.println("=============>processTerminated5");
        new EasyBalloon("已停止", env.getProject(), JBColor.green);
    }
}

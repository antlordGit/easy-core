package com.easy.core.listener;

import com.easy.core.ui.component.EasyBalloon;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionListener;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MyExecutionListener implements ExecutionListener {

    @Override
    public void processStartScheduled(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
        System.out.println("=============>processStartScheduled");
        try {
            String name = env.getState().getClass().getName();
            if (name.contains("RunHttpRequestProfileState")) {
                return;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        new EasyBalloon("加载配置", env.getProject(), new Color(0, 255, 0));
    }

    @Override
    public void processStarting(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
        try {
            String name = env.getState().getClass().getName();
            if (name.contains("RunHttpRequestProfileState")) {
                return;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("=============>processStarting");
        new EasyBalloon("正在启动", env.getProject(), JBColor.green);
    }

    @Override
    public void processNotStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
        try {
            String name = env.getState().getClass().getName();
            if (name.contains("RunHttpRequestProfileState")) {
                return;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("=============>processNotStarted");
        new EasyBalloon("启动失败", env.getProject(), new Color(255, 0, 0));
    }

    @Override
    public void processStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
        try {
            String name = env.getState().getClass().getName();
            if (name.contains("RunHttpRequestProfileState")) {
                return;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("=============>processStarted3");
        new EasyBalloon("启动成功", env.getProject(), new Color(0, 255, 0));
    }

    @Override
    public void processTerminating(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
        System.out.println("=============>processTerminating4");
    }

    @Override
    public void processTerminated(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler, int exitCode) {
        try {
            String name = env.getState().getClass().getName();
            if (name.contains("RunHttpRequestProfileState")) {
                return;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("=============>processTerminated5");
        new EasyBalloon("系统关闭", env.getProject(), new Color(255, 255, 0));
    }
}

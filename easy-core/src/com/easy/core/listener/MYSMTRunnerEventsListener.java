package com.easy.core.listener;

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MYSMTRunnerEventsListener implements SMTRunnerEventsListener {

    public MYSMTRunnerEventsListener() {
        super();
    }

    @Override
    public void onTestingStarted(@NotNull SMTestProxy.SMRootTestProxy smRootTestProxy) {
        System.out.println("=======<><>>onTestingStarted");
    }

    @Override
    public void onTestingFinished(@NotNull SMTestProxy.SMRootTestProxy smRootTestProxy) {
        System.out.println("=======<><>>onTestingFinished");

    }

    @Override
    public void onTestsCountInSuite(int i) {
        System.out.println("=======<><>>onTestsCountInSuite");

    }

    @Override
    public void onTestStarted(@NotNull SMTestProxy smTestProxy) {
        System.out.println("=======<><>>onTestStarted");

    }

    @Override
    public void onTestFinished(@NotNull SMTestProxy smTestProxy) {

        System.out.println("=======<><>>onTestFinished");

    }

    @Override
    public void onTestFailed(@NotNull SMTestProxy smTestProxy) {
        System.out.println("=======<><>>onTestFailed");

    }

    @Override
    public void onTestIgnored(@NotNull SMTestProxy smTestProxy) {
        System.out.println("=======<><>>onTestIgnored");

    }

    @Override
    public void onSuiteFinished(@NotNull SMTestProxy smTestProxy) {
        System.out.println("=======<><>>onSuiteFinished");

    }

    @Override
    public void onSuiteStarted(@NotNull SMTestProxy smTestProxy) {
        System.out.println("=======<><>>onSuiteStarted");

    }

    @Override
    public void onCustomProgressTestsCategory(@Nullable String s, int i) {
        System.out.println("=======<><>>onCustomProgressTestsCategory");

    }

    @Override
    public void onCustomProgressTestStarted() {
        System.out.println("=======<><>>onCustomProgressTestStarted");

    }

    @Override
    public void onCustomProgressTestFailed() {
        System.out.println("=======<><>>onCustomProgressTestFailed");

    }

    @Override
    public void onCustomProgressTestFinished() {
        System.out.println("=======<><>>onCustomProgressTestFinished");

    }

    @Override
    public void onSuiteTreeNodeAdded(SMTestProxy smTestProxy) {
        System.out.println("=======<><>>onSuiteTreeNodeAdded");

    }

    @Override
    public void onSuiteTreeStarted(SMTestProxy smTestProxy) {
        System.out.println("=======<><>>onSuiteTreeStarted");

    }
}

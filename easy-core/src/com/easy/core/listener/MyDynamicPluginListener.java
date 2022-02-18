package com.easy.core.listener;

import com.intellij.ide.plugins.CannotUnloadPluginException;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import org.jetbrains.annotations.NotNull;

public class MyDynamicPluginListener implements DynamicPluginListener {

    @Override
    public void beforePluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        System.out.println("---->beforePluginLoaded");
    }

    @Override
    public void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        System.out.println("---->beforePluginUnload");
    }

    @Override
    public void checkUnloadPlugin(@NotNull IdeaPluginDescriptor pluginDescriptor) throws CannotUnloadPluginException {
        System.out.println("---->checkUnloadPlugin");
    }

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        System.out.println("---->pluginLoaded");
    }

    @Override
    public void pluginUnloaded(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        System.out.println("---->pluginUnloaded");
    }
}

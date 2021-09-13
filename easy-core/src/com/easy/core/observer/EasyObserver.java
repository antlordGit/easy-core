package com.easy.core.observer;

import com.intellij.openapi.vfs.VirtualFile;

public interface EasyObserver {

    void observer(String action, VirtualFile file);
}

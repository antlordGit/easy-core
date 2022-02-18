package com.easy.core.observer;

import com.easy.core.listener.DocumentListener;
import com.easy.core.service.impl.ShowLogoInEditorMenuServiceImpl;
import com.intellij.openapi.vfs.VirtualFile;

public class LogoShowObserver implements EasyObserver{

    @Override
    public void observer(String action, VirtualFile file) {
        if (null != DocumentListener.project) {
            ShowLogoInEditorMenuServiceImpl.showLogo(DocumentListener.project, "文本");
        }
    }

}

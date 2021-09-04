package com.easy.core.action.menu;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @Description: TODO
 * @Author: chenzhiwei
 * @Version: 1.0
 * @Create: 2021/8/24 22:14
 */
public class BackgroundMenuAction extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        final FileChooserDescriptor xmlDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
        xmlDescriptor.setTitle("选择背景图");
        VirtualFile virtualFile = FileChooser.chooseFile(xmlDescriptor, event.getProject(), null);
        if (null != virtualFile) {
            prop.setValue("idea.background.frame", virtualFile.getCanonicalPath());
            prop.setValue("idea.background.editor", virtualFile.getCanonicalPath());
        }
    }
}

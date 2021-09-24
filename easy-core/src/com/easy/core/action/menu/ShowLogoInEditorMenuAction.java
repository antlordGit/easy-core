package com.easy.core.action.menu;

import com.easy.core.service.ShowLogoInEditorMenuService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

public class ShowLogoInEditorMenuAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        ShowLogoInEditorMenuService service = ServiceManager.getService(event.getProject(), ShowLogoInEditorMenuService.class);
        service.actionPerformed(event);
    }
}

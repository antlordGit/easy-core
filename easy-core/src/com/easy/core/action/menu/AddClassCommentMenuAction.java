package com.easy.core.action.menu;

import com.easy.core.service.AddClassCommentMenuService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

public class AddClassCommentMenuAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        AddClassCommentMenuService service = ServiceManager.getService(event.getProject(), AddClassCommentMenuService.class);
        service.actionPerformed(event);
    }
}

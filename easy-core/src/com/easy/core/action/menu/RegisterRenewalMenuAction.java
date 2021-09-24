package com.easy.core.action.menu;

import com.easy.core.service.RegisterRenewalMenuService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

public class RegisterRenewalMenuAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        RegisterRenewalMenuService service = ServiceManager.getService(event.getProject(), RegisterRenewalMenuService.class);
        service.actionPerformed(event);
    }
}

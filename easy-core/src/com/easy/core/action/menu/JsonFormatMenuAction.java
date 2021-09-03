package com.easy.core.action.menu;

import com.easy.core.service.JsonFormatMenuService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

/**
 * Json字符串转换
 */
public class JsonFormatMenuAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        JsonFormatMenuService service = ServiceManager.getService(event.getProject(), JsonFormatMenuService.class);
        service.actionPerformed();
    }
}

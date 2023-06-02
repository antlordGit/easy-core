package com.easy.core.action.menu;

import com.easy.core.service.ToSwaggerService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;

/**
 * 字符串驼峰命名
 */
public class ToSwaggerAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        ToSwaggerService service = ServiceManager.getService(event.getProject(), ToSwaggerService.class);
        service.actionPerformed(event);
    }
}

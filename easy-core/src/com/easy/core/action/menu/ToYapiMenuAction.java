package com.easy.core.action.menu;

import com.easy.core.service.ToYapiMenuService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;

/**
 * 字符串驼峰命名
 */
public class ToYapiMenuAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        ToYapiMenuService service = ServiceManager.getService(event.getProject(), ToYapiMenuService.class);
        service.actionPerformed(event);
    }
}

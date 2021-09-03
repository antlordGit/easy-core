package com.easy.core.action.menu;

import com.easy.core.service.HumpNamingMenuService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;

/**
 * 字符串驼峰命名
 */
public class HumpNamingMenuAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        HumpNamingMenuService service = ServiceManager.getService(event.getProject(), HumpNamingMenuService.class);
        service.actionPerformed(event);
    }
}

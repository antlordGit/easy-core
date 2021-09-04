package com.easy.core.action.function;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.easy.core.service.JsonFormatterService;

/**
 * 将光标选中的文本串进行JSON格式美化
 */
public class JsonFormatterAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        JsonFormatterService service = ServiceManager.getService(event.getProject(), JsonFormatterService.class);
        service.actionPerformed(event);
    }
}

package com.easy.core.action.function;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.easy.core.service.SqlFormatterService;

/**
 * 将光标选中的文本串进行SQL格式美化，并将参数放入占位符中SQL复制到剪贴板
 */
public class SqlFormatterAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        SqlFormatterService service = ServiceManager.getService(event.getProject(), SqlFormatterService.class);
        service.actionPerformed(event);
    }
}

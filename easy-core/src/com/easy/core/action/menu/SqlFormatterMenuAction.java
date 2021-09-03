package com.easy.core.action.menu;

import com.easy.core.service.SqlFormatterMenuService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;

/**
 * 将光标选中的文本串进行SQL格式美化，并将参数放入占位符中SQL复制到剪贴板
 */
public class SqlFormatterMenuAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        SqlFormatterMenuService service = ServiceManager.getService(event.getProject(), SqlFormatterMenuService.class);
        service.actionPerformed(event);
    }
}

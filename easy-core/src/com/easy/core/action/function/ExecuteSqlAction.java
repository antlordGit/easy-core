package com.easy.core.action.function;

import com.easy.core.icons.EasyIcon;
import com.easy.core.service.ExecuteSqlService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

/**
 * 执行SQL工具
 */
public class ExecuteSqlAction extends AnAction {

    public ExecuteSqlAction() {
        super(EasyIcon.RUN);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ExecuteSqlService service = ServiceManager.getService(anActionEvent.getProject(), ExecuteSqlService.class);
        service.setPsiElement(null);
        service.execute(anActionEvent.getProject());
    }
}

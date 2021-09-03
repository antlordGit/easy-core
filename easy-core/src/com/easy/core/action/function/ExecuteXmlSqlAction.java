package com.easy.core.action.function;

import com.easy.core.service.ExecuteSqlService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * 执行Xml文件中Sql
 */
public class ExecuteXmlSqlAction extends AnAction {

    private PsiElement psiElement;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ExecuteSqlService service = ServiceManager.getService(anActionEvent.getProject(), ExecuteSqlService.class);
        service.setPsiElement(psiElement);
        service.execute(anActionEvent.getProject());
    }

    public void setPsiElement(PsiElement psiElement) {
        this.psiElement = psiElement;
    }
}

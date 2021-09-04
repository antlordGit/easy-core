package com.easy.core.action.function;

import com.easy.core.service.CopyFullUrlService;
import com.easy.core.storage.EasyStorage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CopyFullUrlAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        CopyFullUrlService service = ServiceManager.getService(event.getProject(), CopyFullUrlService.class);
        service.actionPerformed(event);
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        try {
            PsiElement psiElement = event.getData(CommonDataKeys.PSI_ELEMENT);
            boolean visibel;
            if (psiElement instanceof PsiMethod) {
                PsiMethod psiMethod = (PsiMethod) psiElement;
                PsiAnnotation[] methodAnnotations = psiMethod.getAnnotations();
                List<PsiAnnotation> requestMapping = new ArrayList<>();
                for (PsiAnnotation methodAnnotation : methodAnnotations) {
                    if (methodAnnotation.getText().contains("Mapping")) {
                        requestMapping.add(methodAnnotation);
                    }
                }
                visibel = CollectionUtils.isNotEmpty(requestMapping);
            } else {
                visibel = StringUtils.isNotBlank(EasyStorage.getCacheUrl());
            }
            event.getPresentation().setEnabledAndVisible(visibel);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "CopyFullUrlAction");
        }
    }
}

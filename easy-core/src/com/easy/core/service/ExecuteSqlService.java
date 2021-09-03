package com.easy.core.service;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

public interface ExecuteSqlService {

    void setPsiElement(PsiElement psiElement);

    void execute(Project project);
}

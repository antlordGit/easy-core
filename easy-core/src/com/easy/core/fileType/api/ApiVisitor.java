package com.easy.core.fileType.api;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class ApiVisitor extends PsiElementVisitor {

    public void visitProperty(@NotNull ApiProperty o) {
        visitNamedElement(o);
    }

    public void visitNamedElement(@NotNull ApiNamedElement o) {
        visitPsiElement(o);
    }

    public void visitPsiElement(@NotNull PsiElement o) {
        visitElement(o);
    }

}
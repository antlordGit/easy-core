package com.easy.core.fileType.api;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class ApiNamedElementImpl  extends ASTWrapperPsiElement implements ApiNamedElement {
    public ApiNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}

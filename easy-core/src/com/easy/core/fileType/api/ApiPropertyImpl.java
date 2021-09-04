package com.easy.core.fileType.api;


import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class ApiPropertyImpl extends ApiNamedElementImpl implements ApiProperty {

    public ApiPropertyImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ApiVisitor visitor) {
        visitor.visitProperty(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof ApiVisitor) {
            accept((ApiVisitor)visitor);
        } else {
            super.accept(visitor);
        }
    }

    @Override
    public String getKey() {
        return ApiPsiImplUtil.getKey(this);
    }

    @Override
    public String getValue() {
        return ApiPsiImplUtil.getValue(this);
    }

    @Override
    public String getName() {
        return ApiPsiImplUtil.getName(this);
    }

    @Override
    public PsiElement setName(String newName) {
        return ApiPsiImplUtil.setName(this, newName);
    }

    @Override
    public PsiElement getNameIdentifier() {
        return ApiPsiImplUtil.getNameIdentifier(this);
    }

    @Override
    public ItemPresentation getPresentation() {
        return ApiPsiImplUtil.getPresentation(this);
    }

}

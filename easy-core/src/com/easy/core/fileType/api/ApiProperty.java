package com.easy.core.fileType.api;


import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;

public interface ApiProperty extends ApiNamedElement {

    String getKey();

    String getValue();

    @Override
    String getName();

    @Override
    PsiElement setName(String newName);

    @Override
    PsiElement getNameIdentifier();

    ItemPresentation getPresentation();

}
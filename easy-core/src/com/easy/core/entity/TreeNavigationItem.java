package com.easy.core.entity;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public class TreeNavigationItem implements NavigationItem {
    private PsiElement psiElement;
    private String name;
    private NavigationItem navigationItem;
    private EasyJson easyJson;

    public TreeNavigationItem(PsiElement psiElement, String name) {
        this.psiElement = psiElement;
        this.name = name;
        if (psiElement instanceof NavigationItem) {
            this.navigationItem = (NavigationItem) psiElement;
        }
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        return new TreeItemPresentation(name);
    }

    @Override
    public void navigate(boolean b) {
        if (null != navigationItem) {
            navigationItem.navigate(b);
        }
    }

    @Override
    public boolean canNavigate() {
        return true;
    }

    @Override
    public boolean canNavigateToSource() {
        return true;
    }

    public EasyJson getEasyJson() {
        return easyJson;
    }

    public void setEasyJson(EasyJson easyJson) {
        this.easyJson = easyJson;
    }
}

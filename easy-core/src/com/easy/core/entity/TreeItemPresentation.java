package com.easy.core.entity;

import com.easy.core.icons.EasyIcon;
import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TreeItemPresentation implements ItemPresentation {
    private String name;

    public TreeItemPresentation(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public String getPresentableText() {
        return name;
    }

    @Nullable
    @Override
    public String getLocationString() {
        return "";
    }

    @Nullable
    @Override
    public Icon getIcon(boolean b) {
        return EasyIcon.JAVA_METHOD;
    }
}

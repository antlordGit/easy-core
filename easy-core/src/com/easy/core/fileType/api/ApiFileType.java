package com.easy.core.fileType.api;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ApiFileType extends LanguageFileType {
    public static final ApiFileType INSTANCE = new ApiFileType();

    private ApiFileType() {
        super(ApiLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Api File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Api language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "api";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.Actions.Back;
    }
}

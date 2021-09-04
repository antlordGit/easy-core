package com.easy.core.fileType.api;

import com.easy.core.icons.EasyIcon;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ApiLanguageFileType extends LanguageFileType {
    public static final ApiLanguageFileType INSTANCE = new ApiLanguageFileType();

    protected ApiLanguageFileType() {
        super(ApiLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "api file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "api language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "api";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return EasyIcon.API_FILE;
    }
}

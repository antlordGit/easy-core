package com.easy.core.fileType.api;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class ApiFile extends PsiFileBase {

    public ApiFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ApiLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ApiLanguageFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Api File";
    }
}

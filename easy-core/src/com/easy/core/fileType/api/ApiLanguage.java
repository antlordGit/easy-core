package com.easy.core.fileType.api;

import com.intellij.lang.Language;

public class ApiLanguage extends Language {
    public static final ApiLanguage INSTANCE = new ApiLanguage();
    protected ApiLanguage() {
        super("api");
    }
}
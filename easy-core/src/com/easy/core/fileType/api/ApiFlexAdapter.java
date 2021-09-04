package com.easy.core.fileType.api;

import com.intellij.lexer.FlexAdapter;

public class ApiFlexAdapter extends FlexAdapter {

    public ApiFlexAdapter() {
        super(new ApiLexer(null));
    }
}

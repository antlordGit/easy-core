package com.easy.core.fileType.api;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ApiIElementType extends IElementType {

    public ApiIElementType( @NotNull @NonNls String debugName) {
        super(debugName, ApiLanguage.INSTANCE);
    }

}


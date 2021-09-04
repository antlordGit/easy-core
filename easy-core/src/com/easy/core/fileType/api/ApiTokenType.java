package com.easy.core.fileType.api;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @Description: TODO
 * @Author: chenzhiwei
 * @Version: 1.0
 * @Create: 2020/6/7 18:37
 */
public class ApiTokenType extends IElementType {
    public ApiTokenType(@NotNull @NonNls String debugName) {
        super(debugName, ApiLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "ApiTokenType." + super.toString();
    }
}

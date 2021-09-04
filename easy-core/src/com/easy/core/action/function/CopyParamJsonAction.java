package com.easy.core.action.function;

import com.easy.core.storage.EasyStorage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.LogUtil;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;

public class CopyParamJsonAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            String parameter = EasyStorage.getParameter();
            if (StringUtils.isNotBlank(parameter)) {
                CopyPasteManager.getInstance().setContents(new StringSelection(parameter));
                Messages.showInfoMessage("参数复制成功", "复制参数");
            } else {
                Messages.showInfoMessage("无参数", "复制参数");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "CopyParamJsonAction");
        }

    }
}

package com.easy.core.action.function;

import com.easy.core.entity.RestUrlConfiguration;
import com.easy.core.icons.EasyIcon;
import com.easy.core.storage.EasyState;
import com.easy.core.util.HttpRequestUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class ConsoleRerunAction extends AnAction {
    public ConsoleRerunAction() {
        super(EasyIcon.RUN);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        try {
            EasyState state = EasyState.getInstance();
            RestUrlConfiguration config = state.getConfig().getRestUrlConfiguration();
            if (null != config) {
                switch (config.getMethod()) {
                    case "GET":
                        HttpRequestUtil.get(config.getUrl(), anActionEvent.getProject());
                        break;
                    case "POST":
                        HttpRequestUtil.post(config.getUrl(), config.getHeaderList(), config.getParam(), anActionEvent.getProject());
                        break;
                    case "FILE":
                        HttpRequestUtil.file(config.getUrl(), config.getFilePath(), config.getParam(), anActionEvent.getProject());
                        break;
                    default:
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "ConsoleRerunAction");
        }

    }
}

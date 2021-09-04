package com.easy.core.action.function;

import com.easy.core.icons.EasyIcon;
import com.easy.core.service.RestServiceService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;

/**
 * 将Controller层的接口显示在编辑区中
 */
public class RestServiceAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent evnet) {
        RestServiceService service = ServiceManager.getService(evnet.getProject(), RestServiceService.class);
        service.actionPerformed(evnet);
    }

    public RestServiceAction() {
        super(EasyIcon.REFRESH);
    }

}

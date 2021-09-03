package com.easy.core.service.impl;

import com.easy.core.entity.EasyJson;
import com.easy.core.service.CopyFullUrlService;
import com.easy.core.storage.EasyStorage;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.LogUtil;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.apache.commons.lang3.StringUtils;

import java.awt.datatransfer.StringSelection;

public class CopyFullUrlServiceImpl implements CopyFullUrlService {

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            PsiElement psiElement = event.getData(CommonDataKeys.PSI_ELEMENT);
            if (psiElement instanceof PsiMethod) {
                PsiMethod psiMethod = (PsiMethod) psiElement;
                String restUrl = EasyStorage.getRestUrl();
                EasyJson json = new EasyJson(restUrl);
                boolean isNull = json.isNull(psiMethod.getName());
                if (!isNull) {
                    String port = EasyStorage.getPort();
                    String http = "http://localhost:" + port;
                    CopyPasteManager.getInstance().setContents(new StringSelection(http + json.getString(psiMethod.getName())));
                    Messages.showInfoMessage("URL复制成功", "复制地址");
                    return;
                }
            } else if (StringUtils.isNotBlank(EasyStorage.getCacheUrl())) {
                CopyPasteManager.getInstance().setContents(new StringSelection(EasyStorage.getCacheUrl()));
                Messages.showInfoMessage("URL复制成功", "复制地址");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "CopyFullUrlServiceImpl");
        }
    }
}

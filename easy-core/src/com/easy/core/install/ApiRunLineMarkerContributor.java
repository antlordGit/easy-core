package com.easy.core.install;

import com.easy.core.entity.EasyConfiguration;
import com.easy.core.entity.EasyHeader;
import com.easy.core.entity.RestUrlConfiguration;
import com.easy.core.fileType.api.ApiFile;
import com.easy.core.icons.EasyIcon;
import com.easy.core.storage.EasyState;
import com.easy.core.util.HttpRequestUtil;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiRunLineMarkerContributor extends RunLineMarkerContributor {

    private List<String> httpMethodList = new ArrayList<>(Arrays.asList("GET", "POST", "PUT", "DELETE"));

    @Nullable
    @Override
    public Info getInfo(@NotNull PsiElement psiElement) {
        try {
            PsiFile containingFile = psiElement.getContainingFile();
            if (containingFile instanceof ApiFile) {
                boolean anyMatch = false;
                for (String s : httpMethodList) {
                    if (s.equals(psiElement.getText())) {
                        anyMatch = true;
                        break;
                    }
                }

                if (anyMatch) {
                    AnAction[] array = new AnAction[1];
                    StringBuilder url = new StringBuilder();
                    String httpMethod = psiElement.getText();
                    String currentElement;
                    PsiElement nextSibling = psiElement.getNextSibling();
                    if (null == nextSibling) {
                        return null;
                    }

                    boolean isUrlNeed = false;
                    // 提取url
                    while (null != nextSibling && !(currentElement = nextSibling.getText()).contains("\n")) {
                        nextSibling = nextSibling.getNextSibling();
                        if (currentElement.contains("http")) {
                            isUrlNeed = true;
                        }
                        if (isUrlNeed && StringUtils.isNotBlank(currentElement)) {
                            url.append(currentElement);
                        }
                    }

                    // 提取Header
                    List<EasyHeader> headerList = Lists.newArrayList();
                    StringBuilder filePath = new StringBuilder();
                    StringBuilder hasFile = new StringBuilder();
                    StringBuilder hasDownloadPath = new StringBuilder();
                    StringBuilder downloadPath = new StringBuilder();
                    StringBuilder isDownload = new StringBuilder();
                    StringBuilder expansion = new StringBuilder();

                    while (null != nextSibling && !(currentElement = nextSibling.getText()).contains("#")) {
                        nextSibling = nextSibling.getNextSibling();
                        if (StringUtils.isNotBlank(currentElement)) {
                            if (currentElement.matches("[A-Z][-A-Za-z]*[\\d\\W\\w]*") && currentElement.contains(":") && currentElement.length() > currentElement.indexOf(":")) {
                                headerList.add(new EasyHeader(currentElement.substring(0, currentElement.indexOf(":")).trim(), currentElement.substring(currentElement.indexOf(":") + 1).trim()));
                            }

                            if (currentElement.contains("filePath")) {
                                hasFile.append("1");
                                filePath.append(currentElement.substring(currentElement.indexOf("filePath") + 8).replaceFirst(":", "").trim());
                            }

                            if (currentElement.contains("download") && !currentElement.contains("Path")) {
                                isDownload.append("1");
                                expansion.append(currentElement.substring(currentElement.indexOf("download") + 8).replaceFirst(":", "").trim());
                            }

                            if (currentElement.contains("downloadPath")) {
                                hasDownloadPath.append("1");
                                downloadPath.append(currentElement.substring(currentElement.indexOf("downloadPath") + 12).replaceFirst(":", "").trim());
                            }
                        }
                    }

                    // 提取参数JSON
                    StringBuilder jsonStr = new StringBuilder();
                    nextSibling = psiElement.getNextSibling();
                    if ("POST".equals(httpMethod.toUpperCase())) {
                        boolean isJsonBegin = false;
                        while (null != nextSibling && !(currentElement = nextSibling.getText()).contains("#")) {
                            nextSibling = nextSibling.getNextSibling();
                            if (StringUtils.isNotBlank(currentElement)) {
                                if (currentElement.contains("{") || currentElement.contains("[")) {
                                    isJsonBegin = true;
                                }
                                if (isJsonBegin) {
                                    jsonStr.append(currentElement);
                                }
                            }
                        }
                    }

                    array[0] = new AnAction("request") {
                        @Override
                        public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//                        EasyStorage.project = anActionEvent.getProject();
                            EasyState state = EasyState.getInstance();
                            EasyConfiguration easyConfiguration = state.getConfig();
                            RestUrlConfiguration config = easyConfiguration.getRestUrlConfiguration();
                            if (null == config) {
                                config = new RestUrlConfiguration();
                                easyConfiguration.setRestUrlConfiguration(config);
                            }
                            if ("1".equals(isDownload.toString())) {
                                config.setExpansion(expansion.toString());
                            } else {
                                config.setExpansion("");
                            }

                            if ("1".equals(hasDownloadPath.toString())) {
                                config.setDownloadPath(downloadPath.toString());
                            }

                            config.setMethod(httpMethod.toUpperCase());
                            config.setUrl(url.toString());
                            switch (httpMethod.toLowerCase()) {
                                case "get":
                                    HttpRequestUtil.get(url.toString(), psiElement.getProject());
                                    break;
                                case "post":
                                    config.setParam(jsonStr.toString());
                                    if (!"1".equals(hasFile.toString())) {
                                        config.setHeaderList(headerList);
                                        HttpRequestUtil.post(url.toString(), headerList, jsonStr.toString(), psiElement.getProject());
                                    } else {
                                        config.setMethod("FILE");
                                        config.setFilePath(filePath.toString());
                                        HttpRequestUtil.file(url.toString(), filePath.toString(), jsonStr.toString(), psiElement.getProject());
                                    }
                                    break;
                                default:
                                    HttpRequestUtil.get(url.toString(), psiElement.getProject());
                            }
                        }
                    };
                    return new Info(EasyIcon.RUN, array, psiElement1 -> "request");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "ApiRunLineMarkerContributor");
        }
        return null;
    }
}

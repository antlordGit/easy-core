package com.easy.core.install;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.impl.source.xml.XmlTokenImpl;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XmlTagJumpDeclarationHandler implements GotoDeclarationHandler {
    private final List<String> sqlMethod = new ArrayList<>(Arrays.asList("select", "update", "insert", "delete"));

    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement element, int position, Editor editor) {
        try {
            if (element instanceof XmlTokenImpl) {
                PsiFile containingFile = element.getContainingFile();
                if (containingFile instanceof XmlFile) {
                    if (null != element.getParent() && null != element.getParent().getParent() && StringUtils.isNotBlank(element.getParent().getParent().getText())
                            && element.getParent().getParent().getText().contains("refid")) {
                        XmlTag[] sqlTagArray = ((XmlFile) element.getContainingFile()).getRootTag().findSubTags("sql");
                        if (sqlTagArray.length > 0) {
                            return Stream.of(sqlTagArray).filter(p -> p.getAttribute("id").getValue().equals(element.getText())).toArray(PsiElement[]::new);
                        }
                    }

                    if (null != element.getParent() && null != element.getParent().getParent() && null != element.getParent().getParent().getParent()) {
                        if (element.getParent().getParent().getParent() instanceof XmlTagImpl) {
                            if ("sql".equals(((XmlTagImpl) element.getParent().getParent().getParent()).getName())) {
                                List<XmlTag> list = Lists.newArrayList();
                                XmlTag rootTag = ((XmlFile) element.getContainingFile()).getRootTag();
                                getIncludeTagList(rootTag, list);
                                if (list.size() > 0) {
//                                    LogUtil.addLog("XmlTagGotoDeclarationHandler", "", "Y", "跳转成功");
                                    return list.parallelStream().filter(p -> element.getText().equals(p.getAttributeValue("refid"))).toArray(PsiElement[]::new);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "XmlTagGotoDeclarationHandler");
        }
        return null;
    }

    private void getIncludeTagList(XmlTag parentTag, List<XmlTag> list) {
        if ("include".equals(parentTag.getName())) {
            list.add(parentTag);
        } else {
            List<PsiElement> collect = Stream.of(parentTag.getChildren()).filter(p -> p instanceof XmlTag).collect(Collectors.toList());
            for (PsiElement child : collect) {
                getIncludeTagList((XmlTag) child, list);
            }
        }
    }
}

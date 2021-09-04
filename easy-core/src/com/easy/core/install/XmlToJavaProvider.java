package com.easy.core.install;

import com.easy.core.icons.EasyIcon;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.file.impl.JavaFileManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class XmlToJavaProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        PsiFile containingFile = element.getContainingFile();
        if (containingFile instanceof XmlFile && element instanceof XmlTag) {
            XmlFile xmlFile = (XmlFile) containingFile;
            XmlTag rootTag = xmlFile.getRootTag();
            XmlTag currentTag = (XmlTag) element;
            XmlAttribute rootTagNamespaceAttribute = rootTag.getAttribute("namespace");
            if (null != rootTagNamespaceAttribute && StringUtils.isNotBlank(rootTagNamespaceAttribute.getValue())) {
                String namespace = rootTagNamespaceAttribute.getValue();
                PsiClass psiClass = JavaFileManager.getInstance(element.getProject()).findClass(namespace, GlobalSearchScope.projectScope(element.getProject()));
                PsiMethod[] methods = psiClass.getMethods();
                List<PsiMethod> collect = Lists.newArrayList();
                if (methods.length > 0) {
                    for (PsiMethod method : methods) {
                        if (null != currentTag.getAttribute("id") && currentTag.getAttributeValue("id").equals(method.getName())) {
                            collect.add(method);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(collect)) {
                        PsiMethod psiMethod = collect.get(0);
                        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                                .create(EasyIcon.TO_JAVA_METHOD)
                                .setTarget(psiMethod)
                                .setTooltipTitle("go to method")
                                .setAlignment(GutterIconRenderer.Alignment.CENTER);
                        result.add(builder.createLineMarkerInfo(element));
                    }
                }
            }
        }
        super.collectNavigationMarkers(element, result);
    }
}

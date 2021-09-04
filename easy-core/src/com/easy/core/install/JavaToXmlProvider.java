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
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class JavaToXmlProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (element instanceof PsiMethod) {
            PsiFile containingFile = element.getContainingFile();  //
            String packageName = ((PsiJavaFileImpl) containingFile).getPackageName();
            PsiMethod psiMethod = (PsiMethod) element;
            PsiClass psiClass = (PsiClass) psiMethod.getParent();
            String[] allFilenames = FilenameIndex.getAllFilenames(element.getProject());
            List<PsiFile> psiFileList = Lists.newArrayList();
            for (String allFilename : allFilenames) {
                if (allFilename.contains(".xml")) {
                    PsiFile[] array = FilenameIndex.getFilesByName(element.getProject(), allFilename, GlobalSearchScope.projectScope(element.getProject()));
                    if (array.length > 0) {
                        psiFileList.add(array[0]);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(psiFileList)) {
                psiFileList.forEach(file -> {
                    if (file instanceof XmlFile) {
                        XmlFile xmlFile = (XmlFile) file;
                        XmlTag rootTag = xmlFile.getRootTag();
                        String name = rootTag.getName();
                        if ("mapper".equals(name)) {
                            XmlAttribute namespace = rootTag.getAttribute("namespace");
                            if (null != namespace) {
                                String classpath = namespace.getValue();
                                if ((packageName + "." + psiClass.getName()).equals(classpath)) {
                                    XmlTag[] subTags = xmlFile.getRootTag().getSubTags();
                                    if (null != subTags && subTags.length > 0) {
                                        List<XmlTag> listSubTag = Lists.newArrayList();
                                        for (XmlTag subTag : subTags) {
                                            if (null != subTag.getAttribute("id") && subTag.getAttribute("id").getValue().equals(psiMethod.getName())) {
                                                listSubTag.add(subTag);
                                            }
                                        }

                                        if (CollectionUtils.isNotEmpty(listSubTag)) {
                                            XmlTag xmlTag = listSubTag.get(0);
                                            NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                                                    .create(EasyIcon.TO_XML_TAG)
                                                    .setTarget(xmlTag)
                                                    .setTooltipTitle("go to sql")
                                                    .setAlignment(GutterIconRenderer.Alignment.CENTER);
                                            result.add(builder.createLineMarkerInfo(((PsiMethod) element).getIdentifyingElement()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        super.collectNavigationMarkers(element, result);
    }


}

package com.easy.core.util;

import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MybatisXmlUtil {


    public static void resolution(PsiElement rootTag, StringBuilder sqlBuild) {
        PsiElement[] children = rootTag.getChildren();
        if (children.length > 0) {
            for (PsiElement child : children) {
                if (child instanceof XmlText) {
                    sqlBuild.append(child.getText().replaceAll("[ \\t\\n]+", " "));
                } else {
                    if (child instanceof XmlTagImpl) {
                        dealTag(child, sqlBuild);
                    } else {
                        resolution(child, sqlBuild);
                    }
                }
            }
        }
    }

    private static void dealTag(PsiElement child, StringBuilder sqlBuild) {
        try {

            switch (((XmlTagImpl) child).getName()) {
                case "trim":
                    XmlAttribute prefix = ((XmlTagImpl) child).getAttribute("prefix");
                    if (null != prefix) {
                        sqlBuild.append(prefix.getValue());
                    }
                    resolution(child, sqlBuild);
                    XmlAttribute suffix = ((XmlTagImpl) child).getAttribute("suffix");
                    if (null != suffix) {
                        sqlBuild.append(suffix.getValue());
                    }

                    XmlAttribute suffixOverrides = ((XmlTagImpl) child).getAttribute("suffixOverrides");
                    if (null != suffixOverrides && sqlBuild.toString().endsWith(suffixOverrides.getValue())) {

                        sqlBuild.replace(sqlBuild.lastIndexOf(suffixOverrides.getValue()), sqlBuild.lastIndexOf(suffixOverrides.getValue()) + suffixOverrides.getValue().length(), "");
                    }
                    break;

                case "where":
                    sqlBuild.append(" WHERE ");
                    resolution(child, sqlBuild);
                    break;

                case "set":
                    sqlBuild.append(" SET ");
                    resolution(child, sqlBuild);
                    if (sqlBuild.toString().endsWith(",")) {
                        sqlBuild.replace(sqlBuild.length() - 1, sqlBuild.length(), "");
                    }
                    break;

                case "include":
                    XmlAttribute refid = ((XmlTagImpl) child).getAttribute("refid");
                    String refidValue = refid.getValue();
                    PsiFile containingFile = child.getContainingFile();
                    XmlFile xmlFile = (XmlFile) containingFile;
                    XmlTag xmlFileRootTag = xmlFile.getRootTag();
                    PsiElement[] allChildren = xmlFileRootTag.getChildren();
                    List<PsiElement> collect = Stream.of(allChildren).filter(p -> {
                        if (p instanceof XmlTagImpl) {
                            return "sql".equals(((XmlTagImpl) p).getName())
                                    && null != ((XmlTagImpl) p).getAttribute("id")
                                    && ((XmlTagImpl) p).getAttribute("id").getValue().equals(refidValue);
                        }
                        return false;
                    }).collect(Collectors.toList());
                    resolution(collect.get(0), sqlBuild);
                    break;

                case "foreach":
                    XmlAttribute prefix2 = ((XmlTagImpl) child).getAttribute("open");
                    if (null != prefix2) {
                        sqlBuild.append(prefix2.getValue());
                    }
                    resolution(child, sqlBuild);
                    XmlAttribute suffix2 = ((XmlTagImpl) child).getAttribute("close");
                    if (null != suffix2) {
                        sqlBuild.append(suffix2.getValue());
                    }
                    break;
                default:
                    resolution(child, sqlBuild);
                    break;
            }
        } catch (Exception e) {
            System.out.println("MybatisXmlHandle.dealTag");
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "MybatisXmlHandle");
        }
    }

}

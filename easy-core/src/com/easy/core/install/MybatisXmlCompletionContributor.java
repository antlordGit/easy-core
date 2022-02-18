package com.easy.core.install;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easy.core.listener.DocumentListener;
import com.easy.core.storage.EasyStorage;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.jvm.JvmAnnotation;
import com.intellij.lang.jvm.JvmParameter;
import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.impl.JavaFileManager;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MybatisXmlCompletionContributor extends CompletionContributor implements DumbAware {

    public MybatisXmlCompletionContributor() {
        super();
//        extend(CompletionType.SMART, PlatformPatterns.psiElement(), new MybatisXmlCompletionProvider(MybatisXmlDescriptor.INSTANCE.mybatis));
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        int offset = parameters.getOffset();
        Document document = parameters.getEditor().getDocument();
        int lineStartOffset = document.getLineStartOffset(document.getLineNumber(offset));
        String text = document.getText(TextRange.create(lineStartOffset, offset));
        if (StringUtils.isBlank(text) && !text.contains("<if test") && !text.contains("#{") && !text.contains("property=")) {
            return;
        }
        beforeCompletion();
        String mybatisXmlFiled = EasyStorage.getMybatisXmlFiled();
        if (StringUtils.isBlank(mybatisXmlFiled)) {
            return;
        }
        String regex = "(<mapper[^>]+>)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(document.getText());
        String namespace = "";
        if (!matcher.find()) {
            return;
        } else {
            String group = matcher.group(0);
            namespace = group.substring(group.indexOf("\"") + 1, group.lastIndexOf("\""));
        }
        if (StringUtils.isBlank(namespace)) {
            return;
        }

        // 得到私有字段
        JSONObject jsonObject = JSON.parseObject(mybatisXmlFiled);
        if (null == jsonObject || 0 == jsonObject.size()) {
            return;
        }
        jsonObject = jsonObject.getJSONObject(namespace);
        if (null == jsonObject || 0 == jsonObject.size()) {
            return;
        }
        List<String> collect = jsonObject.values().parallelStream()
                .flatMap(p -> ((JSONArray) p).stream())
                .map(Object::toString)
                .distinct()
                .collect(Collectors.toList());
        for (String item : collect) {
            result.addElement(LookupElementBuilder.create(item).withPresentableText(item).withCaseSensitivity(true).bold());
        }
    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        super.beforeCompletion(context);
    }

    @Nullable
    @Override
    public String handleEmptyLookup(@NotNull CompletionParameters parameters, Editor editor) {
        return super.handleEmptyLookup(parameters, editor);
    }

    @Nullable
    @Override
    public AutoCompletionDecision handleAutoCompletionPossibility(@NotNull AutoCompletionContext context) {
        return super.handleAutoCompletionPossibility(context);
    }

    @Override
    public void duringCompletion(@NotNull CompletionInitializationContext context) {
        super.duringCompletion(context);
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @Nullable
    @Override
    public String advertise(@NotNull CompletionParameters parameters) {
        return super.advertise(parameters);
    }

    @Override
    public boolean invokeAutoPopup(@NotNull PsiElement position, char typeChar) {
//        return super.invokeAutoPopup(position, typeChar);
        return true;
    }

    private void beforeCompletion() {
        List<VirtualFile> xmlList = null;
        try {
            if (null == DocumentListener.project) {
                return;
            }
            VirtualFile[] openFiles = FileEditorManager.getInstance(DocumentListener.project).getOpenFiles();
            xmlList = Stream.of(openFiles).filter(p -> p.getFileType().getName().equals("XML")).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(xmlList)) {
                return;
            }
        } catch (Exception e) {
            System.out.println("====--=dd");
        }

        if (null == xmlList) {
            return;
        }
        JSONObject classJson = new JSONObject();
        for (VirtualFile file : xmlList) {
            SAXReader reader = new SAXReader();
            try {
                org.dom4j.Document document = reader.read(new File(file.getPath()));
                Element bookstore = document.getRootElement();
                String namespace = bookstore.attribute("namespace").getText();
                PsiClass aClass = JavaFileManager.getInstance(DocumentListener.project).findClass(namespace, GlobalSearchScope.projectScope(DocumentListener.project));
                if (null != aClass) {
                    PsiMethod[] allMethods = aClass.getMethods();
                    Map<String, List<String>> collect1 = Stream.of(allMethods).collect(Collectors.toMap(
                            PsiMethod::getName,
                            p -> {
                                JvmParameter[] parameters = p.getParameters();
                                if (0 == parameters.length) {
                                    return Lists.newArrayList();
                                }
                                List<String> collect = Stream.of(parameters).flatMap(t -> {
                                    JvmAnnotation annotation = t.getAnnotation("org.apache.ibatis.annotations.Param");
                                    List<String> list = Lists.newArrayList();
                                    if (null == annotation) {
                                        if (t.getType() instanceof PsiPrimitiveType) {
                                            list.add(t.getName());
                                        } else if (t.getType() instanceof PsiClassReferenceType) {
//                                            String psiClass = ((PsiClassReferenceType) parameters[0].getType()).getText(true);
                                            String psiClass = ((PsiClassReferenceType) parameters[0].getType()).getCanonicalText(true);
                                            PsiClass aClass1 = JavaFileManager.getInstance(DocumentListener.project).findClass(psiClass, GlobalSearchScope.projectScope(DocumentListener.project));
                                            if (null != aClass1) {
                                                PsiField[] fields = aClass1.getFields();
                                                List<String> collect2 = Stream.of(fields).parallel().map(k -> k.getName()).collect(Collectors.toList());
                                                list.addAll(collect2);
                                            }
                                        }
                                    } else {
                                        String annotationName = (String) ((JvmAnnotationConstantValue) allMethods[1].getParameters()[0].getAnnotation("org.apache.ibatis.annotations.Param").getAttributes().get(0).getAttributeValue()).getConstantValue();
                                        if (t.getType() instanceof PsiPrimitiveType) {
                                            list.add(annotationName + "." + t.getName());
                                        } else if (t.getType() instanceof PsiClassReferenceType) {
                                            String psiClass = ((PsiClassReferenceType) parameters[0].getType()).getCanonicalText(true);
                                            PsiClass aClass1 = JavaFileManager.getInstance(DocumentListener.project).findClass(psiClass, GlobalSearchScope.projectScope(DocumentListener.project));
                                            if (null != aClass1) {
                                                PsiField[] fields = aClass1.getFields();
                                                List<String> collect2 = Stream.of(fields).parallel().map(k -> annotationName + "." + k.getName()).collect(Collectors.toList());
                                                list.addAll(collect2);
                                            }
                                        }
                                    }
                                    return list.parallelStream();
                                }).collect(Collectors.toList());
                                return collect;
                            }));
                    classJson.put(namespace, collect1);
                    EasyStorage.setMybatisXmlFiled(classJson.toJSONString());
                }
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("===============>MybatisXmlCompletionContributor.beforeCompletion");
            }
        }
    }
}
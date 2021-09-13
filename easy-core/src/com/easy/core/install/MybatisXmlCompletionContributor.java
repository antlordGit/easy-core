package com.easy.core.install;

import com.easy.core.entity.MybatisXmlCompletionProvider;
import com.easy.core.entity.MybatisXmlDescriptor;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class MybatisXmlCompletionContributor extends CompletionContributor implements DumbAware {
    private static boolean flag = true;
    public MybatisXmlCompletionContributor() {
//        extend(CompletionType.SMART, PlatformPatterns.psiElement(), new MybatisXmlCompletionProvider(MybatisXmlDescriptor.INSTANCE.keywords));
//        extend(CompletionType.SMART, PlatformPatterns.psiElement(), new MybatisXmlCompletionProvider(MybatisXmlDescriptor.INSTANCE.types));
//        extend(CompletionType.SMART, PlatformPatterns.psiElement(), new MybatisXmlCompletionProvider(MybatisXmlDescriptor.INSTANCE.keywordsWithoutHighlight));
//        extend(CompletionType.SMART, PlatformPatterns.psiElement(), new MybatisXmlCompletionProvider(MybatisXmlDescriptor.INSTANCE.mybatis));
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        int offset = parameters.getOffset();
        Document document = parameters.getEditor().getDocument();
        int lineStartOffset = document.getLineStartOffset(document.getLineNumber(offset));
        String text = document.getText(TextRange.create(lineStartOffset, offset));
        Iterator iterator;
        String value;

        // 得到私有字段
//        Field privateStringField = null;
//        try {
//            privateStringField = CompletionContributor.class.getDeclaredField("myMap");
//            privateStringField.setAccessible(true);
//            MultiMap<CompletionType, Pair<ElementPattern<? extends PsiElement>, CompletionProvider<CompletionParameters>>> myMap = new MultiMap();
//            privateStringField.set(this, myMap);
//            MultiMap<CompletionType, Pair<ElementPattern<? extends PsiElement>, CompletionProvider<CompletionParameters>>> myMap1 = (MultiMap<CompletionType, Pair<ElementPattern<? extends PsiElement>, CompletionProvider<CompletionParameters>>>) privateStringField.get(this);
//            System.out.println("==============");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        iterator = MybatisXmlDescriptor.INSTANCE.mybatis.iterator();
        if (flag) {
            flag = false;
            extend(CompletionType.CLASS_NAME, PlatformPatterns.psiElement(), new MybatisXmlCompletionProvider(MybatisXmlDescriptor.INSTANCE.mybatis));
        }
        while (iterator.hasNext()) {
            value = (String) iterator.next();
            result.addElement(LookupElementBuilder.create(value).withPresentableText(value).withCaseSensitivity(true).bold());
        }

//        if (text.matches("\\s*!\\w*$")) {
//            iterator = MybatisXmlDescriptor.INSTANCE.preproc.iterator();
//            while (iterator.hasNext()) {
//                value = (String) iterator.next();
//                result.addElement(LookupElementBuilder.create(value.substring(1)).withPresentableText(value).withCaseSensitivity(true).bold());
//            }
//        } else if (text.matches("\\s*@\\w*$")) {
//            iterator = MybatisXmlDescriptor.INSTANCE.tags.iterator();
//
//            while (iterator.hasNext()) {
//                value = (String) iterator.next();
//                result.addElement(LookupElementBuilder.create(value.substring(1)).withPresentableText(value).withCaseSensitivity(true).bold());
//            }
//        } else {
//            if (text.endsWith("!")) {
//                return;
//            }
//
//            super.fillCompletionVariants(parameters, result);
////            if (PlantUmlSettings.getInstance().isAutoComplete()) {
////                WordCompletionContributor.addWordCompletionVariants(result, parameters, Collections.emptySet());
////            }
//            WordCompletionContributor.addWordCompletionVariants(result, parameters, Collections.emptySet());
//        }
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
        return super.invokeAutoPopup(position, typeChar);
    }
}
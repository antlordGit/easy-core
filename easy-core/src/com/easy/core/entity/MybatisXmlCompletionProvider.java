package com.easy.core.entity;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MybatisXmlCompletionProvider extends CompletionProvider<CompletionParameters> {

    private final List<String> myItems;

    public MybatisXmlCompletionProvider(List<String> items) {
        myItems = items;
    }
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext processingContext, @NotNull CompletionResultSet result) {
        if (parameters.getInvocationCount() == 0) {
            return;
        }
        for (String item : myItems) {
            result.addElement(LookupElementBuilder.create(item).withCaseSensitivity(true).withItemTextItalic(true));
        }
    }
}
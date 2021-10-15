package com.easy.core.listener;

import com.intellij.openapi.editor.event.VisibleAreaEvent;
import com.intellij.openapi.editor.event.VisibleAreaListener;
import org.jetbrains.annotations.NotNull;

public class FileEditorListener implements VisibleAreaListener {


    @Override
    public void visibleAreaChanged(@NotNull VisibleAreaEvent visibleAreaEvent) {
        System.out.println("===========");
    }


}

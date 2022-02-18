package com.easy.core.action.menu;

import com.easy.core.ui.frame.TranslationFrame;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TranslationMenuAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        TranslationFrame frame = new TranslationFrame();
        frame.pack();
        frame.setSize(500, 400);
        frame.setVisible(true);
    }
}

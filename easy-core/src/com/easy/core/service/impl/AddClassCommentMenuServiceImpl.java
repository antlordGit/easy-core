package com.easy.core.service.impl;

import com.easy.core.service.AddClassCommentMenuService;
import com.easy.core.storage.EasyStorage;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddClassCommentMenuServiceImpl implements AddClassCommentMenuService {

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            Editor editor = event.getData(CommonDataKeys.EDITOR);
            if (null == editor) {
                Messages.showWarningDialog("指定添加位置!", "Warning");
            }

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String author = EasyStorage.getAuthor();
            if (StringUtils.isBlank(author)) {
                author = Messages.showInputDialog(event.getProject(), "输入Author", "设置Author", AllIcons.Actions.Scratch, "CHENZHIWEI334", null);
                EasyStorage.setAuthor(author);
            }
            String comment = "/**\n" +
                    " * @Description: \n" +
                    " * @Author: " + author + "\n" +
                    " * @CreateTime: " + time + "\n" +
                    " */";
            WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
                int offset = editor.getCaretModel().getOffset();
                editor.getDocument().insertString(offset, comment);
            });
        } catch (Exception e) {
            System.out.println("AddClassCommentServiceImpl.apply");
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "AddClassCommentServiceImpl");
        }
    }
}

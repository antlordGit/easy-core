package com.easy.core.service.impl;

import com.easy.core.service.HumpNamingMenuService;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HumpNamingMenuServiceImpl implements HumpNamingMenuService {

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            String rule = "[a-zA-Z0-9]*";
            //获取鼠标编辑的对象
            Editor editor = event.getData(CommonDataKeys.EDITOR);
            if (null == editor) {
                Messages.showWarningDialog("未选择任何字符!", "Warning");
                return;
            }
            CaretModel caretModel = editor.getCaretModel();
            Caret currentCaret = caretModel.getCurrentCaret();
            //获取选中的字符串
            String selectedText = caretModel.getCurrentCaret().getSelectedText();
            if (StringUtils.isBlank(selectedText)) {
                Messages.showWarningDialog("未选择任何字符!", "Warning");
                return;
            }
            //正则表达式校验
            Pattern pattern = Pattern.compile(rule);
            Matcher matcher = pattern.matcher(selectedText);
            StringBuilder result = new StringBuilder();
            boolean isFirstLetter = true;
            //字符串大小写转化
            while (matcher.find()) {
                String text = matcher.group();
                if (StringUtils.isNoneBlank(text)) {
                    if (isFirstLetter) {
                        result.append(text.toLowerCase());
                    } else {
                        result.append(text.substring(0, 1).toUpperCase());
                        if (text.length() > 1) {
                            result.append(text.substring(1).toLowerCase());
                        }
                    }
                    isFirstLetter = false;
                }
            }

            if (StringUtils.isBlank(result.toString())) {
                Messages.showWarningDialog("未选择任何字符!", "Warning");
                return;
            }
            //写入编辑器相应位置
            WriteCommandAction.runWriteCommandAction(event.getProject(),
                    () -> editor.getDocument().replaceString(currentCaret.getSelectionStart(), currentCaret.getSelectionEnd(), result.toString()));
        } catch (Exception e) {
            System.out.println("HumpNamingServiceImpl.execute");
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "HumpNamingServiceImpl");
        }
    }
}

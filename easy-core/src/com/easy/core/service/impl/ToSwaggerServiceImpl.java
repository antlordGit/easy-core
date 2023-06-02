package com.easy.core.service.impl;

import com.easy.core.service.ToSwaggerService;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToSwaggerServiceImpl implements ToSwaggerService {

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            String cc = "\n\nimport io.swagger.annotations.ApiModelProperty;\n";
            //获取鼠标编辑的对象
            Editor editor = event.getData(CommonDataKeys.EDITOR);
            CaretModel caretModel = editor.getCaretModel();
            //获取选中的字符串
            String content = caretModel.getCurrentCaret().getEditor().getDocument().getText();
            if (!content.contains("ApiModelProperty")) {
                int i = content.indexOf(";");
                String cc2=cc;
                //写入编辑器相应位置
                WriteCommandAction.runWriteCommandAction(event.getProject(),
                        () -> editor.getDocument().insertString(i + 1, cc2));
            } else {
                cc = "";
            }

            String pattStr = "(?<=/\\*\\*\n)[\\w\\W\\u4e00-\\u9fa5]*?(?=\\*/\n)";
            Pattern pattern3= Pattern.compile(pattStr);
            Matcher matcher3=pattern3.matcher(content);

            int index=cc.length()+1;

            //将所有匹配的结果打印输出
            while(matcher3.find()) {
//                System.out.println("===========================");
                String group = matcher3.group();

                String dd = "\n    @ApiModelProperty(\"" + group.replace("*","").trim() + "\")";
                int start = matcher3.end() + 1 + index;
                int end = matcher3.end();
                String substring = content.substring(end, content.length());
                int aPrivate = substring.indexOf("private");
                String substring1 = substring.substring(0, aPrivate);
                if (substring1.contains("@ApiModelProperty")) {
                    continue;
                }

                //写入编辑器相应位置
                WriteCommandAction.runWriteCommandAction(event.getProject(),
                        () -> editor.getDocument().insertString(start, dd));
                index+=dd.length();
            }

        } catch (Exception e) {
            System.out.println("HumpNamingServiceImpl.execute");
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "HumpNamingServiceImpl");
        }
    }
}

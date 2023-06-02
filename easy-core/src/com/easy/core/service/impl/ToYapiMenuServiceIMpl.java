package com.easy.core.service.impl;

import com.easy.core.service.ToYapiMenuService;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToYapiMenuServiceIMpl implements ToYapiMenuService {
    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            //获取鼠标编辑的对象
            Editor editor = event.getData(CommonDataKeys.EDITOR);
            CaretModel caretModel = editor.getCaretModel();
            //获取选中的字符串
            String content = caretModel.getCurrentCaret().getEditor().getDocument().getText();
//            //待匹配的字符串："Hello"Hi"Nice"Good
//            //待匹配的字符串："Hello"Hi"Nice"Good
////          String content="\"Hello\"Hi\"Nice\"Good";
//            System.out.println(content);
            //匹配双引号的正则表达式
            String pattStr = "(?<=@ApiModelProperty\\().*?(?=\\))";
            //创建Pattern并进行匹配
            Pattern pattern3= Pattern.compile(pattStr);
            Matcher matcher3=pattern3.matcher(content);

            String pattStr2 = "(?<=\").*?(?=\")";
            //创建Pattern并进行匹配
            Pattern pattern2= Pattern.compile(pattStr2);

            int index=0;

            //将所有匹配的结果打印输出
            while(matcher3.find()) {
//                System.out.println("===========================");
                String group = matcher3.group();
//                System.out.println(matcher3.start());
//                System.out.println(matcher3.end());
//                System.out.println(group);
                if (group.contains(",")) {
                    String[] split = group.split(",");
                    for (String s : split) {
                        if (s.contains("value")) {
//                            System.out.println(s);
//                    String replace = s.replace("value", "").replace("=", "").replace(" ", "");
//                    System.out.println(replace);
                            Matcher matcher1 = pattern2.matcher(s);
                            while (matcher1.find()) {
//                            System.out.println(matcher1.group());
                                int start1 = matcher3.start();
                                String substring = content.substring(0, start1);
                                int aPrivate = substring.lastIndexOf("private");
                                String substring1 = substring.substring(aPrivate);
                                if (substring1.contains("/**")) {
                                    continue;
                                }


                                String dd = "/**\n" +
                                        "     * " + matcher1.group() + "\n" +
                                        "     */\n    ";
                                int start=matcher3.start()+index;
                                //写入编辑器相应位置
                                WriteCommandAction.runWriteCommandAction(event.getProject(),
                                        () -> editor.getDocument().insertString(start - "@ApiModelProperty".length() - 1, dd));
                                index+=dd.length();

                            }
                        }
                    }
                } else {
                    int start2 = matcher3.start();
                    String substring3 = content.substring(0, start2);
                    int aPrivate = substring3.lastIndexOf("private");
                    String substring4 = substring3.substring(aPrivate);
                    if (substring4.contains("/**")) {
                        continue;
                    }
                    if (group.contains("=")) {

                        Matcher matcher1 = pattern2.matcher(group);
                        while (matcher1.find()) {
//                            System.out.println(matcher1.group());

                            String dd = "/**\n" +
                                    "     * " + matcher1.group() + "\n" +
                                    "     */\n    ";
                            int start = matcher3.start() + index;
                            //写入编辑器相应位置
                            WriteCommandAction.runWriteCommandAction(event.getProject(),
                                    () -> editor.getDocument().insertString(start - "@ApiModelProperty".length() - 1, dd));
                            index += dd.length();

                        }
                    }else {
                        String substring = group.substring(1, group.length() - 1);
                        String dd = "/**\n" +
                                "     * " + substring + "\n" +
                                "     */\n    ";
                        int start=matcher3.start()+index;
                        //写入编辑器相应位置
                        WriteCommandAction.runWriteCommandAction(event.getProject(),
                                () -> editor.getDocument().insertString(start - "@ApiModelProperty".length() - 1, dd));
                        index+=dd.length();
                    }

                }

            }

        } catch (Exception e) {
            System.out.println("HumpNamingServiceImpl.execute");
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "HumpNamingServiceImpl");
        }
    }

    private void ddd() {

        // ApiOperation（）
    }
}

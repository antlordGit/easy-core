package com.easy.core.service.impl;

import com.easy.core.service.JsonFormatterService;
import com.easy.core.util.JsonFormatterUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

/**
 * 将光标选中的文本串进行JSON格式美化
 */
public class JsonFormatterServiceImpl implements JsonFormatterService {

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            //获取编辑的对象
            Editor editor = event.getData(CommonDataKeys.EDITOR);
            if (null == editor) {
                Messages.showInfoMessage("请选择字符串", "提示");
                return;
            }
            //获取编辑的数据字符串
            String json = editor.getCaretModel().getCurrentCaret().getSelectedText();
            if (StringUtils.isBlank(json)) {
                Messages.showInfoMessage("请选择字符串", "提示");
                return;
            }
            //json字符串格式转换
            String formatJson = JsonFormatterUtil.jsonFormatter(json);
            if (StringUtils.isNotBlank(formatJson)) {
                Messages.showInfoMessage(formatJson, "JSON已优化");
            } else {
                Messages.showInfoMessage("json字符串格式异常!", "提示");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "JsonFormatterServiceImpl");
        }
    }
}

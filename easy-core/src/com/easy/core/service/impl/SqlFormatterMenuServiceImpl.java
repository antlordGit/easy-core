package com.easy.core.service.impl;

import com.easy.core.service.SqlFormatterMenuService;
import com.easy.core.util.SqlFormatterUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlFormatterMenuServiceImpl implements SqlFormatterMenuService {

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            //取出编辑区
            Editor editor = event.getData(CommonDataKeys.EDITOR);
            if (null == editor) {
                Messages.showInfoMessage("请选择文本!", "Warning");
                return;
            }
            //取出编辑区选中的文本
            String selectedText = editor.getCaretModel().getCurrentCaret().getSelectedText();
            if (StringUtils.isNotBlank(selectedText)) {
                //文本中必须包含SQL语句和参数
                if (selectedText.contains("Preparing: ") && selectedText.contains("Parameters: ")) {
                    String[] split = selectedText.split("\n");
                    //  ==> Preparing: SELECT A.id, A.batch_data_after_id, A.task_id, A.valid_question, A.one_level_category, A.two_level_category, A.three_level_category, A.four_level_category, A.five_level_category, A.six_level_category, A.seven_level_category, A.eight_level_category, A.nine_level_category, A.ten_level_category, A.faq_standard_question, A.faq_same_question, A.answer_belong_channel, A.label_one, A.label_two, A.label_three, A.label_four, A.faq_type, A.faq_question, A.related_faq_type, A.related_faq_question, A.check_flag FROM knowledge_batch_data_after A WHERE A.del_flag= '1' AND A.task_id =? AND A.check_flag =? ORDER BY A.sort LIMIT ?,?
                    //SQL语句的List
                    List<String> collect = Stream.of(split).parallel().filter(p -> p.contains("SELECT") || p.contains("Preparing:")).collect(Collectors.toList());
                    String preparing = collect.get(0);
                    // ==> Parameters: eb99e63951da4b4aa009b88974ea5a1e(String), 0(String), 0(Integer), 20(Integer)
                    //参数的List
                    List<String> collect2 = Stream.of(split).parallel().filter(p -> p.contains("Parameters:")).collect(Collectors.toList());
                    String parameters = collect2.get(0);
                    if (StringUtils.isNotBlank(preparing) && StringUtils.isNotBlank(parameters)) {
                        String substring = preparing.substring(preparing.indexOf("Preparing: ") + 11);
                        String[] parametersItemArray = parameters.substring(parameters.indexOf("Parameters: ") + 12).split(",");
                        int index = 0;
                        //取代占位符
                        while (substring.contains("?")) {
                            if (parametersItemArray[index].contains("(String)")) {
                                substring = substring.replaceFirst("\\?", "'" + parametersItemArray[index].replace("(String)", "").trim() + "'");
                            } else {
                                substring = substring.replaceFirst("\\?", parametersItemArray[index].replaceAll("\\([A-Za-z]*\\)", "").trim());
                            }
                            index++;
                        }
                        //美化SQL语句
                        CopyPasteManager.getInstance().setContents(new StringSelection(SqlFormatterUtil.format(substring)));
                        Messages.showInfoMessage("已经复制SQL已优化!", "Info");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "SqlFormatterServiceImpl");
        }
    }
}

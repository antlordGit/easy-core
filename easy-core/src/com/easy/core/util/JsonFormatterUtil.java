package com.easy.core.util;

import com.easy.core.entity.EasyJson;
import com.easy.core.entity.EasyJsonArray;
import com.intellij.openapi.ui.Messages;

public class JsonFormatterUtil {

    public static String jsonFormatter(String json) {
        json = json.trim();
        String formatJson = "";
        try {
            if (json.startsWith("{")) {
                EasyJson jsonObject = new EasyJson(json);
                formatJson = jsonObject.toString(4);
            } else if (json.startsWith("[")) {
                EasyJsonArray jsonArray = new EasyJsonArray(json);
                formatJson = jsonArray.toString(4);
            }
        } catch (Exception e) {
            Messages.showInfoMessage("json字符串格式异常!", "提示");
            return json;
        }
        return formatJson;
    }

}

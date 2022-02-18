package com.easy.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonFormatterUtil {

    public static String jsonFormatter(String json) {
//        json = json.trim();
//        String formatJson = "";
//        try {
//            if (json.startsWith("{")) {
//                EasyJson jsonObject = new EasyJson(json);
//                formatJson = jsonObject.toString(4);
//            } else if (json.startsWith("[")) {
//                EasyJsonArray jsonArray = new EasyJsonArray(json);
//                formatJson = jsonArray.toString(4);
//            }
//        } catch (Exception e) {
//            Messages.showInfoMessage("json字符串格式异常!", "提示");
//            return json;
//        }
//        return formatJson;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        try {
            json = json.trim();
            String json2 = json;
            if (StringUtils.isBlank(json)) {
                return json;
            }
            String formatJson = "";
            if (json.startsWith("{")) {
                JSONObject jsonObject = JSON.parseObject(json);
                formatJson = JSON.toJSONString(jsonObject,
                        SerializerFeature.PrettyFormat,
                        SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteNullStringAsEmpty,
                        SerializerFeature.DisableCircularReferenceDetect,
                        SerializerFeature.WriteNullListAsEmpty
                );

            } else if (json.startsWith("[")) {
                JSONArray jsonArray = JSON.parseArray(json);
                formatJson = JSON.toJSONString(jsonArray,
                        SerializerFeature.PrettyFormat,
                        SerializerFeature.WriteMapNullValue,
                        SerializerFeature.WriteNullStringAsEmpty,
                        SerializerFeature.DisableCircularReferenceDetect,
                        SerializerFeature.WriteNullListAsEmpty);
            } else {
//                List<VirtualFile> allFilenames = (List<VirtualFile>) FilenameIndex.getAllFilesByExt(DocumentListener.project, "java", GlobalSearchScope.projectScope(DocumentListener.project));
//                Optional<VirtualFile> any = allFilenames.parallelStream().filter(p -> p.getPath().contains(json2)).findAny();
//                if (!any.isPresent()) {
//                    return json;
//                }
//                VirtualFile file = any.get();
//                inputStream = file.getInputStream();
//                inputStreamReader = new InputStreamReader(inputStream);
//                br = new BufferedReader(inputStreamReader);
//                String line;
//                String packageStr = "";
//                while ((line = br.readLine()) != null) {
//                    if (line.contains("package")) {
//                        packageStr = line.trim().replace("package", "").replace(";", "");
//                        break;
//                    }
//
//                }
//                if (StringUtils.isNotBlank(packageStr)) {
//                    String classPath = packageStr + "." + json + ".java";
//                    Class<?> aClass = Class.forName(classPath);
//                    PsiClass aClass1 = JavaFileManager.getInstance(DocumentListener.project).findClasses("com.wengegroup.sars.control.entity.dto.BaiduGeoURIDTO", GlobalSearchScope.allScope(DocumentListener.project))[0];
////                    aClass1.getAllFields()
//                    Object o = aClass.newInstance();
//                    formatJson = JSON.toJSONString(o,
//                            SerializerFeature.PrettyFormat,
//                            SerializerFeature.WriteMapNullValue,
//                            SerializerFeature.WriteNullStringAsEmpty,
//                            SerializerFeature.DisableCircularReferenceDetect,
//                            SerializerFeature.WriteNullListAsEmpty);
//
//                    EasyJson jsonObject = new EasyJson(json);
//                }
            }

            formatJson = formatJson.replace("\t", "    ");
            return formatJson;
        } catch (Exception e) {
            Messages.showInfoMessage("json字符串格式异常!", "提示");
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }

}

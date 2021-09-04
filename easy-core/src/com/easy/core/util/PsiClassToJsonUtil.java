package com.easy.core.util;

import com.easy.core.entity.EasyJson;
import com.easy.core.entity.EasyJsonArray;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.JavaPsiFacadeEx;
import org.codehaus.jettison.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PsiClassToJsonUtil {

    private static String rootClassName;

    public static void classToJson(String className, String filedName, EasyJson easyJson, Project project, boolean isFirst) throws JSONException {
        if (className.contains("javax.servlet.http")) {
            return;
        }

        EasyJson json = new EasyJson();
        EasyJsonArray array = new EasyJsonArray();
        if (!setJavaLangType(className, filedName, easyJson)) {
            if (className.equals(rootClassName) && !isFirst) {
            } else if (className.contains("List") || className.contains("[]")) {
                json.put(filedName, "[]");
            } else if (className.contains("Map")) {
                String substring = className.substring(className.indexOf("<") + 1, className.indexOf(">"));
                String[] split = substring.split(",");
                String keyClassName = split[0];
                String valueClassName = split[1];
                PsiField[] fieldArray = JavaPsiFacadeEx.getInstanceEx(project).findClass(valueClassName).getFields();
                EasyJson child = new EasyJson();
                if (valueClassName.equals(rootClassName)) {
                    json.put(filedName, child);
                } else if ("java.lang.String".equals(keyClassName) && "java.lang.String".equals(valueClassName)) {
                    json.put("demo", "demo");
                } else {
                    for (PsiField psiField : fieldArray) {
                        String childType = psiField.getType().getCanonicalText();
                        classToJson(childType, psiField.getName(), child, project, false);
                    }
                    json.put("demo", child);
                }
            } else {
                PsiField[] fields = JavaPsiFacadeEx.getInstanceEx(project).findClass(className).getFields();
                for (PsiField field : fields) {
                    String canonicalText = field.getType().getCanonicalText();

                    if (canonicalText.equals(rootClassName)) {
                        json.put(field.getName(), new EasyJson());
                    } else {
                        if (canonicalText.contains("UserPrincipal")) {
                            json.put(field.getName(), "null");
                        } else {
                            classToJson(canonicalText, field.getName(), json, project, false);
                        }
                    }
                }
            }
            if (array.length() > 0) {
                easyJson.put(filedName, array);
            } else {
                easyJson.put(filedName, json);
            }
        }
    }


    private static boolean setJavaLangType(String className, String filedName, EasyJson easyJson) throws JSONException {
        switch (className) {
            case "java.lang.String":
            case "String":
            case "char":
            case "java.lang.Char":
            case "Char":
                easyJson.put(filedName, "demo");
                return true;
            case "java.lang.Integer":
            case "Integer":
            case "int":
            case "java.lang.Short":
            case "Short":
            case "short":
            case "java.lang.Byte":
            case "Byte":
            case "byte":
                easyJson.put(filedName, 0);
                return true;
            case "java.lang.Long":
            case "Long":
            case "long":
                easyJson.put(filedName, 0L);
                return true;
            case "java.lang.Float":
            case "Float":
            case "float":
                easyJson.put(filedName, 0.0F);
                return true;
            case "java.lang.Double":
            case "Double":
            case "double":
                easyJson.put(filedName, 0.0D);
                return true;
            case "java.lang.Boolean":
            case "Boolean":
            case "boolean":
                easyJson.put(filedName, false);
                return true;
            case "java.util.Date":
            case "Date":
                easyJson.put(filedName, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return true;
            default:
                return false;
        }
    }

    public static void setClassName(String className) {
        PsiClassToJsonUtil.rootClassName = className;
    }
}

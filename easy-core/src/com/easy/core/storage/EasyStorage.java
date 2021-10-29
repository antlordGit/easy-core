package com.easy.core.storage;

import com.easy.core.entity.EasyJson;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

public class EasyStorage {

    /**
     * 添加全局缓存
     * @param key
     * @param value
     */
    public static void addGlobleValue(String key,String value){
        PropertiesComponent.getInstance().setValue(key, value);
    }

    /**
     * 获取全局缓存
     * @param key
     * @return
     */
    public static String getGlobleValue(String key){
        return PropertiesComponent.getInstance().getValue(key);
    }

    /**
     * 添加项目缓存
     * @param project
     * @param key
     * @param value
     */
    public static void addProjectValue(Project project, String key, String value){
        PropertiesComponent.getInstance(project).setValue(key, value);
    }

    /**
     * 获取项目缓存
     * @param project
     * @param key
     * @return
     */
    public static String getProjectValue(Project project,String key){
        return PropertiesComponent.getInstance().getValue(key);
    }

    public static void setRestUrl(String json) {
        PropertiesComponent.getInstance().setValue("restUrl", json);
    }

    public static String getRestUrl() {
        return PropertiesComponent.getInstance().getValue("restUrl");
    }

    /**
     * toolWindow树节点鼠标右键设置内容，点击右键时保存mapping
     * @param url mapping地址
     */
    public static void setCacheUrl(String url) {
        PropertiesComponent.getInstance().setValue("url", url);
    }

    /**
     * toolWindow树节点鼠标右键获取内容，
     * @return mapping地址
     */
    public static String getCacheUrl() {
        return PropertiesComponent.getInstance().getValue("url");
    }

    public static void setParameter(EasyJson param) {
        PropertiesComponent.getInstance().setValue("parameter", param.toString(4));
    }

    public static String getParameter() {
        return PropertiesComponent.getInstance().getValue("parameter");
    }

    public static void setAuthor(String author) {
        PropertiesComponent.getInstance().setValue("author", author);
    }

    public static String getAuthor() {
        return PropertiesComponent.getInstance().getValue("author");
    }

    public static void setPort(String port) {
        PropertiesComponent.getInstance().setValue("port", port);
    }

    public static String getPort() {
        return PropertiesComponent.getInstance().getValue("port");
    }

    public static Integer getFlowMaxHeight() {
        return PropertiesComponent.getInstance().getInt("maxHeight", 10);
    }

    public static void setFlowMaxHeight(Integer maxHeight) {
        PropertiesComponent.getInstance().setValue("maxHeight", maxHeight, 10);
    }

    public static Integer getFlowMaxWidth() {
        return PropertiesComponent.getInstance().getInt("maxWidth", 10);
    }

    public static void setFlowMaxWidth(Integer maxWidth) {
        PropertiesComponent.getInstance().setValue("maxWidth", maxWidth, 10);
    }

    public static String getSqmCookie() {
        return PropertiesComponent.getInstance().getValue("sqmCookie");
    }

    public static void setSqmCookie(String sqmCookie) {
        PropertiesComponent.getInstance().setValue("sqmCookie", sqmCookie);
    }

    public static void setMybatisXmlFiled(String filedList) {
        PropertiesComponent.getInstance().setValue("mybatisFiled", filedList);
    }

    public static String getMybatisXmlFiled() {
        return PropertiesComponent.getInstance().getValue("mybatisFiled");
    }

    public static void setCurrentFile(String filePath) {
        PropertiesComponent.getInstance().setValue("currentFilePath", filePath);
    }
    public static String gettCurrentFile() {
        return PropertiesComponent.getInstance().getValue("currentFilePath");
    }
    public static void setLogoShow(boolean showFlag) {
        PropertiesComponent.getInstance().setValue("logoShowFlag", showFlag);
    }
    public static boolean getLogoShow() {
        return PropertiesComponent.getInstance().getBoolean("logoShowFlag");
    }

    public static String getGenerator() {
        return PropertiesComponent.getInstance().getValue("generator");
    }

    public static void setGenerator(String json) {
        PropertiesComponent.getInstance().setValue("generator", json);
    }
}

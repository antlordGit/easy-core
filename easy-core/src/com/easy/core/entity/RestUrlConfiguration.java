package com.easy.core.entity;

import java.util.List;

public class RestUrlConfiguration {

//    public static RestUrlConfiguration INSTANCE = new RestUrlConfiguration();

    private String method;
    private String url;
    private List<EasyHeader> headerList;
    private String param;
    private String filePath;

    /**
     * 下载文件的格式
     */
    private String expansion;

    /**
     * 下载文件路劲
     */
    private String downloadPath;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<EasyHeader> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<EasyHeader> headerList) {
        this.headerList = headerList;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExpansion() {
        return expansion;
    }

    public void setExpansion(String expansion) {
        this.expansion = expansion;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }
}

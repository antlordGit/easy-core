package com.easy.core.util;

import com.easy.core.entity.*;
import com.easy.core.storage.EasyState;
import com.easy.core.ui.EasyFrameFactory;
import com.easy.core.ui.frame.ApiResponseConsoleFrame;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.batik.util.MimeTypeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class HttpRequestUtil {



//    public static Project project;

    public static void get(String url, Project project) {
//        new Thread(() -> {
//            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//            HttpGet httpGet = new HttpGet(url);
//            CloseableHttpResponse response = null;
//            try {
//                RequestConfig requestConfig = getRequestConfig();
//                httpGet.setConfig(requestConfig);
//
//                response = httpClient.execute(httpGet);
//                dealResponse(response, url);
//            } catch (Exception e) {
//                ApiResponseConsoleFrame.INSTANCE.consolePrint(e.getMessage());
//            } finally {
//                try {
//                    // 释放资源
//                    if (httpClient != null) {
//                        httpClient.close();
//                    }
//                    if (response != null) {
//                        response.close();
//                    }
//                } catch (IOException e) {
//                    ApiResponseConsoleFrame.INSTANCE.consolePrint(e.getMessage());
//                }
//            }
//        }).start();


        ApiResponseConsoleFrame apiConsole = (ApiResponseConsoleFrame) EasyFrameFactory.getComponent("ApiConsole", project);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {
            httpGet = new HttpGet(url);
            RequestConfig requestConfig = getRequestConfig();
            httpGet.setConfig(requestConfig);

            response = httpClient.execute(httpGet);
            dealResponse(response, url, project);
        } catch (Exception e) {
            apiConsole.consolePrint(e.getMessage(), project);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                apiConsole.consolePrint(e.getMessage(), project);
            }
        }
    }

    public static void post(String url, List<EasyHeader> headerList, String params, Project project) throws JSONException {
//        new Thread(() -> {
//            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//            String param = params.trim();
//            String formatJson;
//            try {
//                formatJson = buildParam(param);
//            } catch (RuntimeException e) {
//                ApiResponseConsoleFrame.INSTANCE.consolePrint(e.getMessage());
//                return;
//            }
//            StringEntity entity = new StringEntity(formatJson, "UTF-8");
//            HttpPost httpPost = new HttpPost(url);
//            httpPost.setEntity(entity);
//
//            if (CollectionUtils.isNotEmpty(headerList)) {
//                for (EasyHeader easyHeader : headerList) {
//                    httpPost.setHeader(easyHeader.getName(), easyHeader.getValue());
//                }
//            }
//
//            CloseableHttpResponse response = null;
//            try {
//                RequestConfig requestConfig = getRequestConfig();
//                httpPost.setConfig(requestConfig);
//
//                response = httpClient.execute(httpPost);
//                dealResponse(response, url);
//            } catch (Exception e) {
//                ApiResponseConsoleFrame.INSTANCE.consolePrint(e.getMessage());
//            } finally {
//                try {
//                    // 释放资源
//                    if (httpClient != null) {
//                        httpClient.close();
//                    }
//                    if (response != null) {
//                        response.close();
//                    }
//                } catch (IOException e) {
//                    ApiResponseConsoleFrame.INSTANCE.consolePrint(e.getMessage());
//                }
//            }
//        }).start();

        ApiResponseConsoleFrame apiConsole = (ApiResponseConsoleFrame) EasyFrameFactory.getComponent("ApiConsole", project);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String param = params.trim();
        String formatJson;
        try {
            formatJson = buildParam(param);
        } catch (RuntimeException e) {
            apiConsole.consolePrint(e.getMessage(), project);
            return;
        }
        StringEntity entity = new StringEntity(formatJson, "UTF-8");
        HttpPost httpPost = null;

        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);
            httpPost.setEntity(entity);

            if (CollectionUtils.isNotEmpty(headerList)) {
                for (EasyHeader easyHeader : headerList) {
                    httpPost.setHeader(easyHeader.getName(), easyHeader.getValue());
                }
            }
            RequestConfig requestConfig = getRequestConfig();
            httpPost.setConfig(requestConfig);

            response = httpClient.execute(httpPost);
            dealResponse(response, url, project);
        } catch (Exception e) {
            apiConsole.consolePrint(e.getMessage(), project);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                apiConsole.consolePrint(e.getMessage(), project);
            }
        }
    }

    public static void file(String url, String filePath, String params,Project project) {
        ApiResponseConsoleFrame apiConsole = (ApiResponseConsoleFrame) EasyFrameFactory.getComponent("ApiConsole", project);
        if (StringUtils.isBlank(filePath)) {
            apiConsole.consolePrint("文件不存在:"+filePath, project);
            return;
        }

//        new Thread(() -> {
//            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//            HttpPost httpPost = new HttpPost(url);
//            CloseableHttpResponse response = null;
//            try {
//
//                RequestConfig requestConfig = getRequestConfig();
//                httpPost.setConfig(requestConfig);
//
//                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//                // 第一个文件
//                String filesKey = "file";
////            File file1 = new File("E:\\project\\springboot\\idea-plugin\\my-plugin\\src\\11111.png");
//                // multipartEntityBuilder.addBinaryBody(filesKey, file1);
//                // 第二个文件(多个文件的话，使用同一个key就行，后端用数组或集合进行接收即可)
//                File file = new File(filePath);
//                if (!file.exists()) {
//                    ApiResponseConsoleFrame.INSTANCE.consolePrint("文件不存在：" + filePath);
//                    return;
//                }
//                // 防止服务端收到的文件名乱码。 我们这里可以先将文件名URLEncode，然后服务端拿到文件名时在URLDecode。就能避免乱码问题。
//                // 文件名其实是放在请求头的Content-Disposition里面进行传输的，如其值为form-data; name="files"; filename="头像.jpg"
//                multipartEntityBuilder.addBinaryBody(filesKey, file, ContentType.DEFAULT_BINARY, URLEncoder.encode(file.getName(), "UTF-8"));
//                // 其它参数(注:自定义contentType，设置UTF-8是为了防止服务端拿到的参数出现乱码)
//                String param = params.trim();
//                EasyJson jsonObject = null;
//                try {
//                    if (param.startsWith("{")) {
//                        jsonObject = new EasyJson(param);
//                    } else if (param.startsWith("[")) {
//                        EasyJsonArray jsonArray = new EasyJsonArray(param);
//                    }
//                } catch (RuntimeException e) {
//                    ApiResponseConsoleFrame.INSTANCE.consolePrint(e.getMessage());
//                    return;
//                }
//                if (null != jsonObject) {
//                    ContentType contentType = ContentType.create(MimeTypeConstants.MIME_TYPES_SVG[2], StandardCharsets.UTF_8);
//                    Set<String> keys = jsonObject.keySet();
//                    for (String key : keys) {
//                        multipartEntityBuilder.addTextBody(key, jsonObject.getString(key), contentType);
//                    }
//                }
//
//                HttpEntity httpEntity = multipartEntityBuilder.build();
//                httpPost.setEntity(httpEntity);
//
//                response = httpClient.execute(httpPost);
//                dealResponse(response, url);
//            } catch (Exception e) {
//                ApiResponseConsoleFrame.INSTANCE.consolePrint(e.getMessage());
//            } finally {
//                try {
//                    // 释放资源
//                    if (httpClient != null) {
//                        httpClient.close();
//                    }
//                    if (response != null) {
//                        response.close();
//                    }
//                } catch (IOException e) {
//                    ApiResponseConsoleFrame.INSTANCE.consolePrint(e.getMessage());
//                }
//            }
//        }).start();



        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);
            RequestConfig requestConfig = getRequestConfig();
            httpPost.setConfig(requestConfig);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // 第一个文件
            String filesKey = "file";
//            File file1 = new File("E:\\project\\springboot\\idea-plugin\\my-plugin\\src\\11111.png");
//             multipartEntityBuilder.addBinaryBody(filesKey, file1);
            // 第二个文件(多个文件的话，使用同一个key就行，后端用数组或集合进行接收即可)
            File file;
            if (filePath.contains(",")) {
                filesKey = "fileArray";
                String[] pathArray = filePath.split(",");
                for (String path : pathArray) {
                    file = new File(path);
                    if (!file.exists()) {
                        apiConsole.consolePrint("文件不存在：" + filePath, project);
                        return;
                    }
                    multipartEntityBuilder.addBinaryBody(filesKey, file, ContentType.DEFAULT_BINARY, URLEncoder.encode(file.getName(), "UTF-8"));
                }

            } else {
                filesKey = "file";
                file = new File(filePath);
                if (!file.exists()) {
                    apiConsole.consolePrint("文件不存在：" + filePath, project);
                    return;
                }
                multipartEntityBuilder.addBinaryBody(filesKey, file, ContentType.DEFAULT_BINARY, URLEncoder.encode(file.getName(), "UTF-8"));
            }

            // 防止服务端收到的文件名乱码。 我们这里可以先将文件名URLEncode，然后服务端拿到文件名时在URLDecode。就能避免乱码问题。
            // 文件名其实是放在请求头的Content-Disposition里面进行传输的，如其值为form-data; name="files"; filename="头像.jpg"
//            multipartEntityBuilder.addBinaryBody(filesKey, file, ContentType.DEFAULT_BINARY, URLEncoder.encode(file.getName(), "UTF-8"));
            // 其它参数(注:自定义contentType，设置UTF-8是为了防止服务端拿到的参数出现乱码)
            String param = params.trim();
            EasyJson jsonObject = null;
            try {
                if (param.startsWith("{")) {
                    jsonObject = new EasyJson(param);
                } else if (param.startsWith("[")) {
                    EasyJsonArray jsonArray = new EasyJsonArray(param);
                }
            } catch (RuntimeException e) {
                apiConsole.consolePrint(e.getMessage(), project);
                return;
            }
            if (null != jsonObject) {
                ContentType contentType = ContentType.create(MimeTypeConstants.MIME_TYPES_SVG[2], StandardCharsets.UTF_8);
                Set<String> keys = jsonObject.keySet();
                for (String key : keys) {
                    multipartEntityBuilder.addTextBody(key, jsonObject.getString(key), contentType);
                }
            }

            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);

            response = httpClient.execute(httpPost);
            dealResponse(response, url, project);
        } catch (Exception e) {
            apiConsole.consolePrint(e.getMessage(), project);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                apiConsole.consolePrint(e.getMessage(), project);
            }
        }
    }


    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(100 * 2)
                .setConnectionRequestTimeout(100 * 2)
                .setSocketTimeout(100 * 2)
                .setRedirectsEnabled(true).build();
    }

    private static void dealResponse(CloseableHttpResponse response, String url, Project project) throws Exception {
        HttpEntity responseEntity = response.getEntity();
        StringBuilder headerLine = new StringBuilder();
        for (Header header : response.getAllHeaders()) {
            headerLine.append(header.toString()).append("\n");
        }

        String fileName = "";
        if (responseEntity != null) {
            EasyState state = EasyState.getInstance();
            EasyConfiguration easyConfiguration = state.getConfig();
            RestUrlConfiguration config = easyConfiguration.getRestUrlConfiguration();
            if (StringUtils.isNotBlank(config.getExpansion())) {
                InputStream content = responseEntity.getContent();
                String parentPath = config.getDownloadPath();
                if (StringUtils.isBlank(parentPath)) {
                    String downloadPath = Messages.showInputDialog(project, "请输入保存路径", "下载文件", AllIcons.Actions.Scratch, "", null);
                    config.setDownloadPath(downloadPath);
                    Messages.showInfoMessage("下载路径已设置为" + config.getDownloadPath() + ",请重新请求", "提示");
                    return;
                }
                if (!parentPath.endsWith("\\")) {
                    parentPath = parentPath + "\\";
                }
                File parent = new File(parentPath);
                if (!parent.exists()) {
                    Messages.showInfoMessage("下载路径" + config.getDownloadPath() + "不存在", "提示");
                    String downloadPath = Messages.showInputDialog(project, "请输入保存路径", "下载文件", AllIcons.Actions.Scratch, "", null);
                    config.setDownloadPath(downloadPath);
                    Messages.showInfoMessage("下载路径已设置为" + config.getDownloadPath() + ",请重新请求", "提示");
                    return;
                }
                fileName = System.currentTimeMillis() + "." + config.getExpansion();
                File file = new File(parentPath + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int l;
                while ((l = content.read(bytes)) != -1) {
                    fos.write(bytes, 0, l);
                }
                fos.flush();
                fos.close();
                Messages.showInfoMessage("下载成功：" + config.getDownloadPath() + "\\" + fileName, "下载文件");
                content.close();
            }

            String resultEntity = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            String result = "";
            if (resultEntity.startsWith("{")) {
                EasyJson dataJson = new EasyJson(resultEntity);
                result = dataJson.toString(4);
            } else if (resultEntity.startsWith("[")) {
                EasyJsonArray array = new EasyJsonArray(resultEntity);
                array.toString(4);
            } else {
                result = resultEntity;
            }
            ApiResponseConsoleFrame apiConsole = (ApiResponseConsoleFrame) EasyFrameFactory.getComponent("ApiConsole", project);
            apiConsole.consolePrint(url + "\n" + headerLine.toString() + response.getStatusLine().toString() + "\n\n" + result, project);
        }
    }

    private static String buildParam(String param) throws JSONException {
        if (param.startsWith("{")) {
            EasyJson jsonObject = new EasyJson(param);
            return jsonObject.toString();
        } else if (param.startsWith("[")) {
            EasyJsonArray jsonArray = new EasyJsonArray(param);
            return jsonArray.toString();
        }
        return param;
    }

}

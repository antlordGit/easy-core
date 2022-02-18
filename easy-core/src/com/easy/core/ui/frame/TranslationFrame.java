package com.easy.core.ui.frame;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TranslationFrame extends JFrame {

    private JEditorPane editorPane;
    private JTextPane jTextPane;

    public TranslationFrame() {
        setTitle("Translation");
        Image jsonLogo = ((IconLoader.CachedImageIcon) IconLoader.getIcon("/icon/image/json.png")).getRealIcon().getImage();
        setIconImage(jsonLogo);
        setLocationRelativeTo(null);


        JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPanel.setContinuousLayout(true);
        splitPanel.setDividerLocation(200);
        splitPanel.setBackground(new Color(255, 0, 0));
//        splitPanel.setlo
        add(splitPanel);

        editorPane = new JEditorPane();
        editorPane.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        editorPane.setBackground(Gray._80);
        editorPane.setForeground(Gray._240);
        editorPane.setCaretColor(Gray._240);

        jTextPane = new JTextPane();
        jTextPane.setEditable(false);
        splitPanel.setBottomComponent(jTextPane);

        JBScrollPane scrollPane = new JBScrollPane(editorPane);
        splitPanel.setTopComponent(scrollPane);

        JPanel bottomJpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomJpanel.setBackground(new JBColor(new Color(60, 63, 65), new Color(60, 63, 65)));
        add(bottomJpanel, BorderLayout.SOUTH);

        JButton formatter = new JButton("Translation");
        formatter.setBackground(new JBColor(new Color(60, 63, 65), new Color(60, 63, 65)));
        formatter.setForeground(new JBColor(Gray._200, Gray._200));
        formatter.addActionListener(e -> onTranslation());
        bottomJpanel.add(formatter);

        JButton cancel = new JButton("Cancel");
        cancel.setBackground(new JBColor(new Color(60, 63, 65), new Color(60, 63, 65)));
        cancel.setForeground(new JBColor(Gray._200, Gray._200));
        cancel.addActionListener(e -> onCancel());
        bottomJpanel.add(cancel);


        JPanel leftJpanel = new JPanel();
        leftJpanel.setPreferredSize(new Dimension(10, 0));
        add(leftJpanel, BorderLayout.WEST);

        JPanel rightJpanel = new JPanel();
        rightJpanel.setPreferredSize(new Dimension(10, 0));
        add(rightJpanel, BorderLayout.EAST);

        JPanel topJpanel = new JPanel();
        topJpanel.setPreferredSize(new Dimension(0, 10));
        add(topJpanel, BorderLayout.NORTH);
    }

    private void onTranslation() {
        String text = editorPane.getText();
        text = text.replace(" ", "%20");
        if (StringUtils.isBlank(text)) {
            return;
        }
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {
            httpGet = new HttpGet("http://fanyi.youdao.com/translate?doctype=json&type=AUTO&i=" + text);
            RequestConfig requestConfig = getRequestConfig();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            String resultEntity = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(resultEntity);
            JSONArray translateResult = jsonObject.getJSONArray("translateResult");
            JSONArray jsonArray = translateResult.getJSONArray(0);
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            String tgt = jsonObject1.getString("tgt");
            jTextPane.setText(tgt);
        } catch (Exception e) {
            System.out.println("0-=-");
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
                System.out.println("dosu-d");
            }
        }
    }

    private void onCancel() {
        dispose();
    }

    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(100 * 2)
                .setConnectionRequestTimeout(100 * 2)
                .setSocketTimeout(100 * 2)
                .setRedirectsEnabled(true).build();
    }
}

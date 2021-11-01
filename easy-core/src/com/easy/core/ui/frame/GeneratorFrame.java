package com.easy.core.ui.frame;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easy.core.entity.TableInfo;
import com.easy.core.listener.DocumentListener;
import com.easy.core.storage.EasyStorage;
import com.google.common.collect.Maps;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class GeneratorFrame extends JBPanel {

    private JBPanel root;
    private JBPanel parentPanel;
    private JTextField dbUrl;
    private JTextField dbUser;
    private JTextField dbPassword;
    private JTextField dbTabel;
    private JTextField dbTabelAlia;
    private JTextField mapperPath;
    private JTextField xmlPath;
    private JTextField servicePath;
    private JTextField controllerPath;
    private JTextField pojoPath;
    private List<File> fileList = Lists.newArrayList();
    private Map<String, Boolean> map = Maps.newHashMap();
    private String tableComment;
    private String author;
    private String nameSpace;

    public GeneratorFrame() {
        super(new BorderLayout());
        root = new JBPanel(new BorderLayout());
        add(root);
        GridLayout gridLayout = new GridLayout(5, 2);
        gridLayout.setVgap(5);
        gridLayout.setHgap(10);
        parentPanel = new JBPanel(gridLayout);
        JBScrollPane scrollPane = new JBScrollPane(parentPanel);
        root.add(scrollPane);

        initUI();
    }

    private void initUI() {
        Color labelBackground = UIUtil.getLabelBackground();
        String generator = EasyStorage.getGenerator();
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(generator)) {
            jsonObject = JSON.parseObject(generator);
        }
        String dbUrlStr = jsonObject.getString("dbUrl");
        JPanel db = new JPanel(new BorderLayout());
        db.add(new JLabel("数据库链接"), BorderLayout.WEST);
        db.add(dbUrl = new JTextField(StringUtils.isBlank(dbUrlStr) ? "数据库链接串" : dbUrlStr), BorderLayout.CENTER);
        parentPanel.add(db);

        String dbUserStr = jsonObject.getString("dbUser");
        JPanel db2 = new JPanel(new BorderLayout());
        db2.add(new JLabel("用户名"), BorderLayout.WEST);
        db2.add(dbUser = new JTextField(StringUtils.isBlank(dbUserStr) ? "用户名" : dbUserStr), BorderLayout.CENTER);
        parentPanel.add(db2);

        String dbPasswordStr = jsonObject.getString("dbPassword");
        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.add(new JLabel("密码"), BorderLayout.WEST);
        panel3.add(dbPassword = new JTextField(StringUtils.isBlank(dbPasswordStr) ? "密码" : dbPasswordStr), BorderLayout.CENTER);
        parentPanel.add(panel3);

        String dbTable = jsonObject.getString("dbTabel");
        JPanel panel9 = new JPanel(new BorderLayout());
        panel9.add(new JLabel("表名"), BorderLayout.WEST);
        panel9.add(dbTabel = new JTextField(StringUtils.isBlank(dbTable) ? "表名" : dbTable), BorderLayout.CENTER);
        JPanel panel91 = new JPanel(new BorderLayout());
        panel91.add(new JLabel("别名"), BorderLayout.WEST);
        dbTabel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                dbTabelAlia.setText(defaultAlia());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                dbTabelAlia.setText(defaultAlia());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                dbTabelAlia.setText(defaultAlia());
            }
        });
        panel91.add(dbTabelAlia = new JTextField(defaultAlia()));
        panel9.add(panel91, BorderLayout.EAST);

        parentPanel.add(panel9);

        String mapperPathStr = jsonObject.getString("mapperPath");
        JPanel panel4 = new JPanel(new BorderLayout());
        panel4.add(new JLabel("mapper路径"), BorderLayout.WEST);
        panel4.add(mapperPath = new JTextField(StringUtils.isBlank(mapperPathStr) ? "mapper路径" : mapperPathStr), BorderLayout.CENTER);
        Button button1 = new Button("浏览");
        button1.setBackground(labelBackground);
        button1.addActionListener(e->{
            final FileChooserDescriptor xmlDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
            xmlDescriptor.setTitle("Choose Path");
            VirtualFile virtualFile = FileChooser.chooseFile(xmlDescriptor, DocumentListener.project, null);
            if (null != virtualFile) {
                mapperPath.setText(virtualFile.getPath());
            }
        });
        panel4.add(button1, BorderLayout.EAST);
        parentPanel.add(panel4);

        String xmlPathStr = jsonObject.getString("xmlPath");
        JPanel panel5 = new JPanel(new BorderLayout());
        panel5.add(new JLabel("xml路径"), BorderLayout.WEST);
        panel5.add(xmlPath = new JTextField(StringUtils.isBlank(xmlPathStr) ? "xml路径" : xmlPathStr), BorderLayout.CENTER);
        Button button2 = new Button("浏览");
        button2.setBackground(labelBackground);
        button2.addActionListener(e->{
            final FileChooserDescriptor xmlDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
            xmlDescriptor.setTitle("Choose Path");
            VirtualFile virtualFile = FileChooser.chooseFile(xmlDescriptor, DocumentListener.project, null);
            if (null != virtualFile) {
                xmlPath.setText(virtualFile.getPath());
            }
        });
        panel5.add(button2, BorderLayout.EAST);
        parentPanel.add(panel5);

        String servicePathStr = jsonObject.getString("servicePath");
        JPanel panel6 = new JPanel(new BorderLayout());
        panel6.add(new JLabel("service路径"), BorderLayout.WEST);
        panel6.add(servicePath = new JTextField(StringUtils.isBlank(servicePathStr) ? "service路径" : servicePathStr), BorderLayout.CENTER);
        Button button3 = new Button("浏览");
        button3.setBackground(labelBackground);
        button3.addActionListener(e->{
            final FileChooserDescriptor xmlDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
            xmlDescriptor.setTitle("Choose Path");
            VirtualFile virtualFile = FileChooser.chooseFile(xmlDescriptor, DocumentListener.project, null);
            if (null != virtualFile) {
                servicePath.setText(virtualFile.getPath());
            }
        });
        panel6.add(button3, BorderLayout.EAST);
        parentPanel.add(panel6);

        String controllerPathStr = jsonObject.getString("controllerPath");
        JPanel panel7 = new JPanel(new BorderLayout());
        panel7.add(new JLabel("controller路径"), BorderLayout.WEST);
        panel7.add(controllerPath = new JTextField(StringUtils.isBlank(controllerPathStr) ? "controller路径" : controllerPathStr), BorderLayout.CENTER);
        Button button4 = new Button("浏览");
        button4.setBackground(labelBackground);
        button4.addActionListener(e -> {
                    final FileChooserDescriptor xmlDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
                    xmlDescriptor.setTitle("Choose Path");
                    VirtualFile virtualFile = FileChooser.chooseFile(xmlDescriptor, DocumentListener.project, null);
                    if (null != virtualFile) {
                        controllerPath.setText(virtualFile.getPath());
                    }
                }
        );
        panel7.add(button4, BorderLayout.EAST);
        parentPanel.add(panel7);

        String pojoPathStr = jsonObject.getString("pojoPath");
        JPanel panel8 = new JPanel(new BorderLayout());
        panel8.add(new JLabel("pojo路径"), BorderLayout.WEST);
        panel8.add(pojoPath = new JTextField(StringUtils.isBlank(pojoPathStr) ? "POJO路径" : pojoPathStr), BorderLayout.CENTER);
        Button button5 = new Button("浏览");
        button5.setBackground(labelBackground);
        button5.addActionListener(e->{
            final FileChooserDescriptor xmlDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
            xmlDescriptor.setTitle("Choose Path");
            VirtualFile virtualFile = FileChooser.chooseFile(xmlDescriptor, DocumentListener.project, null);
            if (null != virtualFile) {
                pojoPath.setText(virtualFile.getPath());
            }
        });
        panel8.add(button5, BorderLayout.EAST);
        parentPanel.add(panel8);

        JPanel panel10 = new JPanel(new BorderLayout());
        Button button = new Button("创建文件");
        button.setBackground(labelBackground);
        button.addActionListener((e) -> { String temp = EasyStorage.getGenerator();
            fileList.clear();
            map.clear();
            tableComment = "";
            author = "";
            nameSpace = StrUtil.toCamelCase(dbTabelAlia.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabelAlia.getText()).substring(1);
            JSONObject jsonObject1 = null;
            if (StringUtils.isBlank(temp)) {
                jsonObject1 = new JSONObject();
            } else {
                jsonObject1 = JSON.parseObject(temp);
            }

            if (StringUtils.isNotBlank(pojoPath.getText())) {
                jsonObject1.put("pojoPath", pojoPath.getText());
                fileList.add(new File(pojoPath.getText() + "/" + nameSpace + "POJO.java"));
            }

            if (StringUtils.isNotBlank(controllerPath.getText())) {
                jsonObject1.put("controllerPath", controllerPath.getText());
                fileList.add(new File(controllerPath.getText() + "/" + nameSpace + "Controller.java"));
            }
            if (StringUtils.isNotBlank(servicePath.getText())) {
                jsonObject1.put("servicePath", servicePath.getText());
                fileList.add(new File(servicePath.getText() + "/" + nameSpace + "Service.java"));
                fileList.add(new File(servicePath.getText() + "/impl/" + nameSpace + "ServiceImpl.java"));
            }

            if (StringUtils.isNotBlank(xmlPath.getText())) {
                jsonObject1.put("xmlPath", xmlPath.getText());
                fileList.add(new File(xmlPath.getText() + "/" + StrUtil.toCamelCase(dbTabelAlia.getText()) + "Mapper.xml"));
            }

            if (StringUtils.isNotBlank(mapperPath.getText())) {
                jsonObject1.put("mapperPath", mapperPath.getText());
                fileList.add(new File(mapperPath.getText() + "/" + nameSpace + "Mapper.java"));
            }

            if (StringUtils.isNotBlank(dbUrl.getText())) {
                jsonObject1.put("dbUrl", dbUrl.getText());
            }
            if (StringUtils.isNotBlank(dbUser.getText())) {
                jsonObject1.put("dbUser", dbUser.getText());
            }
            if (StringUtils.isNotBlank(dbPassword.getText())) {
                jsonObject1.put("dbPassword", dbPassword.getText());
            }

            if (StringUtils.isNotBlank(dbTabel.getText())) {
                jsonObject1.put("dbTabel", dbTabel.getText());
            }

            EasyStorage.setGenerator(JSON.toJSONString(jsonObject1));
            String author = EasyStorage.getAuthor();
            if (StringUtils.isBlank(author)) {
                author = Messages.showInputDialog(DocumentListener.project, "输入Author", "设置Author", AllIcons.Actions.Scratch, "CHENZHIWEI", null);
                EasyStorage.setAuthor(author);
            }

            // 确定新增文件
            boolean creatFlag = checkPath();
            if (!creatFlag) {
                return;
            }
            generator();
        });
        panel10.add(button, BorderLayout.WEST);
        parentPanel.add(panel10);
    }

    private String defaultAlia() {
        if (null == dbTabel || StringUtils.isBlank(dbTabel.getText())) {
            return "数据表别名";
        }
        return dbTabel.getText();
    }

    private void generator() {
        try {
            createFile();
            StringBuffer sb = new StringBuffer();
            map.forEach((k, v) -> {
                if (v) {
                    sb.append("\n(覆盖)").append(k);
                } else {
                    sb.append("\n(新增)").append(k);
                }
            });
            Messages.showInfoMessage(sb.toString().replaceFirst("\n", ""), "已更新以下文件，请刷新编译器查看");
        } catch (Exception e) {
            System.out.println("===d=dd==d=d=");
            System.out.println(e.getMessage());
            Messages.showInfoMessage("创建文件异常", "提示");
        }
    }

    private boolean checkPath() {
        StringBuilder sb = new StringBuilder();
        for (File file : fileList) {
            if (file.exists()) {
                sb.append("\n").append(file.getAbsolutePath());
                map.put(file.getAbsolutePath(), true);
            } else {
                map.put(file.getAbsolutePath(), false);
            }
        }

        if (StringUtils.isNotBlank(sb.toString())) {
            int i = Messages.showYesNoCancelDialog(sb.toString().replaceFirst("\n", ""), "文件已存在，确定覆盖以下文件？", null);
            // 0:yes,1:no,2:cancel
            if (0 == i) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void createFile() throws IOException {
        createPOJO();
        createMapper();
        createXML();
        createService();
        createServiceImpl();
        createController();
    }

    private void createController() throws IOException {
        if (StringUtils.isBlank(controllerPath.getText())) {
            return;
        }

        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {

            File file = new File(controllerPath.getText() + "/" + nameSpace + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = controllerPath.getText().substring(controllerPath.getText().indexOf("java") + 5);
            String packageText = java.replaceAll("([/\\\\])", ".");

            String servicePath1 = servicePath.getText().substring(servicePath.getText().indexOf("java") + 5);
            String packageText1 = servicePath1.replaceAll("([/\\\\])", ".") + "." + nameSpace + "Service";

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport " + packageText1 + ";" +
                    "\nimport org.springframework.beans.factory.annotation.Autowired;" +
                    "\nimport org.springframework.web.bind.annotation.RequestMapping;" +
                    "\nimport org.springframework.web.bind.annotation.RestController;" +
                    "\nimport lombok.extern.slf4j.Slf4j;" +
                    "\n\n/**\n" +
                    " * Description: " + tableComment + "接口\n" +
                    " * @Author: " + author + "\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@RestController" +
                    "\n@RequestMapping(\"/" + StrUtil.toCamelCase(dbTabelAlia.getText()) + "\")" +
                    "\n@Slf4j" +
                    "\npublic class " + nameSpace + "Controller {";
            StringBuilder sb = new StringBuilder(text);
            String serviceFiled = nameSpace + "Service";
            String serviceFileds = serviceFiled.substring(0, 1).toLowerCase() + serviceFiled.substring(1);
            sb.append("\n\n\t/**\n" +
                    "\t * \t" + tableComment + "服务类\n" +
                    "\t */\n" +
                    "\t@Autowired\n" +
                    "\tprivate " + serviceFiled + " " + serviceFileds + ";");
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
            Messages.showInfoMessage("创建controller文件异常", "提示");
//            e.printStackTrace();
        } finally {
            if (null != fos) {
                fos.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (null != bw) {
                bw.close();
            }
        }

    }

    private void createServiceImpl() throws IOException {
        if (StringUtils.isBlank(servicePath.getText())) {
            return;
        }
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File fileImpl = new File(servicePath.getText() + "/impl/" + nameSpace + "ServiceImpl.java");
            if (!fileImpl.getParentFile().exists()) {
                fileImpl.getParentFile().mkdirs();
            }
            if (!fileImpl.exists()) {
                fileImpl.createNewFile();
            }

            fos = new FileOutputStream(fileImpl);
            outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = servicePath.getText().substring(servicePath.getText().indexOf("java") + 5);
            String packageText = java.replaceAll("([/\\\\])", ".") + ".impl";

            String service = java.replaceAll("([/\\\\])", ".") + "." + nameSpace;
            java = mapperPath.getText().substring(mapperPath.getText().indexOf("java") + 5);
            String mapper = java.replaceAll("([/\\\\])", ".") + "." + nameSpace;

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport " + service + "Service;" +
                    "\nimport " + mapper + "Mapper;" +
                    "\nimport org.springframework.stereotype.Service;" +
                    "\nimport lombok.extern.slf4j.Slf4j;" +
                    "\nimport org.springframework.beans.factory.annotation.Autowired;" +
                    "\n\n/**\n" +
                    " * Description: " + tableComment + "服务实现类\n" +
                    " * @Author: " + author + "\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@Service" +
                    "\n@Slf4j" +
                    "\npublic class " + nameSpace + "ServiceImpl implements " + nameSpace + "Service" + " {";
            StringBuilder sb = new StringBuilder(text);
            String mapperFiled = nameSpace + "Mapper";
            String mapperFileds = mapperFiled.substring(0, 1).toLowerCase() + mapperFiled.substring(1);
            sb.append("\n\t/**\n" +
                    "\t * \t" + tableComment + "数据库处理类\n" +
                    "\t */\n" +
                    "\t@Autowired\n" +
                    "\tprivate " + mapperFiled + " " + mapperFileds + ";");
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            Messages.showInfoMessage("创建impl文件异常", "提示");
        } finally {
            if (null != fos) {
                fos.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (null != bw) {
                bw.close();
            }
        }


    }
    private void createService() throws IOException {
        if (StringUtils.isBlank(servicePath.getText())) {
            return;
        }

        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File file = new File(servicePath.getText() + "/" + nameSpace + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = servicePath.getText().substring(servicePath.getText().indexOf("java") + 5);
            String packageText = java.replaceAll("([/\\\\])", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\n/**\n" +
                    " * Description: " + tableComment + "服务类\n" +
                    " * @Author: " + author + "\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\npublic interface " + nameSpace + "Service" + " {";
            StringBuilder sb = new StringBuilder(text);
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            Messages.showInfoMessage("创建service文件异常", "提示");
        } finally {
            if (null != fos) {
                fos.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (null != bw) {
                bw.close();
            }
        }
    }

    private void createXML() throws IOException {
        if (StringUtils.isBlank(xmlPath.getText())) {
            return;
        }
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File file = new File(xmlPath.getText() + "/" + StrUtil.toCamelCase(dbTabelAlia.getText()) + "Mapper.xml");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = mapperPath.getText().substring(mapperPath.getText().indexOf("java") + 5);
            String packageText = java.replaceAll("([/\\\\])", ".");
            String namespace = packageText + "." + nameSpace + "Mapper";
            String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" +
                    "<!-- 映射文件，映射到对应的SQL接口 -->\n" +
                    "<mapper namespace=\"" + namespace + "\">\n" +
                    "\t\n" +
                    "</mapper>";
            bw.write(text);
            bw.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            Messages.showInfoMessage("创建xml文件异常", "提示");
        } finally {
            if (null != fos) {
                fos.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (null != bw) {
                bw.close();
            }
        }
    }


    private void createMapper() throws IOException {
        if (StringUtils.isBlank(mapperPath.getText())) {
            return;
        }
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File file = new File(mapperPath.getText() + "/" + nameSpace + "Mapper.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = mapperPath.getText().substring(mapperPath.getText().indexOf("java") + 5);
            String packageText = java.replaceAll("([/\\\\])", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport org.apache.ibatis.annotations.Mapper;"
                    + "\n\n/**\n" +
                    " * Description: " + tableComment + "数据库处理类\n" +
                    " * @Author: " + author + "\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@Mapper" +
                    "\npublic interface " + nameSpace + "Mapper" + " {";
            StringBuilder sb = new StringBuilder(text);
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            Messages.showInfoMessage("创建Mapper文件异常", "提示");
        } finally {
            if (null != fos) {
                fos.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (null != bw) {
                bw.close();
            }
        }
    }

    private void createPOJO() throws IOException {
        if (StringUtils.isBlank(pojoPath.getText())) {
            return;
        }
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File file = new File(pojoPath.getText() + "/" + nameSpace + "POJO.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = pojoPath.getText().substring(pojoPath.getText().indexOf("java") + 5);
            String packageText = java.replaceAll("([/\\\\])", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String text1 = dbUrl.getText();
            text1 = text1.substring(0, text1.indexOf("?"));
            String tableSchema = text1.substring(text1.lastIndexOf("/") + 1);

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl.getText(), dbUser.getText(), dbPassword.getText());
            PreparedStatement preparedStatement = conn.prepareStatement(StrUtil.format("select  table_name,column_name,column_type, column_comment\n" +
                    "from information_schema.columns\n" +
                    "where TABLE_SCHEMA = '{}'\n" +
                    "  and table_name = '{}'", tableSchema, dbTabel.getText()));
            PreparedStatement preparedStatement2 = conn.prepareStatement(StrUtil.format("SELECT table_name,\n" +
                    "       table_comment\n" +
                    "FROM information_schema.TABLES\n" +
                    "WHERE table_schema = '{}'\n" +
                    "  and TABLE_NAME = '{}' limit 1", tableSchema, dbTabel.getText()));
            ResultSet resultSet = preparedStatement.executeQuery();
            int columnCount = resultSet.getMetaData().getColumnCount();
            List<String> titleList = Lists.newArrayList();
            List<List<String>> dataList = Lists.newArrayList();
            for (int i = 1; i <= columnCount; i++) {
                titleList.add(resultSet.getMetaData().getColumnName(i));
            }
            //获取查询结果
            int rowNum = 0;
            while (resultSet.next()) {
                rowNum++;
                List<String> rowData = Lists.newArrayList();
                rowData.add(rowNum + "");
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(resultSet.getString(i));
                }
                dataList.add(rowData);
            }

            ResultSet resultSet2 = preparedStatement2.executeQuery(StrUtil.format("SELECT table_name,\n" +
                    "       table_comment\n" +
                    "FROM information_schema.TABLES\n" +
                    "WHERE table_schema = '{}'\n" +
                    "  and TABLE_NAME = '{}' limit 1", tableSchema, dbTabel.getText()));
            while (resultSet2.next()) {
                tableComment = resultSet2.getString(2);
            }

            List<TableInfo> tableInfoList = Lists.newArrayList();
            boolean hasDateFlag = false;
            for (List<String> strings : dataList) {
                String type = strings.get(3);
                if (type.contains("(")) {
                    type = type.substring(0, type.indexOf("("));
                }
                type = getTypeMp().get(type);
                if ("Date".equals(type)) {
                    hasDateFlag = true;
                }
                tableInfoList.add(new TableInfo(strings.get(2), type, strings.get(4)));
            }
            conn.close();
            String text = "package " + packageText + ";"
                    + "\n\nimport lombok.Data;"
                    + "\nimport lombok.EqualsAndHashCode;"
                    + "\nimport java.io.Serializable;"
                    + "${}"
                    + "\n\n/**\n" +
                    " * Description: " + tableComment + "实体类\n" +
                    " * @Author: " + author + "\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@Data" +
                    "\n@EqualsAndHashCode(callSuper = false)\n" +
                    "public class " + nameSpace + "POJO" + " implements Serializable {" +
                    "\n\n\tprivate static final long serialVersionUID = 1L;";
            if (hasDateFlag) {
                text = text.replace("${}", "\nimport java.util.Date;");
            } else {
                text = text.replace("${}", "");
            }
            StringBuilder sb = new StringBuilder(text);
            String filed = "";
            for (TableInfo tableInfo : tableInfoList) {
                filed = "\n\n\t/**\n" +
                        "\t * " + tableInfo.getComment() + "\n" +
                        "\t */\n" +
                        "\tprivate " + tableInfo.getType() + " " + StrUtil.toCamelCase(tableInfo.getName()) + ";";
                sb.append(filed);
            }
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("------dddddd");
            Messages.showInfoMessage(e.getMessage(), "POJO创建失败");
        } finally {
            if (null != fos) {
                fos.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (null != bw) {
                bw.close();
            }
        }
    }

    private Map<String, String> getTypeMp() {
        Map<String, String> map = Maps.newHashMap();
        map.put("varchar", "String");
        map.put("VARCHAR", "String");
        map.put("int", "Integer");
        map.put("INT", "Integer");
        map.put("datetime", "Date");
        map.put("DATETIME", "Date");
        map.put("timestamp", "Date");
        map.put("TIMESTAMP", "Date");
        map.put("numeric", "Integer");
        map.put("NUMERIC", "Integer");
        map.put("text", "String");
        map.put("TEXT", "String");
        return map;
    }
}

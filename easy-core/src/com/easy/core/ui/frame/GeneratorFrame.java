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
import org.apache.commons.lang.text.StrBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private JTextField hooksPath;
    private List<File> fileList = Lists.newArrayList();
    private Map<String, Boolean> map = Maps.newHashMap();
    private String tableComment;
    private String author;
    private String nameSpace;
    private List<List<String>> colDataList = Lists.newArrayList();


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


        String hooksPathStr = jsonObject.getString("hooksPath");
        JPanel panel15 = new JPanel(new BorderLayout());
        panel15.add(new JLabel("hooks路径"), BorderLayout.WEST);
        panel15.add(hooksPath = new JTextField(StringUtils.isBlank(hooksPathStr) ? "" : hooksPathStr), BorderLayout.CENTER);
        Button button16 = new Button("浏览");
        button16.setBackground(labelBackground);
        button16.addActionListener(e->{
            final FileChooserDescriptor xmlDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
            xmlDescriptor.setTitle("Choose Path");
            VirtualFile virtualFile = FileChooser.chooseFile(xmlDescriptor, DocumentListener.project, null);
            if (null != virtualFile) {
                hooksPath.setText(virtualFile.getPath());
            }
        });
        panel15.add(button16, BorderLayout.EAST);
        parentPanel.add(panel15);

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
                fileList.add(new File(pojoPath.getText() + "/" + nameSpace + ".java"));
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
            String author2 = EasyStorage.getAuthor();
            if (StringUtils.isBlank(author2)) {
                author = Messages.showInputDialog(DocumentListener.project, "输入Author", "设置Author", AllIcons.Actions.Scratch, "CHENZHIWEI", null);
                EasyStorage.setAuthor(author);
            } else {
                author = author2;
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
        createHooks();
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


            String pojoJava = pojoPath.getText().substring(pojoPath.getText().indexOf("java") + 5);
            String pojoPackageText = pojoJava.replaceAll("([/\\\\])", ".") + "." + nameSpace;

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport " + packageText1 + ";" +
                    "\nimport org.springframework.beans.factory.annotation.Autowired;" +
                    "\nimport org.springframework.web.bind.annotation.RequestMapping;" +
                    "\nimport org.springframework.web.bind.annotation.RequestBody;" +
                    "\nimport org.springframework.web.bind.annotation.PostMapping;" +
                    "\nimport org.springframework.web.bind.annotation.RestController;" +
                    "\nimport java.util.List;" +
                    "\nimport lombok.extern.slf4j.Slf4j;" +
                    "\nimport com.wenge.appframe.core.bean.Result;" +
                    "\nimport " + pojoPackageText + ";" +
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

            String fieldName = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);

            // 新增接口
            StringBuilder addText = new StringBuilder();
            String serverName = "add" + nameSpace;
            addText.append("\n\n\t/**\n" +
                    "\t * 新增" + tableComment + "\n" +
                    "\t */\n" +
                    "\t@PostMapping(\"/" + serverName + "\")\n" +
                    "\tpublic Result " + serverName + "(@RequestBody " + nameSpace + " " + fieldName + ") {\n" +
                    "\t\treturn " + serviceFileds + "." + serverName + "(" + fieldName + ");\n" +
                    "\t}");
            sb.append(addText);

            // 查询接口
            StringBuilder searchText = new StringBuilder();
            serverName = "get" + nameSpace + "List";
            searchText.append("\n\n\t/**\n" +
                    "\t * 查询" + tableComment + "列表\n" +
                    "\t */\n" +
                    "\t@PostMapping(\"/" + serverName + "\")\n" +
                    "\tpublic Result " + serverName + "(@RequestBody " + nameSpace + " " + fieldName + ") {\n" +
                    "\t\treturn " + serviceFileds + "." + serverName + "(" + fieldName + ");\n" +
                    "\t}");
            sb.append(searchText);

            // 更新接口
            StringBuilder updateText = new StringBuilder();
            serverName = "update" + nameSpace;
            updateText.append("\n\n\t/**\n" +
                    "\t * 更新" + tableComment + "\n" +
                    "\t */\n" +
                    "\t@PostMapping(\"/" + serverName + "\")\n" +
                    "\tpublic Result " + serverName + "(@RequestBody " + nameSpace + " " + fieldName + ") {\n" +
                    "\t\treturn " + serviceFileds + "." + serverName + "(" + fieldName + ");\n" +
                    "\t}");
            sb.append(updateText);

            // 删除接口
            StringBuilder deleteText = new StringBuilder();
            serverName = "delete" + nameSpace;
            deleteText.append("\n\n\t/**\n" +
                    "\t * 删除" + tableComment + "\n" +
                    "\t */\n" +
                    "\t@PostMapping(\"/" + serverName + "\")\n" +
                    "\tpublic Result " + serverName + "(@RequestBody List<String> idList) {\n" +
                    "\t\treturn " + serviceFileds + "." + serverName + "(idList);\n" +
                    "\t}");
            sb.append(deleteText);

            sb.append("\n\n}");
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

            String pojoJava = pojoPath.getText().substring(pojoPath.getText().indexOf("java") + 5);
            String pojoPackageText = pojoJava.replaceAll("([/\\\\])", ".") + "." + nameSpace;

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport " + service + "Service;" +
                    "\nimport " + mapper + "Mapper;" +
                    "\nimport org.springframework.stereotype.Service;" +
                    "\nimport com.github.pagehelper.PageHelper;" +
                    "\nimport com.github.pagehelper.PageInfo;" +
                    "\nimport com.github.pagehelper.Page;" +
                    "\nimport java.util.List;" +
                    "\nimport com.wenge.appframe.core.bean.Result;" +
                    "\nimport " + pojoPackageText + ";" +
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

            String fieldName = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);

            // 新增实现
            StringBuilder addText = new StringBuilder();
            String serverName = "add" + nameSpace;
            addText.append("\n\n" +
                    "\t@Override\n" +
                    "\tpublic Result " + serverName + "(" + nameSpace + " " + fieldName + "){\n" +
                    "\t\t" + mapperFileds + "." + serverName + "(" + fieldName + ");\n" +
                    "\t\treturn Result.success();\n" +
                    "\t}");
            sb.append(addText);

            // 查询实现
            StringBuilder searchText = new StringBuilder();
            serverName = "get" + nameSpace + "List";
            searchText.append("\n\n" +
                    "\t@Override\n" +
                    "\tpublic Result " + serverName + "(" + nameSpace + " " + fieldName + "){\n" +
                    "\t\tPage<" + nameSpace + "> page = PageHelper.startPage(" + fieldName + ".getPageNo(), " + fieldName + ".getPageSize());\n" +
                    "\t\t" + mapperFileds + "." + serverName + "(" + fieldName + ");\n" +
                    "\t\tPageInfo<" + nameSpace + "> pageInfo = PageInfo.of(page);\n" +
                    "\t\treturn Result.success(pageInfo);\n" +
                    "\t}");
            sb.append(searchText);

            // 更新实现
            StringBuilder updateText = new StringBuilder();
            serverName = "update" + nameSpace;
            updateText.append("\n\n" +
                    "\t@Override\n" +
                    "\tpublic Result " + serverName + "(" + nameSpace + " " + fieldName + "){\n" +
                    "\t\t" + mapperFileds + "." + serverName + "(" + fieldName + ");\n" +
                    "\t\treturn Result.success();\n" +
                    "\t}");
            sb.append(updateText);

            // 删除实现
            StringBuilder deleteText = new StringBuilder();
            serverName = "delete" + nameSpace;
            deleteText.append("\n\n" +
                    "\t@Override\n" +
                    "\tpublic Result " + serverName + "(List<String> idList){\n" +
                    "\t\t" + mapperFileds + "." + serverName + "(idList);\n" +
                    "\t\treturn Result.success();\n" +
                    "\t}");

            sb.append(deleteText);


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

            String pojoJava = pojoPath.getText().substring(pojoPath.getText().indexOf("java") + 5);
            String pojoPackageText = pojoJava.replaceAll("([/\\\\])", ".") + "." + nameSpace;

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";" +
                    "\nimport com.wenge.appframe.core.bean.Result;" +
                    "\nimport java.util.List;" +
                    "\nimport " + pojoPackageText + ";" +
                    "\n\n/**\n" +
                    " * Description: " + tableComment + "服务类\n" +
                    " * @Author: " + author + "\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\npublic interface " + nameSpace + "Service" + " {";
            StringBuilder sb = new StringBuilder(text);

            String fieldName = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);

            // 新增服务
            StringBuilder addText = new StringBuilder();
            String serverName = "add" + nameSpace;
            addText.append("\n\n    Result " + serverName + "(" + nameSpace + " " + fieldName + ");");
            sb.append(addText);

            // 查询服务
            StringBuilder searchText = new StringBuilder();
            serverName = "get" + nameSpace + "List";
            searchText.append("\n\n    Result " + serverName + "(" + nameSpace + " " + fieldName + ");");
            sb.append(searchText);

            // 更新服务
            StringBuilder updateText = new StringBuilder();
            serverName = "update" + nameSpace;
            updateText.append("\n\n    Result " + serverName + "(" + nameSpace + " " + fieldName + ");");
            sb.append(updateText);

            // 删除服务
            StringBuilder deleteText = new StringBuilder();
            serverName = "delete" + nameSpace;
            deleteText.append("\n\n    Result " + serverName + "(List<String> idList);");
            sb.append(deleteText);

            sb.append("\n\n}");
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
                    "\t\n";
            StrBuilder sb = new StrBuilder(text);

            // 新增
            StrBuilder addText = new StrBuilder();


            addText.append("    <insert id=\"add" + nameSpace + "\">");
            addText.append("\n        insert into " + dbTabel.getText() + "(");

            boolean isFirst = true;
            for (List<String> strings : colDataList) {
                if (StringUtils.isNotBlank(strings.get(5)) || "id".equals(strings.get(2))) {
                    continue;
                }
                if (!isFirst) {
                    addText.append(",");
                }
                addText.append("\n                " + strings.get(2));
                isFirst = false;
            }
            addText.append(")");
            addText.append("\n        value (\n");

            isFirst = true;
            for (List<String> strings : colDataList) {
                if (StringUtils.isNotBlank(strings.get(5)) || "id".equals(strings.get(2))) {
                    continue;
                }
                if (!isFirst) {
                    addText.append(",\n");
                }
                addText.append("                #{param." + StrUtil.toCamelCase(strings.get(2)) + "}");
                isFirst = false;
            }

            addText.append("\n        )");

            addText.append("\n    </insert>\n\n");
            sb.append(addText);


            // 查询
            StrBuilder searchText = new StrBuilder();
            String pojoJava = pojoPath.getText().substring(pojoPath.getText().indexOf("java") + 5);
            String pojoPackageText = pojoJava.replaceAll("([/\\\\])", ".");

            searchText.append("    <select id=\"get" + nameSpace + "List\" resultType=\"" + pojoPackageText + "." + nameSpace +"\">");

            searchText.append("\n        SELECT");
            isFirst = true;
            for (List<String> strings : colDataList) {
                if (!isFirst) {
                    searchText.append(",");
                }
                searchText.append("\n                " + strings.get(2));
                isFirst = false;
            }
            searchText.append("\n        from " + dbTabel.getText());
            searchText.append("\n        <where>");
            searchText.append("\n                and delete_flag = 0");

            for (List<String> strings : colDataList) {
                searchText.append("\n            <if test = \"" + StrUtil.toCamelCase(strings.get(2)) + " != null and " + StrUtil.toCamelCase(strings.get(2)) + " != '' \">\n" +
                        "                and " + strings.get(2) + " = #{" + StrUtil.toCamelCase(strings.get(2)) + "}\n" +
                        "            </if>");

            }


            searchText.append("\n        </where>");
            searchText.append("\n       order by update_time desc");
            searchText.append("\n    </select>\n\n");
            sb.append(searchText);

            // 更新
            StrBuilder updateText = new StrBuilder();


            updateText.append("    <update id=\"update" + nameSpace + "\">");
            updateText.append("\n        update " + dbTabel.getText());
            updateText.append("\n        <set>");

            for (List<String> strings : colDataList) {
                if ("id".equals(strings.get(2))
                        || "create_time".equals(strings.get(2))
                        || "delete_flag".equals(strings.get(2))
                        || "update_time".equals(strings.get(2))) {
                    continue;
                }
                updateText.append("\n            " + strings.get(2) + " = #{param." + StrUtil.toCamelCase(strings.get(2)) + "},");
            }
            updateText.append("\n        </set>");
            updateText.append("\n        <where>");
            updateText.append("\n            id = #{param.id}");
            updateText.append("\n        </where>");

            updateText.append("\n    </update>");
            sb.append(updateText);


            // 删除
            StrBuilder deleteText = new StrBuilder();
            deleteText.append("\n\n    <delete id=\"delete" + nameSpace + "\">");
            deleteText.append("\n        delete from " + dbTabel.getText());
            deleteText.append("\n        <where>\n" +
                    "            id in\n" +
                    "            <foreach collection=\"idList\" item=\"item\" separator=\",\" open=\"(\" close=\")\">\n" +
                    "                #{item}\n" +
                    "            </foreach>\n" +
                    "        </where>\n" +
                    "    </delete>");
            sb.append(deleteText);



            sb.append("\n\n</mapper>");
            bw.write(sb.toString());
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

            String pojoPackageText = pojoPath.getText().substring(pojoPath.getText().indexOf("java") + 5);
            pojoPackageText = pojoPackageText.replaceAll("([/\\\\])", ".") + "." + nameSpace;

            String text = "package " + packageText + ";"
                    + "\n\nimport org.apache.ibatis.annotations.Mapper;"
                    + "\nimport org.apache.ibatis.annotations.Param;"
                    + "\nimport java.util.List;"
                    + "\n\nimport " + pojoPackageText + ";"
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

            // 新增
            String addText = "\n\n" +
                    "    /**\n" +
                    "     * 新增" + tableComment + "\n" +
                    "     */\n" +
                    "    void add" + nameSpace + "(@Param(\"param\") " + nameSpace + " " + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + ");";
            sb.append(addText);

            // 查询
            String searchText = "\n\n" +
                    "    /**\n" +
                    "     * 查询" + tableComment + "\n" +
                    "     */\n" +
                    "    List<" + nameSpace + "> get" + nameSpace + "List(" + nameSpace + " " + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + ");";
            sb.append(searchText);


            // 更新
            String updateText = "\n\n" +
                    "    /**\n" +
                    "     * 更新" + tableComment + "\n" +
                    "     */\n" +
                    "    void update" + nameSpace + "(@Param(\"param\") " + nameSpace + " " + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + ");";
            sb.append(updateText);


            // 删除
            String delteText = "\n\n" +
                    "    /**\n" +
                    "     * 删除" + tableComment + "\n" +
                    "     */\n" +
                    "    void delete" + nameSpace + "(@Param(\"idList\") List<String> idList);\n";
            sb.append(delteText);



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
            File file = new File(pojoPath.getText() + "/" + nameSpace + ".java");
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
            PreparedStatement preparedStatement = conn.prepareStatement(StrUtil.format("select  table_name,column_name,column_type, column_comment,column_default\n" +
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
            colDataList.clear();
            colDataList.addAll(dataList);

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
                    + "\n\nimport io.swagger.annotations.ApiModel;"
                    + "\nimport io.swagger.annotations.ApiModelProperty;"
                    + "\nimport lombok.Data;"
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
                    "\n@ApiModel" +
                    "\n@Data" +
                    "\n@EqualsAndHashCode(callSuper = false)\n" +
                    "public class " + nameSpace + "" + " implements Serializable {" +
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
                        "\t@ApiModelProperty(name = \"" + StrUtil.toCamelCase(tableInfo.getName()) + "\", value = \"" + tableInfo.getComment() + "\", dataType = \"" + tableInfo.getType() + "\")" +
                        "\n\tprivate " + tableInfo.getType() + " " + StrUtil.toCamelCase(tableInfo.getName()) + ";";
                sb.append(filed);
            }

            sb.append("\n\n" +
                    "\t/**\n" +
                    "\t * 页码\n" +
                    "\t */\n" +
                    "\tprivate Integer pageNo;\n" +
                    "\n" +
                    "\t/**\n" +
                    "\t * 记录数\n" +
                    "\t */\n" +
                    "\tprivate Integer pageSize;\n");

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
        map.put("tinyint", "Integer");
        map.put("TINYINT", "Integer");
        map.put("int", "Integer");
        map.put("INT", "Integer");
        map.put("bigint", "Long");
        map.put("BIGINT", "Long");
        map.put("decimal", "BigDecimal");
        map.put("DECIMAL", "BigDecimal");
        map.put("datetime", "String");
        map.put("DATETIME", "String");
        map.put("timestamp", "String");
        map.put("TIMESTAMP", "String");
        map.put("numeric", "Integer");
        map.put("NUMERIC", "Integer");
        map.put("text", "String");
        map.put("TEXT", "String");
        map.put("longtext", "String");
        map.put("LONGTEXT", "String");
        map.put("date", "String");
        map.put("DATE", "String");
        return map;
    }

    private void createHooks() {
        String hooksPathText = hooksPath.getText();
        if (StringUtils.isNotBlank(hooksPathText)) {
            createUrlTs();
            createApiTs();
            createAddTs();
            createDeleteTs();
            createEditFormTs();
            createFormatterTs();
            createGetData();
            createOperaConfig();
            createOption();
            createPageTs();
            createTableConfig();
            createUpdateTs();
        }
    }

    private void createUrlTs() {
        String addUrl = "\n\nexport const ADD_" + dbTabelAlia.getText().toUpperCase() + "_URL:string = '/" + StrUtil.toCamelCase(dbTabelAlia.getText()) + "/add" + nameSpace + "'";
        String updateUrl = "\n\nexport const UPDATE_" + dbTabelAlia.getText().toUpperCase() + "_URL:string = '/" + StrUtil.toCamelCase(dbTabelAlia.getText()) + "/update" + nameSpace + "'";
        String getUrl = "\n\nexport const GET_" + dbTabelAlia.getText().toUpperCase() + "_URL:string = '/" + StrUtil.toCamelCase(dbTabelAlia.getText()) + "/get" + nameSpace + "List'";
        String deleteUrl = "\n\nexport const DELETE_" + dbTabelAlia.getText().toUpperCase() + "_URL:string = '/" + StrUtil.toCamelCase(dbTabelAlia.getText()) + "/delete" + nameSpace + "'";

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/api/url.ts"));
        StringBuilder sb = new StringBuilder();
        sb.append(addUrl);
        sb.append(updateUrl);
        sb.append(getUrl);
        sb.append(deleteUrl);
        try {
            write(sb.toString(), hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/api/url.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createApiTs() {
        String addApi = "\n\nexport const add" + nameSpace + "Api = function (param: any) {\n" +
                "    return POST(url.ADD_" + dbTabelAlia.getText().toUpperCase() + "_URL, param)\n" +
                "};";

        String getApi = "\n\nexport const get" + nameSpace + "ListApi = function (param: any) {\n" +
                "    return POST(url.GET_" + dbTabelAlia.getText().toUpperCase() + "_URL, param)\n" +
                "};\n";

        String updateApi = "\n\nexport const update" + nameSpace + "Api = function (param: any) {\n" +
                "    return POST(url.UPDATE_" + dbTabelAlia.getText().toUpperCase() + "_URL, param)\n" +
                "};";

        String deleteApi = "\n\nexport const delete" + nameSpace + "Api = function (param: string[]) {\n" +
                "    return POST(url.DELETE_" + dbTabelAlia.getText().toUpperCase() + "_URL, param)\n" +
                "};";

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/api/api.ts"));
        StringBuilder sb = new StringBuilder();
        sb.append(addApi);
        sb.append(getApi);
        sb.append(updateApi);
        sb.append(deleteApi);
        try {
            write(sb.toString(), hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/api/api.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAddTs() {
        String api = "add" + nameSpace + "Api";
        String module = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);
        String addTs = "import {" + api + "} from \"@/api/api\";\n" +
                "import {RES_CODE} from \"@/api/url\";\n" +
                "import {ElMessage} from \"element-plus\";\n" +
                "import getData from \"@/hooks/" + module + "/getData\";\n" +
                "import editForm from \"@/hooks/" + module + "/editForm\";\n" +
                "\n" +
                "export default function () {\n" +
                "    " + api + "(editForm.form)\n" +
                "        .then((res: any) => {\n" +
                "            let {code, msg} = res;\n" +
                "            if (code === RES_CODE) {\n" +
                "                ElMessage({\n" +
                "                    message: '成功',\n" +
                "                    type: 'success',\n" +
                "                });\n" +
                "                getData();\n" +
                "                editForm.visible = false;\n" +
                "            } else {\n" +
                "                ElMessage({\n" +
                "                    message: msg,\n" +
                "                    type: 'error',\n" +
                "                });\n" +
                "            }\n" +
                "        });\n" +
                "};\n";

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/add.ts"));
        try {
            write(addTs, hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/add.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDeleteTs() {
        String api = "delete" + nameSpace + "Api";
        String module = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);

        String deleteText = "// 删除数据\n" +
                "import {ElMessage, ElMessageBox} from \"element-plus\";\n" +
                "import {" + api + "} from \"@/api/api\";\n" +
                "import {RES_CODE} from \"@/api/url\";\n" +
                "import getData from \"@/hooks/" + module + "/getData\";\n" +
                "\n" +
                "export default function (idList: any[]) {\n" +
                "    if (!idList || idList.length == 0) {\n" +
                "        ElMessage({\n" +
                "            message: '请选择你要删除的数据',\n" +
                "            type: 'warning',\n" +
                "        });\n" +
                "        return\n" +
                "    }\n" +
                "\n" +
                "    ElMessageBox.confirm(\n" +
                "        '确认删除?',\n" +
                "        '删除模块',\n" +
                "        {\n" +
                "            confirmButtonText: '确认',\n" +
                "            cancelButtonText: '取消',\n" +
                "            type: 'warning',\n" +
                "        }\n" +
                "    )\n" +
                "        .then(() => {\n" +
                "            " + api + "(idList)\n" +
                "                .then((data: any) => {\n" +
                "                    if (RES_CODE === data.code) {\n" +
                "                        ElMessage({\n" +
                "                            message: '删除成功',\n" +
                "                            type: 'success',\n" +
                "                        });\n" +
                "                        getData();\n" +
                "                    }\n" +
                "                })\n" +
                "                .catch(error => {\n" +
                "                    console.log(error);\n" +
                "                    ElMessage({\n" +
                "                        message: '删除失败',\n" +
                "                        type: 'error',\n" +
                "                    });\n" +
                "                })\n" +
                "        })\n" +
                "        .catch(() => {\n" +
                "            ElMessage({\n" +
                "                type: 'info',\n" +
                "                message: '已取消',\n" +
                "            })\n" +
                "        });\n" +
                "\n" +
                "};\n";

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/delete.ts"));
        try {
            write(deleteText, hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/delete.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createEditFormTs() {
        StringBuilder sb = new StringBuilder();
        sb.append("import {reactive} from \"vue\";\n" +
                "\n" +
                "export default reactive({\n" +
                "    visible: false,\n" +
                "    tile: '',\n" +
                "    width: 500,\n" +
                "    form: {},\n" +
                "    action: '',\n" +
                "    disabled: [] as string[],\n" +
                "    props: [");

        for (List<String> strings : colDataList) {
            if (StringUtils.isNotBlank(strings.get(5)) || "id".equals(strings.get(2))) {
                continue;
            }
            sb.append("\n        {label: '" + strings.get(4) + "', type: 'input', prop: '" + StrUtil.toCamelCase(strings.get(2)) + "', width: 80, filed: '" + StrUtil.toCamelCase(strings.get(2)) + "'},");
        }

        sb.append("\n    ],");
        sb.append("\n    rules: {");
        for (List<String> strings : colDataList) {
            if (StringUtils.isNotBlank(strings.get(5)) || "id".equals(strings.get(2))) {
                continue;
            }

            sb.append("\n        " + StrUtil.toCamelCase(strings.get(2)) + ": [\n" +
                    "            {required: true, message: '" + strings.get(4) + "不能为空', trigger: 'blur'},\n" +
                    "        ],");
        }

        sb.append("\n    }\n" +
                "});");

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/editForm.ts"));
        try {
            write(sb.toString(), hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/editForm.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFormatterTs() {
        String text = "export const formatters = function (row: any) {\n" +
                "    return row.id\n" +
                "};\n";
        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/formatters.ts"));
        try {
            write(text, hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/formatters.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGetData() {
        String api = "get" + nameSpace + "ListApi";
        String module = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);

        String text = "// 查询表格数据\n" +
                "import operaConfig from \"@/hooks/" + module + "/operaConfig\";\n" +
                "import {" + api + "} from \"@/api/api\";\n" +
                "import tableInfo from \"@/hooks/" + module + "/tableInfo\";\n" +
                "\n" +
                "export default function () {\n" +
                "    tableInfo.loading = true;\n" +
                "    let param = {\n" +
                "        ...operaConfig.param,\n" +
                "        pageNo: tableInfo.pageInfo.currentPage,\n" +
                "        pageSize: tableInfo.pageInfo.pageSize,\n" +
                "    };\n" +
                "    " + api + "(param as any)\n" +
                "        .then(res => {\n" +
                "            tableInfo.tableList = res.data.list;\n" +
                "\n" +
                "            tableInfo.pageInfo.total = res.data.total;\n" +
                "            tableInfo.loading = false;\n" +
                "        }, error => {\n" +
                "            tableInfo.loading = false;\n" +
                "        });\n" +
                "};\n";

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/getData.ts"));
        try {
            write(text, hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/getData.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createOperaConfig() {
        String module = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);

        StringBuilder sb = new StringBuilder();
        sb.append("import {reactive} from \"vue\";\n" +
                "import editForm from \"@/hooks/" + module + "/editForm\";\n" +
                "import deleteBatch from \"@/hooks/" + module + "/delete\";\n" +
                "import getData from \"@/hooks/" + module + "/getData\";\n" +
                "import {statusOptionList} from \"@/hooks/" + module + "/options\";\n" +
                "import {selected} from \"@/hooks/" + module + "/page\";\n" +
                "\n" +
                "export default reactive({\n" +
                "    param: {},\n" +
                "    labelWidth: 100,\n" +
                "    fieldList: [");

        for (List<String> strings : colDataList) {
            if (StringUtils.isNotBlank(strings.get(5)) || "id".equals(strings.get(2))) {
                continue;
            }
            sb.append("\n" +
                    "        {\n" +
                    "            label: '" + strings.get(4) + "',\n" +
                    "            field: '" + StrUtil.toCamelCase(strings.get(2)) + "',\n" +
                    "            type: 'input',\n" +
                    "            placeholder: '" + strings.get(4) + "',\n" +
                    "        },");
        }

        sb.append("\n" +
                "        {\n" +
                "            text: '查询',\n" +
                "            type: 'button',\n" +
                "            backGround: 'primary',\n" +
                "            click() {\n" +
                "                getData();\n" +
                "            }\n" +
                "        }],\n" +
                "    butList: [\n" +
                "        {\n" +
                "            type: 'success',\n" +
                "            round: true,\n" +
                "            show: true,\n" +
                "            size: 'small',\n" +
                "            text: \"新增\",\n" +
                "            click() {\n" +
                "                editForm.form = {};\n" +
                "                editForm.tile = '新增模块';\n" +
                "                editForm.action = 'add';\n" +
                "                editForm.disabled = [];\n" +
                "                editForm.visible = true;\n" +
                "            },\n" +
                "        },\n" +
                "        {\n" +
                "            type: 'danger',\n" +
                "            round: true,\n" +
                "            show: true,\n" +
                "            size: 'small',\n" +
                "            text: \"删除\",\n" +
                "            click() {\n" +
                "                const idList: string[] = selected.map((item: any) => item.id as string);\n" +
                "                deleteBatch(idList);\n" +
                "            },\n" +
                "        }],\n" +
                "});");

//        System.out.println(sb.toString());

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/operaConfig.ts"));
        try {
            write(sb.toString(), hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/operaConfig.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createOption() {
        String text = "export const statusOptionList = [\n" +
                "    {\n" +
                "        label: '',\n" +
                "        value: '',\n" +
                "    }\n" +
                "];\n" +
                "\n";

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/options.ts"));
        try {
            write(text, hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/options.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPageTs() {
        String module = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);
        String text = "// 表格多选时，选中的数据集合\n" +
                "import {reactive} from \"vue\";\n" +
                "import tableInfo from \"@/hooks/" + module + "/tableInfo\";\n" +
                "import getData from \"@/hooks/" + module + "/getData\";\n" +
                "\n" +
                "export const selected = reactive<any[]>([]);\n" +
                "\n" +
                "// 表格选中事件\n" +
                "export const selectChange = (val: any[]) => {\n" +
                "    selected.splice(0, selected.length);\n" +
                "\n" +
                "    val.forEach((item: any) => {\n" +
                "        selected.push(item);\n" +
                "    });\n" +
                "}\n" +
                "\n" +
                "// 表格分页pageSize改变事件\n" +
                "export const sizeChange = (pageSize: number) => {\n" +
                "    tableInfo.pageInfo.pageSize = pageSize;\n" +
                "    tableInfo.pageInfo.currentPage = 1;\n" +
                "    getData();\n" +
                "};\n" +
                "\n" +
                "// 表格分页pagaNo改变事件\n" +
                "export const currentChange = (pageNo: number) => {\n" +
                "    tableInfo.pageInfo.currentPage = pageNo;\n" +
                "    getData();\n" +
                "}\n";


        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/page.ts"));
        try {
            write(text, hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/page.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTableConfig() {
        String module = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);

        StringBuilder sb = new StringBuilder();
        sb.append("// 表格配置\n" +
                "import {reactive} from \"vue\";\n" +
                "import editForm from \"@/hooks/" + module + "/editForm\";\n" +
                "import deleteBatch from \"@/hooks/" + module + "/delete\";\n" +
                "\n" +
                "export default reactive({\n" +
                "    props: [");

        for (List<String> strings : colDataList) {
            if (StringUtils.isNotBlank(strings.get(5)) || "id".equals(strings.get(2))) {
                continue;
            }
            sb.append("\n" +
                    "        {label: '" + strings.get(4) + "', filed: '" + StrUtil.toCamelCase(strings.get(2)) + "', type: 'none', minWidth: 120,},");
        }

        sb.append("\n" +
                "    ],\n" +
                "    operaList: {\n" +
                "        label: '操作',\n" +
                "        minWidth: 120,\n" +
                "        butList: [\n" +
                "            {\n" +
                "                text: '编辑',\n" +
                "                background: 'success',\n" +
                "                plain: true,\n" +
                "                show: true,\n" +
                "                size: 'small',\n" +
                "                click(row: any) {\n" +
                "                    editForm.form = Object.assign({}, row);\n" +
                "                    editForm.tile = '编辑模块';\n" +
                "                    editForm.disabled = ['code', 'name'];\n" +
                "                    editForm.action = 'update';\n" +
                "                    editForm.visible = true;\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                text: '删除',\n" +
                "                background: 'danger',\n" +
                "                plain: true,\n" +
                "                show: true,\n" +
                "                size: 'small',\n" +
                "                click(row: any) {\n" +
                "                    let idList = [row.id];\n" +
                "                    deleteBatch(idList);\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    tableList: [],\n" +
                "    selectionFlag: true,\n" +
                "    pageInfo: {\n" +
                "        currentPage: 1,\n" +
                "        pageSize: 10,\n" +
                "        total: 0,\n" +
                "    },\n" +
                "    loading: false\n" +
                "});\n");

        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/tableInfo.ts"));
        try {
            write(sb.toString(), hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/tableInfo.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createUpdateTs() {
        String module = nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1);
        String api = "update" + nameSpace + "Api";

        String text = "// 表单提交\n" +
                "import editForm from \"@/hooks/" + module + "/editForm\";\n" +
                "import {" + api + "} from \"@/api/api\";\n" +
                "import {RES_CODE} from \"@/api/url\";\n" +
                "import {ElMessage} from \"element-plus\";\n" +
                "import getData from \"@/hooks/" + module + "/getData\";\n" +
                "\n" +
                "export default function () {\n" +
                "    // 更新模块\n" +
                "    " + api + "(editForm.form)\n" +
                "        .then((res: any) => {\n" +
                "            let {code, msg} = res;\n" +
                "            if (code === RES_CODE) {\n" +
                "                ElMessage({\n" +
                "                    message: '成功',\n" +
                "                    type: 'success',\n" +
                "                });\n" +
                "                getData();\n" +
                "                editForm.visible = false;\n" +
                "            } else {\n" +
                "                ElMessage({\n" +
                "                    message: msg,\n" +
                "                    type: 'error',\n" +
                "                });\n" +
                "            }\n" +
                "        });\n" +
                "};\n";


        fileList.add(new File(hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/update.ts"));
        try {
            write(text, hooksPath.getText() + "/" + nameSpace.substring(0, 1).toLowerCase() + nameSpace.substring(1) + "/update.ts");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void write(String text, String fileName) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {

            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            bw = new BufferedWriter(outputStreamWriter);
            bw.write(text);
            bw.flush();
        } catch (Exception e) {
            Messages.showInfoMessage("创建文件异常", "提示");
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

}

package com.easy.core.ui.frame;

import cn.hutool.core.util.StrUtil;
import com.easy.core.entity.TableInfo;
import com.easy.core.listener.DocumentListener;
import com.google.common.collect.Maps;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
    private JTextField mapperPath;
    private JTextField xmlPath;
    private JTextField servicePath;
    private JTextField controllerPath;
    private JTextField pojoPath;
    private String tableName;

    public GeneratorFrame() {
        super(new BorderLayout());
        root = new JBPanel(new BorderLayout());
        add(root);
        GridLayout gridLayout = new GridLayout(5, 2);
        gridLayout.setVgap(50);
        gridLayout.setHgap(10);
        parentPanel = new JBPanel(gridLayout);
        JBScrollPane scrollPane = new JBScrollPane(parentPanel);
        root.add(scrollPane);

        initUI();
    }

    private void initUI() {
        Color labelBackground = UIUtil.getLabelBackground();
        JPanel db = new JPanel(new BorderLayout());
        db.add(new JLabel("数据库链接"), BorderLayout.WEST);
        db.add(dbUrl = new JTextField("数据库链接串"), BorderLayout.CENTER);
        parentPanel.add(db);

        JPanel db2 = new JPanel(new BorderLayout());
        db2.add(new JLabel("用户名"), BorderLayout.WEST);
        db2.add(dbUser = new JTextField("用户名"), BorderLayout.CENTER);
        parentPanel.add(db2);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.add(new JLabel("密码"), BorderLayout.WEST);
        panel3.add(dbPassword = new JTextField("密码"), BorderLayout.CENTER);
        parentPanel.add(panel3);

        JPanel panel9 = new JPanel(new BorderLayout());
        panel9.add(new JLabel("表名"), BorderLayout.WEST);
        panel9.add(dbTabel = new JTextField("表名"), BorderLayout.CENTER);
        parentPanel.add(panel9);

        JPanel panel4 = new JPanel(new BorderLayout());
        panel4.add(new JLabel("mapper路径"), BorderLayout.WEST);
        panel4.add(mapperPath = new JTextField("mapper路径"), BorderLayout.CENTER);
        mapperPath.setMaximumSize(new Dimension(100, 50));
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

        JPanel panel5 = new JPanel(new BorderLayout());
        panel5.add(new JLabel("xml路径"), BorderLayout.WEST);
        panel5.add(xmlPath = new JTextField("xml路径"), BorderLayout.CENTER);
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

        JPanel panel6 = new JPanel(new BorderLayout());
        panel6.add(new JLabel("service路径"), BorderLayout.WEST);
        panel6.add(servicePath = new JTextField("service路径"), BorderLayout.CENTER);
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

        JPanel panel7 = new JPanel(new BorderLayout());
        panel7.add(new JLabel("controller路径"), BorderLayout.WEST);
        panel7.add(controllerPath = new JTextField("controller路径"), BorderLayout.CENTER);
        Button button4 = new Button("浏览");
        button4.setBackground(labelBackground);
        button4.addActionListener(e->{
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


        JPanel panel8 = new JPanel(new BorderLayout());
        panel8.add(new JLabel("pojo路径"), BorderLayout.WEST);
        panel8.add(pojoPath = new JTextField("pojo路径"), BorderLayout.CENTER);
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
        button.addActionListener((e) -> {
            generator();
            Messages.showInfoMessage("代码已生成,请刷新编译器", "代码生成提示");
        });
        panel10.add(button, BorderLayout.WEST);
        parentPanel.add(panel10);
    }

    private void generator() {
        try {
            createFile();
        } catch (Exception e) {
            System.out.println("===d=dd==d=d=");
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
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {

            File file = new File(controllerPath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Controller.java");
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
            String packageText = java.replace("\\", ".");

            String servicePath1 = servicePath.getText().substring(servicePath.getText().indexOf("java") + 5);
            String packageText1 = servicePath1.replace("\\", ".") + "." + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Service";

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport " + packageText1 + ";" +
                    "\nimport org.springframework.beans.factory.annotation.Autowired;" +
                    "\nimport org.springframework.web.bind.annotation.RequestMapping;" +
                    "\nimport org.springframework.web.bind.annotation.RestController;" +
                    "\nimport lombok.extern.slf4j.Slf4j;" +
                    "\n\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@RestController" +
                    "\n@RequestMapping(\"/" + StrUtil.toCamelCase(dbTabel.getText()) + "\")" +
                    "\n@Slf4j" +
                    "\npublic class " + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Controller {";
            StringBuilder sb = new StringBuilder(text);
            String serviceFiled = StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Service";
            String serviceFileds = serviceFiled.substring(0, 1).toLowerCase() + serviceFiled.substring(1);
            sb.append("\n\n\t/**\n" +
                    "\t * \t获取CDM信息服务数据访问接口\n" +
                    "\t */\n" +
                    "\t@Autowired\n" +
                    "\tprivate " + serviceFiled + " " + serviceFileds + ";");
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
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
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File fileImpl = new File(servicePath.getText() + "/impl/" + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "ServiceImpl.java");
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
            String packageText = java.replace("\\", ".") + ".impl";

            String service = java.replace("\\", ".") + "." + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1);
            String mapper = java.replace("\\", ".") + "." + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1);

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport " + service + "Service;" +
                    "\nimport " + mapper + "Mapper;" +
                    "\nimport org.springframework.stereotype.Service;" +
                    "\nimport lombok.extern.slf4j.Slf4j;" +
                    "\nimport org.springframework.beans.factory.annotation.Autowired;" +
                    "\n\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@Service" +
                    "\n@Slf4j" +
                    "\npublic class " + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "ServiceImpl implements " + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Service" + " {";
            StringBuilder sb = new StringBuilder(text);
            String mapperFiled = StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Mapper";
            String mapperFileds = mapperFiled.substring(0, 1).toLowerCase() + mapperFiled.substring(1);
            sb.append("\n\t/**\n" +
                    "\t * \t获取CDM信息服务数据访问接口\n" +
                    "\t */\n" +
                    "\t@Autowired\n" +
                    "\tprivate " + mapperFiled + " " + mapperFileds + ";");
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
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
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File file = new File(servicePath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Service.java");
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
            String java = mapperPath.getText().substring(servicePath.getText().indexOf("java") + 5);
            String packageText = java.replace("\\", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\npublic interface " + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Service" + " {";
            StringBuilder sb = new StringBuilder(text);
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
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
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File file = new File(xmlPath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()) + "Mapper.xml");
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
            String packageText = java.replace("\\", ".");
            String namespace = packageText + "." + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Mapper";
            String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" +
                    "<!-- 映射文件，映射到对应的SQL接口 -->\n" +
                    "<mapper namespace=\"" + namespace + "\">\n" +
                    "\t\n" +
                    "</mapper>";
            bw.write(text);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
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
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File file = new File(mapperPath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Mapper.java");
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
            String packageText = java.replace("\\", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport org.apache.ibatis.annotations.Mapper;"
                    + "\n\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@Mapper" +
                    "\npublic interface " + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "Mapper" + " {";
            StringBuilder sb = new StringBuilder(text);
            sb.append("\n}");
            bw.write(sb.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
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
        FileOutputStream fos = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            File file = new File(pojoPath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "POJO.java");
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
            String packageText = java.replace("\\", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n\nimport lombok.Data;"
                    + "\nimport lombok.EqualsAndHashCode;"
                    + "\nimport java.io.Serializable;"
                    + "\n\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@Data" +
                    "\n@EqualsAndHashCode(callSuper = false)\n" +
                    "public class " + StrUtil.toCamelCase(dbTabel.getText()).substring(0, 1).toUpperCase() + StrUtil.toCamelCase(dbTabel.getText()).substring(1) + "POJO" + " implements Serializable {";
            StringBuilder sb = new StringBuilder(text);

            Connection conn = DriverManager.getConnection(dbUrl.getText(), dbUser.getText(), dbPassword.getText());
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("select  table_name,column_name,column_type, column_comment,table_comment from information_schema.columns where table_schema ='表所在的库'  and table_name = '要查看的表名' ;" + dbTabel.getText());
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

            List<TableInfo> tableInfoList = Lists.newArrayList();
            for (List<String> strings : dataList) {
                tableInfoList.add(new TableInfo(strings.get(0), getTypeMp().get(strings.get(1)), strings.get(2)));
            }


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
            e.printStackTrace();
        }finally {
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
        map.put("datetime", "Date");
        return map;
    }
}

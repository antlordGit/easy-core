package com.easy.core.ui.frame;

import cn.hutool.core.util.StrUtil;
import com.easy.core.entity.TableInfo;
import com.easy.core.listener.DocumentListener;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import org.apache.commons.compress.utils.Lists;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        gridLayout.setVgap(20);
        gridLayout.setHgap(20);
        parentPanel = new JBPanel(new GridLayout(5, 2));
        JBScrollPane scrollPane = new JBScrollPane(parentPanel);
        root.add(scrollPane);

        initUI();
    }

    private void initUI() {
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

        Button button = new Button("创建文件");
        button.addActionListener((e) -> {
            generator();
        });
        parentPanel.add(button);
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

            File fileImpl = new File(controllerPath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()) + "Controller.java");
            if (!fileImpl.exists()) {
                fileImpl.createNewFile();
            }

            fos = new FileOutputStream(fileImpl);
            outputStreamWriter = new OutputStreamWriter(fos);
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = mapperPath.getText().substring(mapperPath.getText().indexOf("java") + 4);
            String packageText = java.replace("/", ".");

            String servicePath1 = servicePath.getText().substring(servicePath.getText().indexOf("java") + 4);
            String packageText1 = servicePath1.replace("/", ".") + StrUtil.toCamelCase(dbTabel.getText()) + "Service.java";


            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\nimport " + packageText1 + ";" +
                    "\nimport org.springframework.beans.factory.annotation.Autowired;" +
                    "\nimport org.springframework.web.bind.annotation.RequestMapping;" +
                    "\nimport org.springframework.web.bind.annotation.RestController;" +
                    "\nimport lombok.extern.slf4j.Slf4j;" +
                    "\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@RestController" +
                    "\n@RequestMapping(\"/" + StrUtil.toCamelCase(dbTabel.getText()) + "\")" +
                    "\n@Slf4j" +
                    "public class " + StrUtil.toCamelCase(dbTabel.getText()) + "Controller {";
            StringBuilder sb = new StringBuilder(text);
            String serviceFiled = StrUtil.toCamelCase(dbTabel.getText()) + "Service";
            String serviceFileds = serviceFiled.substring(0, 1).toLowerCase() + serviceFiled.substring(1);
            sb.append("\n\t/**\n" +
                    "\t * \t获取CDM信息服务数据访问接口\n" +
                    "\t */\n" +
                    "\t@Autowired\n" +
                    "\tprivate " + serviceFiled + " " + serviceFileds);
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

            File fileImpl = new File(servicePath.getText() + "/impl/" + StrUtil.toCamelCase(dbTabel.getText()) + "ServiceImpl.java");
            if (!fileImpl.exists()) {
                fileImpl.createNewFile();
            }

            fos = new FileOutputStream(fileImpl);
            outputStreamWriter = new OutputStreamWriter(fos);
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = mapperPath.getText().substring(servicePath.getText().indexOf("java") + 4);
            String packageText = java.replace("/", ".") + ".impl";
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\nimport " + packageText + "Service;" +
                    "\nimport org.springframework.stereotype.Service;" +
                    "\nimport lombok.extern.slf4j.Slf4j;" +
                    "\nimport org.springframework.beans.factory.annotation.Autowired;" +
                    "\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "@Service" +
                    "@Slf4j" +
                    "public class " + StrUtil.toCamelCase(dbTabel.getText()) + "ServiceImpl implements " + StrUtil.toCamelCase(dbTabel.getText()) + "Service" + " {;";
            StringBuilder sb = new StringBuilder(text);
            String mapperFiled = StrUtil.toCamelCase(dbTabel.getText()) + "Mapper";
            String mapperFileds = mapperFiled.substring(0, 1).toLowerCase() + mapperFiled.substring(1);
            sb.append("\t/**\n" +
                    "\t * \t获取CDM信息服务数据访问接口\n" +
                    "\t */\n" +
                    "\t@Autowired\n" +
                    "\tprivate " + mapperFiled + " " + mapperFileds);
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
            File file = new File(servicePath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()) + "Service.java");
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos);
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = mapperPath.getText().substring(servicePath.getText().indexOf("java") + 4);
            String packageText = java.replace("/", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\npublic interface " + StrUtil.toCamelCase(dbTabel.getText()) + "Service" + " {";
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
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos);
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = mapperPath.getText().substring(mapperPath.getText().indexOf("java") + 4);
            String packageText = java.replace("/", ".");
            String namespace = packageText + "." + StrUtil.toCamelCase(dbTabel.getText()) + "Mapper";
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
            File file = new File(mapperPath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()) + "Mapper.java");
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos);
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = mapperPath.getText().substring(mapperPath.getText().indexOf("java") + 4);
            String packageText = java.replace("/", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\nimport org.apache.ibatis.annotations.Mapper;"
                    + "\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@Mapper" +
                    "\npublic interface " + StrUtil.toCamelCase(dbTabel.getText()) + "Mapper" + " {";
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
            File file = new File(pojoPath.getText() + "/" + StrUtil.toCamelCase(dbTabel.getText()) + "POJO.java");
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fos);
            bw = new BufferedWriter(outputStreamWriter);
            // bw.newLine();
            String java = pojoPath.getText().substring(pojoPath.getText().indexOf("java") + 4);
            String packageText = java.replace("/", ".");
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String text = "package " + packageText + ";"
                    + "\nimport lombok.Data;"
                    + "\nimport lombok.EqualsAndHashCode;"
                    + "\nimport java.io.Serializable;"
                    + "\n/**\n" +
                    " * Description: \n" +
                    " * Author: chenzhiwei\n" +
                    " * Version: 1.0\n" +
                    " * Create Date Time: " + time + "\n" +
                    " *\n" +
                    " */" +
                    "\n@Data" +
                    "\n@EqualsAndHashCode(callSuper = false)\n" +
                    "public class " + StrUtil.toCamelCase(dbTabel.getText()) + "POJO" + " implements Serializable {";
            StringBuilder sb = new StringBuilder(text);

//            Connection conn = DriverManager.getConnection(dbUrl.getText(), dbUser.getText(), dbPassword.getText());
//            Statement stmt = conn.createStatement();
//            ResultSet resultSet = stmt.executeQuery("select  table_name,column_name,column_type, column_comment,table_comment from information_schema.columns where table_schema ='表所在的库'  and table_name = '要查看的表名' ;" + dbTabel.getText());
//            int columnCount = resultSet.getMetaData().getColumnCount();
//            List<String> titleList = Lists.newArrayList();
            List<List<String>> dataList = Lists.newArrayList();
//            for (int i = 1; i <= columnCount; i++) {
//                titleList.add(resultSet.getMetaData().getColumnName(i));
//            }
//            //获取查询结果
//            int rowNum = 0;
//            while (resultSet.next()) {
//                rowNum++;
//                List<String> rowData = Lists.newArrayList();
//                rowData.add(rowNum + "");
//                for (int i = 1; i <= columnCount; i++) {
//                    rowData.add(resultSet.getString(i));
//                }
//                dataList.add(rowData);
//            }

            List<TableInfo> tableInfoList = Lists.newArrayList();
            for (List<String> strings : dataList) {
//                tableInfoList.add(new TableInfo(strings.get(1), strings.get(2), strings.get(3)));
                tableInfoList.add(new TableInfo("userName", "String", "用户名称"));
            }

            String filed = "";
            for (TableInfo tableInfo : tableInfoList) {
                filed = "\n\t/**\n" +
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
}

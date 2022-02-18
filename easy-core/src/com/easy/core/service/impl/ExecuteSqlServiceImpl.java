package com.easy.core.service.impl;

import com.easy.core.service.ExecuteSqlService;
import com.easy.core.ui.EasyFrameFactory;
import com.easy.core.ui.frame.DatabaseFrame;
import com.easy.core.util.MybatisXmlUtil;
import com.easy.core.util.SqlFormatterUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class ExecuteSqlServiceImpl implements ExecuteSqlService {

    private PsiElement psiElement;

    @Override
    public void setPsiElement(PsiElement psiElement) {
        this.psiElement = psiElement;
    }

    @Override
    public void execute(Project project) {
        try {
            //从PinganTool窗口中获取Database框
            DatabaseFrame databaseFrame = (DatabaseFrame) EasyFrameFactory.getComponent("Database", project);
//        DatabaseFrame databaseFrame = DatabaseFrame.INSTANCE;
            String sql = null;
            if (null != psiElement) {
                StringBuilder sqlBuild = new StringBuilder();
                //解析XML元素配置中的Sql
                MybatisXmlUtil.resolution(psiElement, sqlBuild);
                //替换MybatisXMl文件中的动态参以及转义符<![CDATA[]]>
                sql = sqlBuild.toString().replaceAll("#\\{[,. =a-zA-Z0-9]*}", "?").replace("<![CDATA[", "").replace("]]>", "");
                // SqlFormatter sqlFormatter = new SqlFormatterImpl();
               //  将格式化后的sql放入文本框中
                databaseFrame.setSql(SqlFormatterUtil.format(sql));
            } else {
                sql = databaseFrame.getSql();
            }
            //代码重复 TODO
//            SqlFormatter sqlFormatter = new SqlFormatterImpl();
            databaseFrame.setSql(SqlFormatterUtil.format(sql));
            //sql非空判断、包含"?"号
            if (StringUtils.isBlank(sql) || sql.contains("?")) {
                databaseFrame.setTable(null, null, project);
                Messages.showWarningDialog("请初始化sql", "SQL异常");
                return;
            }
            //全局搜索项目中PsiFile文件
            PsiFile[] filesByName = FilenameIndex.getFilesByName(project, "local.test.druid.properties", GlobalSearchScope.projectScope(project));
            if (filesByName.length > 0) {
                InputStream inputStream = null;
                Connection conn = null;
                Statement stmt = null;
                try {
                    //获取文件流
                    inputStream = filesByName[0].getVirtualFile().getInputStream();
                    Properties properties = new Properties();
                    properties.load(inputStream);
                    //判断数据库连接信息
                    if (StringUtils.isNotBlank(properties.getProperty("url")) && StringUtils.isNotBlank(properties.getProperty("username"))
                            && StringUtils.isNotBlank(properties.getProperty("password")) && StringUtils.isNotBlank(properties.getProperty("driverClassName"))) {

                        List<List<String>> dataList = Lists.newArrayList();
                        List<String> titleList = Lists.newArrayList();
                        //获取数据库驱动
                        String driverClassName = properties.getProperty("driverClassName");
                        String executeSql = "";
                        //判断数据库类型，进行分页
                        if (driverClassName.toLowerCase().contains("mysql")) {
                            if (!databaseFrame.getSql().toLowerCase().contains("limit") && databaseFrame.getSql().trim().toLowerCase().startsWith("select")) {
                                executeSql = "SELECT * FROM (" + databaseFrame.getSql() + ") newtable limit 0,5";
                            }
                        } else if (driverClassName.toLowerCase().contains("oracle")) {
                            if (!databaseFrame.getSql().toLowerCase().contains("rownum") && databaseFrame.getSql().trim().toLowerCase().startsWith("select")) {
                                executeSql = "SELECT * FROM (" + databaseFrame.getSql().toLowerCase().replaceFirst("select", " select rownum num, ") + ") NEWTABEL where NEWTABEL.num >=0 and NEWTABEL.num <= 10";
                            }
                        }
                        //executeSql非空判断
                        if (StringUtils.isBlank(executeSql)) {
                            executeSql = databaseFrame.getSql();
                        }
                        //建立数据库连接
                        Class.forName(properties.getProperty("driverClassName"));
                        conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
                        stmt = conn.createStatement();
                        //执行sql
                        ResultSet resultSet = stmt.executeQuery(executeSql);
                        int rowNum = 0;
                        //获取列名
                        int columnCount = resultSet.getMetaData().getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            titleList.add(resultSet.getMetaData().getColumnName(i));
                        }
                        //获取查询结果
                        while (resultSet.next()) {
                            rowNum++;
                            List<String> rowData = Lists.newArrayList();
                            rowData.add(rowNum + "");
                            for (int i = 1; i <= columnCount; i++) {
                                rowData.add(resultSet.getString(i));
                            }
                            dataList.add(rowData);
                        }
                        titleList.add(0, "");
                        //将查询结果输出到框内
                        databaseFrame.setTable(titleList.parallelStream().toArray(String[]::new),
                                dataList.parallelStream().map(p -> p.parallelStream().toArray(String[]::new)).toArray(String[][]::new), project);
                    } else {
                        Messages.showWarningDialog("请检查配置url,username,password,driverClassName", "数据库配置");
                    }
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    Messages.showErrorDialog(e.getMessage(), "查询异常");
                } finally {
                    if (null != inputStream) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != conn) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != stmt) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "ExecutSqlServiceImpl");
        }
    }
}

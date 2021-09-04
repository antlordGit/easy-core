package com.easy.core.service.impl;

import com.easy.core.entity.EasyJson;
import com.easy.core.entity.TreeNavigationItem;
import com.easy.core.entity.TreeNode;
import com.easy.core.service.RestServiceService;
import com.easy.core.storage.EasyState;
import com.easy.core.storage.EasyStorage;
import com.easy.core.ui.frame.RestUrlJframe;
import com.easy.core.util.PsiClassToJsonUtil;
import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 将Controller层的接口显示在编辑区中
 */
public class RestServiceServiceImpl implements RestServiceService {

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            //项目模块管理器
            ModuleManager moduleManager = ModuleManager.getInstance(event.getProject());
            if (null != moduleManager) {
                TreeNode rootNode = new TreeNode(null, null, event.getProject().getName(), "1", event.getProject());
                List<TreeNode> rootNodeChildList = rootNode.getChildList();
                //模块管理器取出各子模块
                Module[] modules = moduleManager.getModules();
                TreeNavigationItem treeNavigationItem;
                if (modules.length > 0) {
                    try {

                        // 保存mapping
                        EasyState.navigationItemList.clear();

                        // 保存项目端口号
                        EasyStorage.setPort("");
                        PsiFile[] filesByName = FilenameIndex.getFilesByName(event.getProject(), "local.properties", GlobalSearchScope.projectScope(event.getProject()));
                        if (filesByName.length > 0) {
                            InputStream inputStream = filesByName[0].getVirtualFile().getInputStream();
                            Properties properties = new Properties();
                            properties.load(inputStream);
                            if (StringUtils.isNotBlank(properties.getProperty("papp.jetty.port"))) {
                                EasyStorage.setPort(properties.getProperty("papp.jetty.port"));
                            }
                        }
                        if (StringUtils.isBlank(EasyStorage.getPort())) {
                            EasyStorage.setPort("8080");
                        }

                        int i = 1;
                        //遍历项目子模块取出Controller层
                        for (Module module : modules) {
                            TreeNode moduleNode = new TreeNode(rootNode, null, module.getName(), rootNode.getNodeIndex() + "-" + (i++), event.getProject());

                            List<TreeNode> moduleNodeChildList = moduleNode.getChildList();
                            Collection<PsiAnnotation> controller = JavaAnnotationIndex.getInstance().get("Controller", event.getProject(), GlobalSearchScope.moduleScope(module));
                            if (CollectionUtils.isNotEmpty(controller)) {
                                rootNodeChildList.add(moduleNode);
                                int j = 1;
                                for (PsiAnnotation psiAnnotation : controller) {
                                    PsiClass psiClass = (PsiClass) psiAnnotation.getParent().getParent();
                                    if (null != psiClass) {
                                        PsiAnnotation[] annotations = psiClass.getAnnotations();
                                        //取出Controller类的RequestMapping值
                                        List<PsiAnnotation> collect = Stream.of(annotations).filter(p -> p.getText().contains("RequestMapping")).collect(Collectors.toList());
                                        String parentMapping = "";
                                        if (CollectionUtils.isNotEmpty(collect)) {
                                            parentMapping = (String) ((JvmAnnotationConstantValue) collect.get(0).getAttributes().get(0).getAttributeValue()).getConstantValue();
                                        }
                                        PsiMethod[] methods = psiClass.getMethods();
                                        if (methods.length > 0) {
                                            for (PsiMethod method : methods) {
                                                PsiAnnotation[] methodAnnotations = method.getAnnotations();
                                                if (methodAnnotations.length > 0) {
                                                    //取出Mapping的方法名
                                                    List<PsiAnnotation> requestMapping = Lists.newArrayList();
                                                    for (PsiAnnotation methodAnnotation : methodAnnotations) {
                                                        if (methodAnnotation.getText().contains("Mapping")) {
                                                            requestMapping.add(methodAnnotation);
                                                        }
                                                    }
                                                    String value;
                                                    if (CollectionUtils.isNotEmpty(requestMapping)) {
                                                        String mapping = "";
                                                        if (CollectionUtils.isNotEmpty(requestMapping) && CollectionUtils.isNotEmpty(requestMapping.get(0).getAttributes())) {
                                                            mapping = (String) ((JvmAnnotationConstantValue) requestMapping.get(0).getAttributes().get(0).getAttributeValue()).getConstantValue();
                                                        }
                                                        if (!mapping.startsWith("/")) {
                                                            mapping = "/" + mapping;
                                                        }
                                                        //拼接请求路径
                                                        value = (parentMapping + mapping).replace("\"", "").replaceAll("[//]+", "/");
                                                        String restUrl = EasyStorage.getRestUrl();
                                                        EasyJson json = new EasyJson(StringUtils.isNotBlank(restUrl) ? restUrl : "{\"name\":\"tom\"}");
                                                        boolean isNull = json.isNull(method.getName());
                                                        if (isNull) {
                                                            json.put(method.getName(), value);
                                                            EasyStorage.setRestUrl(json.toString());
                                                        }
                                                        PsiParameterList parameterList = method.getParameterList();
                                                        EasyJson easyJson = new EasyJson();
                                                        if (!parameterList.isEmpty()) {
                                                            PsiParameter[] parameters = parameterList.getParameters();
                                                            for (PsiParameter parameter : parameters) {
                                                                String className = parameter.getType().getCanonicalText();
                                                                PsiClassToJsonUtil.setClassName(className);
                                                                PsiClassToJsonUtil.classToJson(className, parameter.getName(), easyJson, event.getProject(), true);
                                                            }
                                                        }
                                                        //将Controller层的请求路径放到treeNavigationItem
                                                        treeNavigationItem = new TreeNavigationItem(method, value);
                                                        EasyState.navigationItemList.add(treeNavigationItem);
                                                        treeNavigationItem.setEasyJson(easyJson);
                                                        TreeNode treeNode = new TreeNode(moduleNode, treeNavigationItem, value, moduleNode.getNodeIndex() + "-" + (j++), event.getProject());
                                                        moduleNodeChildList.add(treeNode);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(moduleNodeChildList)) {
                                List<TreeNode> collect = moduleNodeChildList.parallelStream().sorted(Comparator.comparing(TreeNode::getName)).collect(Collectors.toList());
                                moduleNodeChildList.clear();
                                moduleNodeChildList.addAll(collect);
                            }
                        }
                        //将树型的请求路径显示在Window上
                        ToolWindow toolWindow = ToolWindowManager.getInstance(event.getProject()).getToolWindow("EasyTool");
                        RestUrlJframe component = (RestUrlJframe) toolWindow.getContentManager().findContent("RestService").getComponent();
                        component.initTree(rootNode, event.getProject());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Messages.showErrorDialog("映射URL失败！", "映射URL");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("RestServiceServiceImpl.apply");
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "RestServiceServiceImpl");
        }
    }

}

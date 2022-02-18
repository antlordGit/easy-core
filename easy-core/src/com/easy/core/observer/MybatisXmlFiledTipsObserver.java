package com.easy.core.observer;

import com.intellij.openapi.vfs.VirtualFile;

public class MybatisXmlFiledTipsObserver implements EasyObserver{


    @Override
    public void observer(String action, VirtualFile virtualFile) {
        switch (action) {
            case "open":
                open(virtualFile);
                break;
            default:
                break;
        }

    }

    private boolean open(VirtualFile virtualFile) {
//        if (!"XML".equals(virtualFile.getFileType().getName())) {
//            return false;
//        }
//
//        VirtualFile[] openFiles = DocumentListener.openFiles;
//        List<VirtualFile> xmlList = Stream.of(openFiles).filter(p -> p.getFileType().getName().equals("XML")).collect(Collectors.toList());
//        if (CollectionUtils.isEmpty(xmlList)) {
//            return false;
//        }
//        JSONObject classJson = new JSONObject();
//        for (VirtualFile file : xmlList) {
//            SAXReader reader = new SAXReader();
//            try {
//                Document document = reader.read(new File(file.getPath()));
//                Element bookstore = document.getRootElement();
//
//                String namespace = bookstore.attribute("namespace").getText();
//                PsiClass aClass = JavaFileManager.getInstance(DocumentListener.project).findClass(namespace, GlobalSearchScope.projectScope(DocumentListener.project));
//                if (null != aClass) {
//                    PsiMethod[] allMethods = aClass.getMethods();
//                    Map<String, List<String>> collect1 = Stream.of(allMethods).collect(Collectors.toMap(
//                            PsiMethod::getName,
//                            p -> {
//                        JvmParameter[] parameters = p.getParameters();
//                        if (0 == parameters.length) {
//                            return Lists.newArrayList();
//                        }
//                        List<String> collect = Stream.of(parameters).map(t -> {
//                            JvmAnnotation annotation = t.getAnnotation("org.apache.ibatis.annotations.Param");
//                            if (null == annotation) {
//                                return t.getName();
//                            } else {
//                                return (String)((JvmAnnotationConstantValue) allMethods[1].getParameters()[0].getAnnotation("org.apache.ibatis.annotations.Param").getAttributes().get(0).getAttributeValue()).getConstantValue();
//                            }
//                        }).collect(Collectors.toList());
//                        return collect;
//                    }));
//                    classJson.put(namespace, collect1);
//                    EasyStorage.setMybatisXmlFiled(classJson.toJSONString());
//                }
//            } catch (Exception e) {
////                e.printStackTrace();
//                System.out.println("===============>MybatisXmlFiledTipsObserver.open");
//            }
//        }
        return true;
    }
}

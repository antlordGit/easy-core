package com.easy.core.observer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.easy.core.storage.EasyStorage;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

public class MybatisXmlFiledTipsObserver implements EasyObserver{


    @Override
    public void observer(String action, VirtualFile virtualFile) {
        if (!"open".equals(action)) {
            return;
        }
        if (!"XML".equals(virtualFile.getFileType().getName())) {
            return;
        }

        String mybatisXmlFiled = EasyStorage.getMybatisXmlFiled();
        JSONArray jsonArray = JSON.parseArray(mybatisXmlFiled);
        if (CollectionUtils.isNotEmpty(jsonArray)) {
            if (5 == jsonArray.size()) {
                jsonArray.remove(0);
            }
        }

        SAXReader reader = new SAXReader();
        try {

            Document document = reader.read(new File(virtualFile.getPath()));
            Element bookstore = document.getRootElement();
            Iterator storeit = bookstore.elementIterator();

            while(storeit.hasNext()){
                Object next = storeit.next();

            }
        } catch (DocumentException e) {

            e.printStackTrace();
        }

    }
}

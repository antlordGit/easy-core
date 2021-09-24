package com.easy.core.service.impl;

import com.easy.core.service.RegisterRenewalMenuService;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.ui.Messages;

import java.io.*;

public class RegisterRenewalMenuServiceImpl implements RegisterRenewalMenuService {
    @Override
    public void actionPerformed(AnActionEvent event) {
        String configPath = PathManager.getConfigPath();
        File evalDir = new File(configPath, "eval");
        int index;
        if (evalDir.exists()) {
            File[] files = evalDir.listFiles();
            if (files == null || files.length == 0) {
                return;
            } else {
                int length = files.length;
                for (index = 0; index < length; ++index) {
                    File file = files[index];
                    if (file.getName().endsWith(".key")) {
                        DataInputStream dis = null;
                        DataOutputStream dos = null;
                        try {
//                            dis = new DataInputStream(new FileInputStream(file));
//                            long start = ~dis.readLong();
//                            Date expireDate = new Date(start + 2592000000L);
//                            System.out.println("==========" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expireDate));
                            dos = new DataOutputStream(new FileOutputStream(file));
                            dos.writeLong(~System.currentTimeMillis());
//                            dis.close();
                            dos.close();
                            Messages.showInfoMessage("免费试用已续期30天", "免费试用");
                        } catch (Exception e) {
//                            e.printStackTrace();
                            System.out.println("===========RegisterRenewalMenuAction.actionPerformed");
                        } finally {
                            if (null != dis) {
                                try {
                                    dis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (null != dos) {
                                try {
                                    dos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            ApplicationManager.getApplication().restart();
                        }

                    }
                }
            }
        }
    }
}

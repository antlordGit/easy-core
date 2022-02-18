package com.easy.core.action.menu;

import com.easy.core.service.RegisterRenewalMenuService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class RegisterRenewalMenuAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        RegisterRenewalMenuService service = ServiceManager.getService(event.getProject(), RegisterRenewalMenuService.class);
        service.actionPerformed(event);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\陈志伟\\AppData\\Roaming\\JetBrains\\IntelliJIdea2021.3\\eval\\idea212.evaluation.key");
        System.out.println("===" + file.getAbsolutePath() + "\\" + file.getName());
        DataInputStream dis = null;
        DataOutputStream dos = null;
        // C:\Users\陈志伟\AppData\Local\JetBrains\IntelliJIdea2020.2\plugins-sandbox\config\eval\idea202.evaluation.key\idea202.evaluation.key
        dos = new DataOutputStream(new FileOutputStream(file));
        dos.writeLong(~System.currentTimeMillis());
//                            dis.close();
        dos.close();
    }
}

package com.easy.core.action.menu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.PathManager;
import org.jetbrains.annotations.NotNull;
//import io.zhile.research.intellij.ier.helper.DateTime;
//import io.zhile.research.intellij.ier.helper.NotificationHelper;

public class AddClassCommentMenuAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
//        AddClassCommentMenuService service = ServiceManager.getService(event.getProject(), AddClassCommentMenuService.class);
//        service.actionPerformed(event);
        String configPath = PathManager.getConfigPath();
//        return new File(configPath, "eval");

//        File evalDir = new File(configPath, "eval");
//        File[] files;
//        int var5;
//        if (evalDir.exists()) {
//             files = evalDir.listFiles();
//            if (files == null) {
//                NotificationHelper.showError((Project)null, "List eval license file failed!");
//            } else {
//                files = files;
//                int var4 = files.length;
//
//                for(var5 = 0; var5 < var4; ++var5) {
//                    File file = files[var5];
//                    if (file.getName().endsWith(".key")) {
////                        list.add(new LicenseFileRecord(file));
//                        try {
//                            DataInputStream dis = new DataInputStream(new FileInputStream(file));
//                            Date expireDate = new Date(dis.readLong() + 2592000000L);
//                            String format = DateTime.DF_DATETIME.format(expireDate);
//                            System.out.println("==========0" + format);
//                            dis.close();
//                            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
//                            dos.writeLong(System.currentTimeMillis() + 2592000000L);
//                            dos.flush();
//                            dos.close();
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }


//        ApplicationManager.getApplication().restart();


    }
}
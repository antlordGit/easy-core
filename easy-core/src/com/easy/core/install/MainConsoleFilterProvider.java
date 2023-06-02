package com.easy.core.install;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainConsoleFilterProvider implements ConsoleFilterProvider {

    @NotNull
    @Override
    public Filter[] getDefaultFilters(@NotNull Project project) {
        return new Filter[]{new MainConsoleFilter()};
    }

    public class MainConsoleFilter implements Filter {

        @Nullable
        @Override
        public Result applyFilter(@NotNull String log, int offset) {
            Result result = null;
            try {
                List<ResultItem> list = new ArrayList<>();
                // sql日志为绿色
                if (log.contains("Preparing: ") || log.contains("Parameters:")) {
                    TextAttributes textAttributes = new TextAttributes();
                    textAttributes.setForegroundColor(new JBColor(new Color(37, 245, 19), new Color(37, 245, 19)));
                    int start = log.indexOf("Preparing:");
                    if (start == -1) {
                        start = log.indexOf("Parameters:");
                    }
                    int length = log.length() - start;
                    ResultItem item = new ResultItem(offset - length, offset, null, textAttributes);
                    list.add(item);
                    result = new Result(list);
                } else if (log.contains("controller") || log.contains("ServiceImpl")) {
                    // 普通日志为紫色
//                     TextAttributes textAttributes = new TextAttributes();
//                     textAttributes.setForegroundColor(new JBColor(new Color(255, 150, 255), new Color(255, 150, 255)));
//
//                     // 点击日志跳转
// //                     HyperlinkInfo info = new HyperlinkInfo() {
// //                         @Override
// //                         public void navigate(Project project) {
// //                             if (log.contains(":] --") && log.contains("[com")) {
// //                                 String classPath = log.substring(log.indexOf("[com") + 1, log.indexOf(":] --"));
// //                                 PsiClass psiClass = JavaFileManager.getInstance(project).findClass(classPath, GlobalSearchScope.projectScope(project));
// // //                        Stream.of(psiClass.getChildren()).filter(p->p.getText().contains())
// //                             }
// //                             navigate(project);
// //                         }
// //                     };
//
// //            ResultItem item = new ResultItem(offset - log.length(), offset, info, textAttributes);
// //                     ResultItem item = new ResultItem(offset - log.length(), offset, null, textAttributes);
// //                     list.add(item);
// //                     result = new Result(list);
//
//                     int start = log.indexOf("controller");
//                     if (start == -1) {
//                         start = log.indexOf("ServiceImpl") + 11;
//                     } else {
//                         start = start + 10;
//                     }
//                     int length = log.length() - start;
//                     ResultItem item = new ResultItem(offset - length, offset, null, textAttributes);
//                     list.add(item);
//                     result = new Result(list);
                }
            } catch (Exception e) {
                System.out.println("MainConsoleFilter.applyFilter");
                e.printStackTrace();
                Messages.showErrorDialog(e.getMessage(), "MainConsoleFilter");
            }

            return result;
        }
    }
}

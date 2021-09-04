package com.easy.core.install;

import com.easy.core.action.function.ExecuteXmlSqlAction;
import com.easy.core.icons.EasyIcon;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MybatisXmlContributor extends RunLineMarkerContributor {

    private final List<String> sqlMethod = new ArrayList<>(Arrays.asList("select", "update", "insert", "delete"));

    @Nullable
    @Override
    public Info getInfo(@NotNull PsiElement element) {
        try {
            PsiFile containingFile = element.getContainingFile();
            if (containingFile instanceof XmlFile && element instanceof XmlTag) {
                boolean anyMatch = sqlMethod.parallelStream().anyMatch(p -> ((XmlTagImpl) element).getName().equals(p));
                if (anyMatch) {
                    AnAction[] array = new AnAction[1];
                    array[0] = new AnAction("execute") {
                        @Override
                        public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                            ExecuteXmlSqlAction executeXmlSqlAction = (ExecuteXmlSqlAction) ActionManager.getInstance().getAction("ExecuteXmlSqlActionId");
                            executeXmlSqlAction.setPsiElement(element);
                            executeXmlSqlAction.actionPerformed(anActionEvent);
                        }
                    };

//                    array[1] = new AnAction("SQM") {
//                        @Override
//                        public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//                            SqmScanAction sqmScanAction = (SqmScanAction) ActionManager.getInstance().getAction("SqmScanActionId");
//                            sqmScanAction.setPsiElement(element);
//                            sqmScanAction.actionPerformed(anActionEvent);
//                        }
//                    };

                    return new Info(EasyIcon.RUN, array, psiElement1 -> "request");
                }
            }
        } catch (Exception e) {
            System.out.println("MybatisXmlContributor.getInfo");
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "MybatisXmlContributor");
        }
        return null;
    }

}

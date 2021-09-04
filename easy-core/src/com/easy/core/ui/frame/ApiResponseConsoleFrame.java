package com.easy.core.ui.frame;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;

public class ApiResponseConsoleFrame extends JBPanel {
    private JPanel rootPanel;
    private ConsoleView console;
    public ApiResponseConsoleFrame() {
        super(new BorderLayout());
        rootPanel = new JBPanel(new BorderLayout());
        add(rootPanel);
    }

    public void consolePrint(String value, Project project) {
        try {
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("EasyTool");
            toolWindow.activate(() -> {
                Content apiConsole = toolWindow.getContentManager().findContent("ApiConsole");
                toolWindow.getContentManager().setSelectedContent(apiConsole);
                console.clear();
                console.print(StringUtils.isBlank(value) ? "" : value, ConsoleViewContentType.LOG_INFO_OUTPUT);
            });
        } catch (Exception e) {
            System.out.println("ApiResponseConsoleFrame.consolePrint");
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "ApiResponseConsoleFrame");
        }
    }

    public void setConsole(ConsoleView console) {
        this.console = console;
    }
}

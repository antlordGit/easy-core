package com.easy.core.ui;

import com.easy.core.icons.EasyIcon;
import com.easy.core.ui.frame.ApiResponseConsoleFrame;
import com.easy.core.ui.frame.DatabaseFrame;
import com.easy.core.ui.frame.RestUrlJframe;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class EasyFrameFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ApiResponseConsoleFrame apiResponseConsoleFrame = getConsoleFrame(project);

        RestUrlJframe restUrlJframe = new RestUrlJframe();
        DatabaseFrame databaseFrame = new DatabaseFrame();
        Content restServiceContent = toolWindow.getContentManager().getFactory().createContent(restUrlJframe, "RestService", false);
        Content apiConsoleContent = toolWindow.getContentManager().getFactory().createContent(apiResponseConsoleFrame, "ApiConsole", false);
        Content databaseContent = toolWindow.getContentManager().getFactory().createContent(databaseFrame, "Database", false);
        toolWindow.getContentManager().addContent(restServiceContent);
        toolWindow.getContentManager().addContent(apiConsoleContent);
        toolWindow.getContentManager().addContent(databaseContent);
        toolWindow.setTitle("RestService");
        toolWindow.setType(ToolWindowType.DOCKED, null);
        toolWindow.setIcon(EasyIcon.TOOL_WINDOW);
    }

    public static JComponent getComponent(String frameId, Project project) {
        try {
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("EasyTool");
            Content content = toolWindow.getContentManager().findContent(frameId);
            toolWindow.activate(() -> toolWindow.getContentManager().setSelectedContent(content));
            return content.getComponent();
        } catch (Exception e) {
            System.out.println("PinganFrameFactory.getComponent");
            e.printStackTrace();
            return null;
        }
    }

    private ApiResponseConsoleFrame getConsoleFrame(Project project) {
        ConsoleView console = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        ApiResponseConsoleFrame apiResponseConsoleFrame = new ApiResponseConsoleFrame();
        try {
            apiResponseConsoleFrame.setLayout(new BorderLayout());
            apiResponseConsoleFrame.add(console.getComponent(), "Center");
            apiResponseConsoleFrame.setConsole(console);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultActionGroup actionGroup = (DefaultActionGroup) ActionManager.getInstance().getAction("consoleToolActionId");
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("consoleToolBarId", actionGroup, false);
        apiResponseConsoleFrame.add(actionToolbar.getComponent(), BorderLayout.WEST);
        return apiResponseConsoleFrame;
    }
}

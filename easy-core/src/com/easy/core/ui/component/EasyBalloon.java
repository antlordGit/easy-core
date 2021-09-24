package com.easy.core.ui.component;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.BalloonImpl;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.UIUtil;
import kotlin.Pair;
import kotlin.collections.ArraysKt;

import javax.swing.*;
import java.awt.*;

public class EasyBalloon {

    public EasyBalloon(String message, Project project, JBColor color) {
        JPanel content = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel();
        jLabel.setText(message);
        jLabel.setForeground(color);
        jLabel.setFont(new Font("微软雅黑", Font.BOLD, 30));
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        UIUtil.getListSelectionBackground(true);
        jLabel.setBounds(0, 0, 300, 150);
        content.add(jLabel);
        BalloonBuilder builder = JBPopupFactory.getInstance().createBalloonBuilder(content);
        builder.setShadow(false);
        Color panelBackground = UIUtil.getPanelBackground();
        builder.setFillColor(panelBackground);
        builder = builder.setFadeoutTime(1000);
        BalloonImpl balloon = (BalloonImpl) builder.createBalloon();
        balloon.setBounds(new Rectangle(new Point((width - 300) / 2, (height - 150) / 2), new Dimension(300, 150)));

        IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(project);
        if (ideFrame == null) {
            WindowManager windowManager = WindowManager.getInstance();
            IdeFrame[] ideFrameArray = windowManager.getAllProjectFrames();
            ideFrame = ArraysKt.first(ideFrameArray);
        }
        JComponent component = ideFrame.getComponent();
        Rectangle frameBounds = component.getBounds();
        RelativePoint notificationPosition = new RelativePoint(ideFrame.getComponent(), new Point(frameBounds.x, 20));
        balloon.show(notificationPosition, Balloon.Position.atLeft);
    }
}

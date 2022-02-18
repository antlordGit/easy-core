package com.easy.core.ui.frame;

import com.intellij.openapi.editor.event.VisibleAreaEvent;
import com.intellij.openapi.editor.event.VisibleAreaListener;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GlancePanel extends JPanel implements VisibleAreaListener {
    private MainPanel mainPanel;
    public GlancePanel() {
        super(new BorderLayout());
        mainPanel = new MainPanel();
        setPreferredSize(new Dimension(50, 100));
//        add(mainPanel);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(new ImageIcon("E:\\1.png"));
        add(jLabel);
    }

    @Override
    public void visibleAreaChanged(@NotNull VisibleAreaEvent visibleAreaEvent) {
        System.out.println("============");
    }


    private class MainPanel extends JPanel{
        public MainPanel() {
            super(new BorderLayout());
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            try {
                BufferedImage read = ImageIO.read(new File("E:\\1.png"));
                g.drawImage(read, 0, 0, read.getWidth(), read.getHeight(), null);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}

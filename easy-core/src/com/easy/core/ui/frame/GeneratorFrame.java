package com.easy.core.ui.frame;

import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class GeneratorFrame extends JBPanel {

    private JBPanel root;
    private JBPanel parentPanel;

    public GeneratorFrame() {
        super(new BorderLayout());
        root = new JBPanel(new BorderLayout());
        add(root);
        parentPanel = new JBPanel(new GridLayout(5, 2));
        JBScrollPane scrollPane = new JBScrollPane(parentPanel);
        root.add(scrollPane);

        Panel dbpanel = new Panel(new GridLayout(1, 4));
        dbpanel.add(new Label("数据库连接"));
        JTextField jTextField = new JTextField("11111");
        dbpanel.add(jTextField);
        dbpanel.add(new Label("用户名"));
        dbpanel.add(new JTextField("22222"));
        parentPanel.add(dbpanel);
    }
}

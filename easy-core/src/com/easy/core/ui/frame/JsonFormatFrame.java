package com.easy.core.ui.frame;

import com.easy.core.util.JsonFormatterUtil;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class JsonFormatFrame extends JFrame {

    private JEditorPane editorPane;

    public JsonFormatFrame() {
        setTitle("JSON Formatter");
        Image jsonLogo = ((IconLoader.CachedImageIcon) IconLoader.getIcon("/icon/image/json.png")).getRealIcon().getImage();
        setIconImage(jsonLogo);
        setLocationRelativeTo(null);

        editorPane = new JEditorPane();
        editorPane.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        editorPane.setBackground(Gray._80);
        editorPane.setForeground(Gray._240);
        editorPane.setCaretColor(Gray._240);

        JBScrollPane scrollPane = new JBScrollPane(editorPane);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomJpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomJpanel.setBackground(new JBColor(new Color(60, 63, 65), new Color(60, 63, 65)));
        add(bottomJpanel, BorderLayout.SOUTH);

        JButton formatter = new JButton("Formatter");
        formatter.setBackground(new JBColor(new Color(60, 63, 65), new Color(60, 63, 65)));
        formatter.setForeground(new JBColor(Gray._200, Gray._200));
        formatter.addActionListener(e -> onFormatter());
        bottomJpanel.add(formatter);

        JButton cancel = new JButton("Cancel");
        cancel.setBackground(new JBColor(new Color(60, 63, 65), new Color(60, 63, 65)));
        cancel.setForeground(new JBColor(Gray._200, Gray._200));
        cancel.addActionListener(e -> onCancel());
        bottomJpanel.add(cancel);


        JPanel leftJpanel = new JPanel();
        leftJpanel.setPreferredSize(new Dimension(10, 0));
        add(leftJpanel, BorderLayout.WEST);

        JPanel rightJpanel = new JPanel();
        rightJpanel.setPreferredSize(new Dimension(10, 0));
        add(rightJpanel, BorderLayout.EAST);

        JPanel topJpanel = new JPanel();
        topJpanel.setPreferredSize(new Dimension(0, 10));
        add(topJpanel, BorderLayout.NORTH);
    }

    private void onFormatter() {
        String text = editorPane.getText();
        String json = JsonFormatterUtil.jsonFormatter(text);
        editorPane.setText(json);
    }

    private void onCancel() {
        dispose();
    }
}

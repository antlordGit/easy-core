package com.easy.core.action.menu;

import com.easy.core.entity.GlanceInfo;
import com.google.common.collect.Maps;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class GlanceCodeMenuAction extends AnAction {
    private static Cursor cursor;
    public final static Map<String, GlanceInfo> GLANCE_LABEL_MAP = Maps.newHashMap();

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        FileEditor[] allEditors = FileEditorManager.getInstance(anActionEvent.getProject()).getAllEditors();
        if (null == allEditors || 0 == allEditors.length) {
            return;
        }
        Editor selectedTextEditor = FileEditorManager.getInstance(anActionEvent.getProject()).getSelectedTextEditor();

        for (FileEditor editor : allEditors) {
            JComponent component = editor.getComponent();
            JPanel parentPanel = (JPanel) component;
            JLayeredPane parentLayeredPane = (JLayeredPane) parentPanel.getComponent(0);
            JPanel componentPanel = (JPanel) parentLayeredPane.getComponent(0);
            if (componentPanel.getComponentCount() > 1) {
                continue;
            }
            JLabel jLabel = new JLabel();
            GlanceInfo glanceInfo = new GlanceInfo();
            glanceInfo.setjLabel(jLabel);
            glanceInfo.setGlanceY(0);
            GLANCE_LABEL_MAP.put(editor.getName(), glanceInfo);

            componentPanel.add(jLabel, BorderLayout.EAST);
            initGlanceLabel(jLabel, editor, anActionEvent.getProject(), selectedTextEditor.getColorsScheme());
            addMouseMotionListener(anActionEvent.getProject(), jLabel, editor, selectedTextEditor.getColorsScheme());
            addDocumentChanged((TextEditor) editor, jLabel, anActionEvent.getProject(), selectedTextEditor.getColorsScheme());
            addMouseListener(anActionEvent.getProject(), jLabel, editor, selectedTextEditor.getColorsScheme());
            addMouseWheelListener(anActionEvent.getProject(), jLabel, editor, selectedTextEditor.getColorsScheme());
        }
    }

    private void addDocumentChanged(TextEditor editor, JLabel jLabel, Project project, EditorColorsScheme scheme) {
        Document document = editor.getEditor().getDocument();
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                System.out.println("==-----------------");
                GlanceInfo glanceInfo = GLANCE_LABEL_MAP.get(editor.getName());
                render((int) jLabel.getSize().getWidth(), (int) jLabel.getSize().getHeight(), glanceInfo.getGlanceY(), project, jLabel, editor, scheme, new Color(0, 0, 0));
                glanceInfo = GLANCE_LABEL_MAP.get(editor.getName());
                Integer glanceHeight = glanceInfo.getGlanceHeight();
                if (glanceHeight - 60 < glanceInfo.getGlanceY()) {
                    glanceInfo.setGlanceY(glanceHeight - 60);
                }
                render((int) jLabel.getSize().getWidth(), (int) jLabel.getSize().getHeight(), glanceInfo.getGlanceY(), project, jLabel, editor, scheme, new Color(0, 0, 0));
            }
        });
    }

    public void render(int backWidth, int backHeight, int glanceY, Project project, JLabel jLabel, FileEditor editor, EditorColorsScheme scheme, Color background) {
        BufferedImage bufferedImage = new BufferedImage(backWidth, backHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(background);
        graphics.fillRect(0, 0, backWidth, backHeight);
        setGlanceContent(bufferedImage, editor, project, scheme);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
        graphics.setComposite(ac);
        graphics.setColor(new Color(150, 150, 150));
        graphics.fillRect(0, glanceY, backWidth, 60);
        FileEditorManager instance = FileEditorManager.getInstance(project);
        Editor selectedTextEditor = instance.getSelectedTextEditor();
        selectedTextEditor.getScrollingModel().scrollTo(new LogicalPosition(glanceY / 3, 0), ScrollType.CENTER_DOWN);
        jLabel.setIcon(new ImageIcon(bufferedImage));
        jLabel.updateUI();
    }

    private void initGlanceLabel(JLabel jLabel, FileEditor editor, Project project, EditorColorsScheme scheme) {
        render(100, 881, 0, project, jLabel, editor, scheme, new Color(0, 0, 0));
    }
    
    private void setGlanceContent(BufferedImage bufferedImage, FileEditor editor, Project project, EditorColorsScheme scheme) {
        TextEditor editor1 = (TextEditor) editor;
        Document document = editor1.getEditor().getDocument();
        VirtualFile file = editor.getFile();
        final SyntaxHighlighter hl = SyntaxHighlighterFactory.getSyntaxHighlighter(file.getFileType(), project, file);
        Lexer lexer = hl.getHighlightingLexer();
        String text = document.getText();
        lexer.start(text);

        int lastY = 0;
        int lastx = 0;
        int begin;
        for (IElementType tokenType = lexer.getTokenType(); tokenType != null; tokenType = lexer.getTokenType()) {
            int x = lexer.getTokenStart();
            int rgb = getColorForElementType(tokenType, hl, scheme);
            String tokenText = lexer.getTokenText();
            int charAtIndex = -1;
            begin = x - lastx;
            int tail = lastx;
            for (int i = x - tail; i < lexer.getTokenEnd() - tail; i++) {
                charAtIndex++;
                if (tokenText.charAt(charAtIndex) == '\n') {
                    lastx = lexer.getTokenStart() + (charAtIndex + 1);
                    begin = 0;
                    lastY++;
                    lastY++;
                    lastY++;
                    continue;
                }

                if (tokenText.charAt(charAtIndex) == '\t') {
                    begin++;
                    begin++;
                    begin++;
                    begin++;
                    lastx++;
                    lastx++;
                    lastx++;
                    lastx++;
                    continue;
                }

                if (tokenText.charAt(charAtIndex) == ' ') {
                    begin++;
                    continue;
                }
                setPixel(bufferedImage, begin, lastY, rgb);
                setPixel(bufferedImage, begin, lastY + 1, rgb);
                begin++;
            }
            lexer.advance();
        }
        GlanceInfo glanceInfo = GLANCE_LABEL_MAP.get(editor.getName());
        glanceInfo.setGlanceHeight(lastY);
    }


    private void addMouseMotionListener(Project project, JLabel jLabel, FileEditor editor, EditorColorsScheme scheme) {
        jLabel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (jLabel.getCursor().getType() == Cursor.W_RESIZE_CURSOR) {
                    int w = jLabel.getSize().width - e.getPoint().x;
                    w = Math.max(Math.min(w, 200), 60);
                    int h = jLabel.getSize().height;

                    jLabel.setPreferredSize(new Dimension(w, h));
                }
                moveGlance(e, project, jLabel, editor, scheme);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (8 >= e.getX()) {
                    cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
                    jLabel.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                } else {
                    cursor = Cursor.getDefaultCursor();
                    jLabel.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }

    private void moveGlance(MouseEvent e, Project project, JLabel jLabel, FileEditor editor, EditorColorsScheme scheme) {
        int y;
        GlanceInfo glanceInfo = GLANCE_LABEL_MAP.get(editor.getName());
        Integer glanceHeight = glanceInfo.getGlanceHeight();

        if (e.getYOnScreen() - 160 < 0) {
            y = Math.max(e.getYOnScreen() - 160, 0);
        } else {
            y = Math.min(e.getYOnScreen() - 160, glanceHeight - 60);
        }
        glanceInfo.setGlanceY(y);
        render((int) jLabel.getSize().getWidth(), (int) jLabel.getSize().getHeight(), y, project, jLabel, editor, scheme, new Color(0, 0, 0));

    }
    private void addMouseListener(Project project, JLabel jLabel, FileEditor editor, EditorColorsScheme scheme) {
        jLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                moveGlance(e, project, jLabel, editor, scheme);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }



    private void addMouseWheelListener(Project project, JLabel jLabel, FileEditor editor, EditorColorsScheme scheme) {
        jLabel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                GlanceInfo glanceInfo = GLANCE_LABEL_MAP.get(editor.getName());
//                Integer glanceHeight = glanceInfo.getGlanceHeight();
                Integer glanceHeight = glanceInfo.getGlanceY();
                int y = 0;
                // 向下
                if (e.getWheelRotation() == 1) {
//                    System.out.println("====1");
                    glanceInfo.setGlanceY(glanceHeight + 10);
                    y = Math.min(glanceInfo.getGlanceY(), 60);
                }

                // 向上
                if (e.getWheelRotation() == -1) {
                    glanceInfo.setGlanceY(glanceHeight - 10);
                    y = Math.min(glanceInfo.getGlanceY(), glanceHeight - 60);
                }
//                y = glanceInfo.getGlanceY();
                System.out.println("y====" + y);
                render((int) jLabel.getSize().getWidth(), (int) jLabel.getSize().getHeight(), y, project, jLabel, editor, scheme, new Color(0, 0, 0));

//                int y;
//                GlanceInfo glanceInfo = GLANCE_LABEL_MAP.get(editor.getName());
//                Integer glanceHeight = glanceInfo.getGlanceHeight();
//
//                if (e.getYOnScreen() - 160 < 0) {
//                    y = Math.max(e.getYOnScreen() - 160, 0);
//                } else {
//                    y = Math.min(e.getYOnScreen() - 160, glanceHeight - 60);
//                }
//                glanceInfo.setGlanceY(y);
//                render((int) jLabel.getSize().getWidth(), (int) jLabel.getSize().getHeight(), y, project, jLabel, editor, scheme, new Color(0, 0, 0));
//
//
//                moveGlance(e, project, jLabel, editor, scheme);
            }
        });
    }

    private final int getColorForElementType(IElementType element, SyntaxHighlighter hl, EditorColorsScheme colorScheme) {
        int color = colorScheme.getDefaultForeground().getRGB();
        TextAttributesKey[] attributes = hl.getTokenHighlights(element);

        for(int var8 = 0; var8 < attributes.length; ++var8) {
            TextAttributesKey attribute = attributes[var8];
            TextAttributes attr = colorScheme.getAttributes(attribute);
            if (attr != null) {
                Color tmp = attr.getForegroundColor();
                if (tmp != null) {
                    color = tmp.getRGB();
                }
            }
        }

        return color;
    }

    private final void setPixel(BufferedImage bufferedImage, int x, int y, int rgb) {
        int[] colorArray = new int[4];

        colorArray[3] = (int) (rgb * (float) 255);
        colorArray[0] = (rgb & 16711680) >> 16;
        colorArray[1] = (rgb & '\uff00') >> 8;
        colorArray[2] = rgb & 255;
        try {
            bufferedImage.getRaster().setPixel(x, y, colorArray);
        } catch (Exception e) {
            // System.out.println("x=" + x + ",y=" + y);
        }
    }
}

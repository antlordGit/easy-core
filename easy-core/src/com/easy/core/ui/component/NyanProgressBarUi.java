package com.easy.core.ui.component;

import com.easy.core.icons.EasyIcon;
import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.openapi.util.ScalableIcon;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * @Description: TODO
 * @Author: chenzhiwei
 * @Version: 1.0
 * @Create: 2021/8/20 22:12
 */
public class NyanProgressBarUi extends BasicProgressBarUI {


    private static final float ONE_OVER_SEVEN = 0.14285715F;
    private static final Color VIOLET = new Color(90, 0, 157);
    private volatile int offset = 0;
    private volatile int offset2 = 0;
    private volatile int velocity = 1;

    public NyanProgressBarUi() {
    }

    /**
     * @description: 初始化指定的自定义组件
     *
     * @author: chenzhiwei
     * @create: 2021/8/22 19:16
     * @param c [c]
     * @return javax.swing.plaf.ComponentUI
     */
    public static ComponentUI createUI(JComponent c) {
        c.setBorder(JBUI.Borders.empty().asUIResource());
        return new NyanProgressBarUi();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(super.getPreferredSize(c).width, JBUI.scale(20));
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        this.progressBar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
            }

            // 进度条隐藏时调用方法
            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
            }
        });
    }

    @Override
    protected void paintIndeterminate(Graphics g2d, JComponent c) {
        if (g2d instanceof Graphics2D) {
            Graphics2D g = (Graphics2D)g2d;
            Insets b = this.progressBar.getInsets();
//            //  System.out.println("paintIndeterminate=============" + b.left + "," + b.left + "," + b.top + "," + b.bottom);
            int barRectWidth = this.progressBar.getWidth() - (b.right + b.left);
            int barRectHeight = this.progressBar.getHeight() - (b.top + b.bottom);
            if (barRectWidth > 0 && barRectHeight > 0) {
                g.setColor(new JBColor(Gray._240.withAlpha(50), Gray._128.withAlpha(50)));
                int w = c.getWidth();
                int h = c.getPreferredSize().height;
                if (!isEven(c.getHeight() - h)) {
                    ++h;
                }
                int amountFull = this.getAmountFull(b, barRectWidth, barRectHeight);
                //
                // g.setColor(new Color(255, 0, 0));
                // g.fillRect(0, 0, amountFull, h);

                LinearGradientPaint baseRainbowPaint = new LinearGradientPaint(0.0F, (float)JBUI.scale(2), 0.0F, (float)(h - JBUI.scale(6)), new float[]{0.14285715F, 0.2857143F, 0.42857146F, 0.5714286F, 0.71428573F, 0.8571429F, 1.0F}, new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.cyan, Color.blue, VIOLET});
                g.setPaint(baseRainbowPaint);
                if (c.isOpaque()) {
                    g.fillRect(0, (c.getHeight() - h) / 2, w, h);
                }

                g.setColor(new JBColor(Gray._165.withAlpha(50), Gray._88.withAlpha(50)));
                GraphicsConfig config = GraphicsUtil.setupAAPainting(g);
                g.translate(0, (c.getHeight() - h) / 2);
                int x = -this.offset;
                Paint old = g.getPaint();
                g.setPaint(baseRainbowPaint);
                float R = JBUI.scale(8.0F);
                float R2 = JBUI.scale(9.0F);
                Area containingRoundRect = new Area(new RoundRectangle2D.Float(1.0F, 1.0F, (float)w - 2.0F, (float)h - 2.0F, R, R));
                g.fill(containingRoundRect);
                g.setPaint(old);
                this.offset = (this.offset + 1) % this.getPeriodLength();
                this.offset2 += this.velocity;
                if (this.offset2 <= 2) {
                    this.offset2 = 2;
                    this.velocity = 1;
                } else if (this.offset2 >= w - JBUI.scale(15)) {
                    this.offset2 = w - JBUI.scale(15);
                    this.velocity = -1;
                }

                Area area = new Area(new java.awt.geom.Rectangle2D.Float(0.0F, 0.0F, (float)w, (float)h));
                area.subtract(new Area(new RoundRectangle2D.Float(1.0F, 1.0F, (float)w - 2.0F, (float)h - 2.0F, R, R)));
                g.setPaint(Gray._128);
                if (c.isOpaque()) {
                    g.fill(area);
                }

                area.subtract(new Area(new RoundRectangle2D.Float(0.0F, 0.0F, (float)w, (float)h, R2, R2)));
                Container parent = c.getParent();
                Color background = parent != null ? parent.getBackground() : UIUtil.getPanelBackground();
                g.setPaint(background);
                if (c.isOpaque()) {
                    g.fill(area);
                }

                Icon scaledIcon = this.velocity > 0 ? (ScalableIcon) EasyIcon.CAT_ICON : (ScalableIcon)EasyIcon.RCAT_ICON;
                scaledIcon.paintIcon(this.progressBar, g, this.offset2 - JBUI.scale(10), -JBUI.scale(6));
                g.draw(new RoundRectangle2D.Float(1.0F, 1.0F, (float)w - 2.0F - 1.0F, (float)h - 2.0F - 1.0F, R, R));
                g.translate(0, -(c.getHeight() - h) / 2);

                // if (this.progressBar.isStringPainted()) {
                //     if (this.progressBar.getOrientation() == 0) {
                //         paintString1(g, b.left, b.top, barRectWidth, barRectHeight, this.boxRect.x, this.boxRect.width);
                //     } else {
                //         paintString1(g, b.left, b.top, barRectWidth, barRectHeight, this.boxRect.y, this.boxRect.height);
                //     }
                // }

                g.setColor(new Color(0, 0, 0));
                Point renderLocation = getStringPlacement(g, "1111111111111", b.left, b.top, barRectWidth, barRectHeight);
                SwingUtilities2.drawString(progressBar, g, "不要催", renderLocation.x, renderLocation.y);

                config.restore();
            }
        }
    }

    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {
        if (g instanceof Graphics2D) {
            if (this.progressBar.getOrientation() == 0 && c.getComponentOrientation().isLeftToRight()) {
                GraphicsConfig config = GraphicsUtil.setupAAPainting(g);
                Insets b = this.progressBar.getInsets();
//                //  System.out.println("paintDeterminate=============" + b.left + "," + b.left + "," + b.top + "," + b.bottom);
                int w = this.progressBar.getWidth();
                int h = this.progressBar.getPreferredSize().height;
                if (!isEven(c.getHeight() - h)) {
                    ++h;
                }

                int barRectWidth = w - (b.right + b.left);
                int barRectHeight = h - (b.top + b.bottom);
                if (barRectWidth > 0 && barRectHeight > 0) {
                    int amountFull = this.getAmountFull(b, barRectWidth, barRectHeight);
                    // g.setColor(new Color(255, 255, 0));
                    // g.fillRect(0, 0, amountFull, h);


                    //  System.out.println("amountFull=" + amountFull);
                    Container parent = c.getParent();
                    Color background = parent != null ? parent.getBackground() : UIUtil.getPanelBackground();
                    g.setColor(background);
                    Graphics2D g2 = (Graphics2D)g;
                    if (c.isOpaque()) {
                        g.fillRect(0, 0, w, h);
                    }

                    float R = JBUI.scale(8.0F);
                    float R2 = JBUI.scale(9.0F);
                    float off = JBUI.scale(1.0F);
                    g2.translate(0, (c.getHeight() - h) / 2);
                    g2.setColor(this.progressBar.getForeground());
                    g2.fill(new RoundRectangle2D.Float(0.0F, 0.0F, (float)w - off, (float)h - off, R2, R2));
                    g2.setColor(background);
                    g2.fill(new RoundRectangle2D.Float(off, off, (float)w - 2.0F * off - off, (float)h - 2.0F * off - off, R, R));
                    g2.setPaint(new LinearGradientPaint(0.0F, (float)JBUI.scale(2), 0.0F, (float)(h - JBUI.scale(6)), new float[]{0.14285715F, 0.2857143F, 0.42857146F, 0.5714286F, 0.71428573F, 0.8571429F, 1.0F}, new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.cyan, Color.blue, VIOLET}));
                    EasyIcon.CAT_ICON.paintIcon(this.progressBar, g2, amountFull - JBUI.scale(10), -JBUI.scale(6));
                    g2.fill(new RoundRectangle2D.Float(2.0F * off, 2.0F * off, (float)amountFull - JBUI.scale(5.0F), (float)h - JBUI.scale(5.0F), JBUI.scale(7.0F), JBUI.scale(7.0F)));
                    g2.translate(0, -(c.getHeight() - h) / 2);
//                    if (this.progressBar.isStringPai2nted()) {
//                        paintString1(g, b.left, b.top, barRectWidth, barRectHeight,0, amountFull);
//                    }
                    g2.setColor(new Color(0, 255, 0));
                    Point renderLocation = this.getStringPlacement(g2, "3333333333333", b.left, b.top, barRectWidth, barRectHeight);
                    SwingUtilities2.drawString(this.progressBar, g2, "不要催", renderLocation.x, renderLocation.y);

                    config.restore();
                }
            } else {
                super.paintDeterminate(g, c);
            }
        }
    }

    private void paintString1(Graphics g, int x, int y, int w, int h, int fillStart, int amountFull) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D)g;
            String progressString = this.progressBar.getString();
            g2.setFont(this.progressBar.getFont());
            Point renderLocation = this.getStringPlacement(g2, progressString, x, y, w, h);
            Rectangle oldClip = g2.getClipBounds();
            if (this.progressBar.getOrientation() == 0) {
                g2.setColor(this.getSelectionBackground());
                SwingUtilities2.drawString(this.progressBar, g2, progressString, renderLocation.x, renderLocation.y);
                g2.setColor(this.getSelectionForeground());
                g2.clipRect(fillStart, y, amountFull, h);
                SwingUtilities2.drawString(this.progressBar, g2, progressString, renderLocation.x, renderLocation.y);
            } else {
                g2.setColor(this.getSelectionBackground());
                AffineTransform rotate = AffineTransform.getRotateInstance(1.5707963267948966D);
                g2.setFont(this.progressBar.getFont().deriveFont(rotate));
                renderLocation = this.getStringPlacement(g2, progressString, x, y, w, h);
                SwingUtilities2.drawString(this.progressBar, g2, progressString, renderLocation.x, renderLocation.y);
                g2.setColor(this.getSelectionForeground());
                g2.clipRect(x, fillStart, w, amountFull);
                SwingUtilities2.drawString(this.progressBar, g2, progressString, renderLocation.x, renderLocation.y);
            }

            g2.setClip(oldClip);
        }
    }

    @Override
    protected int getBoxLength(int availableLength, int otherDimension) {
        return availableLength;
    }

    private int getPeriodLength() {
        return JBUI.scale(16);
    }

    private static boolean isEven(int value) {
        return value % 2 == 0;
    }

}

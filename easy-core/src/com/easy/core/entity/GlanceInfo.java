package com.easy.core.entity;

import javax.swing.*;

public class GlanceInfo {

    private JLabel jLabel;

    private Integer glanceHeight;

    private Integer backgroundHeight;

    private Integer glanceY;

    public JLabel getjLabel() {
        return jLabel;
    }

    public void setjLabel(JLabel jLabel) {
        this.jLabel = jLabel;
    }

    public Integer getGlanceHeight() {
        return glanceHeight;
    }

    public void setGlanceHeight(Integer glanceHeight) {
        this.glanceHeight = glanceHeight;
    }

    public Integer getBackgroundHeight() {
        return backgroundHeight;
    }

    public void setBackgroundHeight(Integer backgroundHeight) {
        this.backgroundHeight = backgroundHeight;
    }

    public Integer getGlanceY() {
        return glanceY;
    }

    public void setGlanceY(Integer glanceY) {
        this.glanceY = glanceY;
    }
}

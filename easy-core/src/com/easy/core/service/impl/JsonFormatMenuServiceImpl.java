package com.easy.core.service.impl;

import com.easy.core.service.JsonFormatMenuService;
import com.easy.core.ui.frame.JsonFormatFrame;

public class JsonFormatMenuServiceImpl implements JsonFormatMenuService {

    @Override
    public void actionPerformed() {
        JsonFormatFrame frame = new JsonFormatFrame();
        frame.pack();
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
}

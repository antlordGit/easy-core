package com.easy.core.storage;

import com.easy.core.entity.EasyConfiguration;
import com.easy.core.entity.TreeNavigationItem;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@State(name = "EasyStatore",storages = {@Storage("easy-config.xml")})
public class EasyState implements PersistentStateComponent<EasyState> {

    private EasyConfiguration easyConfiguration;
    public static List<TreeNavigationItem> navigationItemList = Lists.newArrayList();

    public static EasyState getInstance(){
        return ServiceManager.getService(EasyState.class);
    }

    @Nullable
    @Override
    public EasyState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull EasyState easyState) {
        XmlSerializerUtil.copyBean(easyState, this);
    }

    public EasyConfiguration getConfig() {
        if (null == easyConfiguration) {
            easyConfiguration = new EasyConfiguration();
        }
        return easyConfiguration;
    }

}

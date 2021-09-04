package com.easy.core.entity;

import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RestServiceFilteringGotoByModel extends FilteringGotoByModel {

    public RestServiceFilteringGotoByModel(@NotNull Project project, @NotNull ChooseByNameContributor[] contributors) {
        super(project, contributors);
    }

    /**
     * @description: 命中选项
     *
     * @author: chenzhiwei
     * @create: 2020/5/16 22:32
     * @return java.lang.Object
     */
    @Nullable
    @Override
    protected Object filterValueFor(NavigationItem navigationItem) {
        if (navigationItem instanceof TreeNavigationItem) {
            TreeNavigationItem myNavigationItem = (TreeNavigationItem) navigationItem;
            return myNavigationItem.getName();
        }
        return null;
    }

    /**
     * @description: 搜索框标题
     *
     * @author: chenzhiwei
     * @create: 2020/5/16 22:32
     * @return java.lang.String
     */
    @Nls(capitalization = Nls.Capitalization.Sentence)
    @Override
    public String getPromptText() {
        return "Enter";
    }

    @NotNull
    @Override
    public String getNotInMessage() {
        return "Not in Message";
    }

    @NotNull
    @Override
    public String getNotFoundMessage() {
        return "Not Found message";
    }

    /**
     * @description: 过滤器是否打开
     *
     * @author: chenzhiwei
     * @create: 2020/5/16 22:33
     * @return java.lang.String
     */
    @Nullable
    @Override
    public String getCheckBoxName() {
        return "New Check";
    }

    @Override
    public boolean loadInitialCheckBoxState() {
        return false;
    }

    @Override
    public void saveInitialCheckBoxState(boolean b) {
    }

    @NotNull
    @Override
    public String[] getSeparators() {
        return new String[]{"/","?"};
    }

    /**
     * @description: 必须重写，返回数据项
     *
     * @author: chenzhiwei
     * @create: 2020/5/16 22:33
     * @return java.lang.String
     */
    @Nullable
    @Override
    public String getFullName(@NotNull Object element) {
        return ((TreeNavigationItem)element).getName();
    }

    @Override
    public boolean willOpenEditor() {
        return true;
    }
}
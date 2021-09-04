package com.easy.core.entity;

import com.easy.core.storage.EasyState;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RestServiceChooseByNameContributor implements ChooseByNameContributor {

    private List<TreeNavigationItem> list = new ArrayList<>();

    public RestServiceChooseByNameContributor(Project project) {
        list.clear();
        list.addAll(EasyState.navigationItemList);
    }

    /**
     * @description: 提供全部选项
     *
     * @author: chenzhiwei
     * @create: 2020/5/16 22:31
     * @return java.lang.String[]
     */
    @NotNull
    @Override
    public String[] getNames(Project project, boolean b) {
        return list.parallelStream().map(TreeNavigationItem::getName).toArray(String[]::new);
    }

    /**
     * @description: 匹配到符合的项
     *
     * @author: chenzhiwei
     * @create: 2020/5/16 22:31
     * @return com.intellij.navigation.NavigationItem[]
     */
    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean b) {
        NavigationItem[] navigationItems = list.parallelStream().filter(
                p -> p.getName().equals(name))
                .toArray(NavigationItem[]::new);
        return navigationItems;
    }
}
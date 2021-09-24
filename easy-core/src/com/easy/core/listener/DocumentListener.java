package com.easy.core.listener;

import com.easy.core.observer.EasyObserver;
import com.easy.core.observer.LogoShowObserver;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DocumentListener implements FileEditorManagerListener, ProjectComponent {

    private List<EasyObserver> observerList = Lists.newArrayList();
    public static Project project;

    public DocumentListener(@NotNull Project project) {
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);
        this.project = project;
//        observerList.add(new MybatisXmlFiledTipsObserver());
        observerList.add(new LogoShowObserver());
    }

    @Override
    public void fileOpenedSync(@NotNull FileEditorManager source, @NotNull VirtualFile file, @NotNull Pair<FileEditor[], FileEditorProvider[]> editors) {
        if (CollectionUtils.isNotEmpty(observerList)) {
            observerList.forEach(observer -> observer.observer("open", file));
        }
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (CollectionUtils.isNotEmpty(observerList)) {
            observerList.forEach(observer -> observer.observer("open", file));
        }
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        if (CollectionUtils.isNotEmpty(observerList)) {
            observerList.forEach(observer -> observer.observer("open", null));
        }
    }

//    @Override
//    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//        FileEditor[] editors = source.getAllEditors();
//    }

}

package com.easy.core.listener;

import com.easy.core.observer.EasyObserver;
import com.easy.core.observer.MybatisXmlFiledTipsObserver;
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

    public DocumentListener(@NotNull Project project) {
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);
        observerList.add(new MybatisXmlFiledTipsObserver());
    }

    @Override
    public void fileOpenedSync(@NotNull FileEditorManager source, @NotNull VirtualFile file, @NotNull Pair<FileEditor[], FileEditorProvider[]> editors) {
        if (CollectionUtils.isNotEmpty(observerList)) {
            observerList.forEach(observer -> observer.observer("open", file));
        }
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {

    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
//        if (CollectionUtils.isNotEmpty(observerList)) {
//            observerList.forEach(observer -> observer.observer("change", file));
//        }
    }

//    @Override
//    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//        FileEditor[] editors = source.getAllEditors();
//    }

}

package com.easy.core.fileType.api;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

public class ApiElementFactory {
    public static ApiProperty createProperty(Project project, String name) {
        final ApiFile file = createFile(project, name);
        return (ApiProperty) file.getFirstChild();
    }

    public static ApiFile createFile(Project project, String text) {
        String name = "dummy.api";
        return (ApiFile) PsiFileFactory.getInstance( project).createFileFromText(name, ApiFileType.INSTANCE, text);
    }

    public static ApiProperty createProperty(Project project, String name, String value) {
        final ApiFile file = createFile(project, name + " = " + value);
        return (ApiProperty) file.getFirstChild();
    }

    public static PsiElement createCRLF(Project project) {
        final ApiFile file = createFile(project, "\n");
        return file.getFirstChild();
    }

}

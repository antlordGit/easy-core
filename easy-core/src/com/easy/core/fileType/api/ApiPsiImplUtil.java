package com.easy.core.fileType.api;


import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ApiPsiImplUtil {
    public static String getKey(ApiProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(ApiLexer.KEY);
        if (keyNode != null) {
            // IMPORTANT: Convert embedded escaped spaces to simple spaces
            return keyNode.getText().replaceAll("\\\\ ", " ");
        } else {
            return null;
        }
    }

    public static String getValue(ApiProperty element) {
        ASTNode valueNode = element.getNode().findChildByType(ApiLexer.VALUE);
        if (valueNode != null) {
            return valueNode.getText();
        } else {
            return null;
        }
    }

    public static String getName(ApiProperty element) {
        return getKey(element);
    }

    public static PsiElement setName(ApiProperty element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(ApiLexer.KEY);
        if (keyNode != null) {
            ApiProperty property = ApiElementFactory.createProperty(element.getProject(), newName);
            ASTNode newKeyNode = property.getFirstChild().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement getNameIdentifier(ApiProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(ApiLexer.KEY);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static ItemPresentation getPresentation(final ApiProperty element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getKey();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiFile containingFile = element.getContainingFile();
                return containingFile == null ? null : containingFile.getName();
            }

            @Override
            public Icon getIcon(boolean unused) {
                return AllIcons.Actions.Diff;
            }
        };
    }

}
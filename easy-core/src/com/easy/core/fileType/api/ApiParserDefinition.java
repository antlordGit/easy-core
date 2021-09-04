package com.easy.core.fileType.api;


import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class ApiParserDefinition  implements ParserDefinition {

    public static TokenSet COMMENT = TokenSet.create(ApiLexer.COMMENT);
    public static TokenSet WHITE_SPACE = TokenSet.create(TokenType.WHITE_SPACE);

    public static IFileElementType FileElementType = new IFileElementType(ApiLanguage.INSTANCE);
    public static ApiParser APIPARSER = new ApiParser();
    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new ApiFlexAdapter();
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACE;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENT;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return APIPARSER;
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FileElementType;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ApiFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();
        if (type.equals(ApiLexer.PROPERTY)) {
            return new ApiPropertyImpl(node);
        }
        throw new AssertionError("Unknown element type: " + type);
    }
}


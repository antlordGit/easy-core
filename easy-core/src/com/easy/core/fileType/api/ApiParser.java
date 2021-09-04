package com.easy.core.fileType.api;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;

@SuppressWarnings({"SimplifiableIfStatement"})
public class ApiParser implements PsiParser, LightPsiParser {

    @Override
    @NotNull
    public ASTNode parse(IElementType t, PsiBuilder b) {
        parseLight(t, b);
        return b.getTreeBuilt();
    }

    @Override
    public void parseLight(IElementType t, PsiBuilder b) {
        boolean r;
        b = adapt_builder_(t, b, this, null);
        Marker m = enter_section_(b, 0, _COLLAPSE_, null);
        r = parse_root_(t, b);
        exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
    }

    protected boolean parse_root_(IElementType t, PsiBuilder b) {
        return parse_root_(t, b, 0);
    }

    static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
        return apiFile(b, l + 1);
    }

    /* ********************************************************** */
    // property|COMMENT|CRLF
    static boolean item_(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "item_")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = property(b, l + 1);
        if (!r) {
            r = consumeToken(b, ApiLexer.COMMENT);
        }
        if (!r) {
            r = consumeToken(b, ApiLexer.CRLF);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // (KEY? SEPARATOR VALUE?) | KEY
    public static boolean property(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, ApiLexer.PROPERTY, "<property>");
        r = property_0(b, l + 1);
        if (!r) {
            r = consumeToken(b, ApiLexer.KEY);
        }
        exit_section_(b, l, m, r, false, recover_property_parser_);
        return r;
    }

    // KEY? SEPARATOR VALUE?
    private static boolean property_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_0")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b);
        r = property_0_0(b, l + 1);
        r = r && consumeToken(b, ApiLexer.SEPARATOR);
        r = r && property_0_2(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // KEY?
    private static boolean property_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_0_0")) {
            return false;
        }
        consumeToken(b, ApiLexer.KEY);
        return true;
    }

    // VALUE?
    private static boolean property_0_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "property_0_2")) {
            return false;
        }
        consumeToken(b, ApiLexer.VALUE);
        return true;
    }

    /* ********************************************************** */
    // !(KEY|SEPARATOR|COMMENT)
    static boolean recover_property(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "recover_property")) {
            return false;
        }
        boolean r;
        Marker m = enter_section_(b, l, _NOT_);
        r = !recover_property_0(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // KEY|SEPARATOR|COMMENT
    private static boolean recover_property_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "recover_property_0")) {
            return false;
        }
        boolean r;
        r = consumeToken(b, ApiLexer.KEY);
        if (!r) {
            r = consumeToken(b, ApiLexer.SEPARATOR);
        }
        if (!r) {
            r = consumeToken(b, ApiLexer.COMMENT);
        }
        return r;
    }

    /* ********************************************************** */
    // item_*
    static boolean apiFile(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "apiFile")) {
            return false;
        }
        while (true) {
            int c = current_position_(b);
            if (!item_(b, l + 1)) {
                break;
            }
            if (!empty_element_parsed_guard_(b, "apiFile", c)) {
                break;
            }
        }
        return true;
    }

    static final Parser recover_property_parser_ = new Parser() {
        @Override
        public boolean parse(PsiBuilder b, int l) {
            return recover_property(b, l + 1);
        }
    };
}
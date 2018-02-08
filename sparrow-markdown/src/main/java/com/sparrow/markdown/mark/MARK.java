package com.sparrow.markdown.mark;

import com.sparrow.constant.CONSTANT;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harry
 * @date 2018/2/6
 */
public enum MARK {
    H1("# ", CONSTANT.ENTER_TEXT, true),
    H2("## ", CONSTANT.ENTER_TEXT, true),
    H3("### ", CONSTANT.ENTER_TEXT, true),
    H4("#### ", CONSTANT.ENTER_TEXT, true),
    H5("##### ", CONSTANT.ENTER_TEXT, true),
    H6("###### ", CONSTANT.ENTER_TEXT, true),
    HORIZONTAL_LINE("---", CONSTANT.ENTER_TEXT, true),
    QUOTE(">", CONSTANT.ENTER_TEXT, false),
    TAB("    ", null, false),
    CHECK_BOX("- [ ] ", CONSTANT.ENTER_TEXT, false),
    DISABLE_CHECK_BOX("- [x] ", CONSTANT.ENTER_TEXT, false),
    CODE("```", "```", false),
    HIGHLIGHT("==", "==", true),
    UNDERLINE("++", "++", true),
    ERASURE("~~", "~~", true),
    ITALIC("*", "*", true),
    LITERARY("", "", false),
    BOLD("**", "**", true);

    private String start;
    private String end;
    private boolean line;

    public boolean isLine() {
        return line;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }


    MARK(String start, String end, boolean line) {
        this.start = start;
        this.end = end;
        this.line = line;
    }
}

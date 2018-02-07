package com.sparrow.markdown.mark;

import com.sparrow.constant.CONSTANT;
import com.sparrow.markdown.parser.MarkParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harry
 * @date 2018/2/6
 */
public enum MARK {
    H1("# ", CONSTANT.ENTER_TEXT, false),
    H2("## ", CONSTANT.ENTER_TEXT, false),
    H3("### ", CONSTANT.ENTER_TEXT, false),
    H4("#### ", CONSTANT.ENTER_TEXT, false),
    H5("##### ", CONSTANT.ENTER_TEXT, false),
    H6("###### ", CONSTANT.ENTER_TEXT, false),
    HORIZONTAL_LINE("---", CONSTANT.ENTER_TEXT, false),
    QUOTE(">", CONSTANT.ENTER_TEXT, true),
    CHECK_BOX("- [ ] ", CONSTANT.ENTER_TEXT, true),
    DISABLE_CHECK_BOX("- [x] ", CONSTANT.ENTER_TEXT, true),
    CODE("```", "```", true),
    HIGHLIGHT("==", "==", false),
    UNDERLINE("++", "++", true),
    ERASURE("~~", "~~", true),
    ITALIC("*", "*", true),
    BOLD("**", "**", true);

    private String start;
    private String end;
    private boolean allowEnter;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public boolean isAllowEnter() {
        return allowEnter;
    }

    MARK(String start, String end, boolean allowEnter) {
        this.start = start;
        this.end = end;
        this.allowEnter = allowEnter;
    }

    public static final List<MARK> CONTAINER = new ArrayList<MARK>();
    static {
        CONTAINER.add(H1);
        CONTAINER.add(H2);
        CONTAINER.add(H3);
        CONTAINER.add(H4);
        CONTAINER.add(H5);
        CONTAINER.add(H6);
        CONTAINER.add(HORIZONTAL_LINE);
        CONTAINER.add(QUOTE);
        CONTAINER.add(CHECK_BOX);
        CONTAINER.add(DISABLE_CHECK_BOX);
        CONTAINER.add(CODE);
        CONTAINER.add(HIGHLIGHT);
        CONTAINER.add(UNDERLINE);
        CONTAINER.add(ERASURE);
        CONTAINER.add(ITALIC);
        CONTAINER.add(BOLD);
    }
}

package com.sparrow.markdown.mark;

import com.sparrow.constant.CONSTANT;

/**
 * @author harry
 * @date 2018/2/6
 */
public enum MARK {
    H1(CONSTANT.ENTER_TEXT+"# ", CONSTANT.ENTER_TEXT, true,"<h1>%1$s</h1>"),
    H2(CONSTANT.ENTER_TEXT+"## ", CONSTANT.ENTER_TEXT, true,"<h2>%1$s</h2>"),
    H3(CONSTANT.ENTER_TEXT+"### ", CONSTANT.ENTER_TEXT, true,"<h3>%1$s</h3>"),
    H4(CONSTANT.ENTER_TEXT+"#### ", CONSTANT.ENTER_TEXT, true,"<h4>%1$s</h4>"),
    H5(CONSTANT.ENTER_TEXT+"##### ", CONSTANT.ENTER_TEXT, true,"<h5>%1$s</h5>"),
    H6(CONSTANT.ENTER_TEXT+"###### ", CONSTANT.ENTER_TEXT, true,"<h6>%1$s</h6>"),
    HORIZONTAL_LINE(CONSTANT.ENTER_TEXT+"---", CONSTANT.ENTER_TEXT, true,"<hr/>"),
    QUOTE(CONSTANT.ENTER_TEXT+">", CONSTANT.ENTER_TEXT, false,"<p class=\"quote\">%1$s</p>"),
    TAB(CONSTANT.ENTER_TEXT+"    ", null, false,"<p class=\"tab\">%1$s</p>"),
    CHECK_BOX(CONSTANT.ENTER_TEXT+"- [ ] ", CONSTANT.ENTER_TEXT, false,"<input type=\"checkbox\" disabled=\"\">%1$s"),
    DISABLE_CHECK_BOX(CONSTANT.ENTER_TEXT+"- [x] ", CONSTANT.ENTER_TEXT, false,"<input type=\"checkbox\" disabled=\"\" checked=\"checked\">%1$s"),
    CODE(CONSTANT.ENTER_TEXT+"```", "```", false,"<p class=\"code\">%1$s</p>"),
    HIGHLIGHT(CONSTANT.ENTER_TEXT+"==", "==", true,"<span class=\"highlight\">%1$s</span>"),
    UNDERLINE("++", "++", true,"<span class=\"underline\">%1$s</span>"),
    ERASURE("~~", "~~", true,"<span class=\"erasure\">%1$s</span>"),
    ITALIC("*", "*", true,"<span class=\"italic\">%1$s</span>"),
    LITERARY("", "", false,""),
    BOLD("**", "**", true,"<span class=\"bold\">%1$s</span>");

    private String start;
    private String end;
    private boolean line;
    private String format;

    public boolean isLine() {
        return line;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getFormat() {
        return format;
    }

    MARK(String start, String end, boolean line, String format) {
        this.start = start;
        this.end = end;
        this.line = line;
        this.format=format;
    }
}

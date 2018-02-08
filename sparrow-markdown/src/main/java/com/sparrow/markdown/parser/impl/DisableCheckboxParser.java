package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class DisableCheckboxParser implements MarkParser {
    private String content;

    @Override
    public String parse(MarkContext parser) {
        return String.format("<input type=\"checkbox\" disabled=\"\" checked=\"checked\">%1$s", this.content);
    }

    @Override public MARK mark() {
        return MARK.DISABLE_CHECK_BOX;
    }
}

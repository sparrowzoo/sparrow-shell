package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class CheckboxParser implements MarkParser {
    private String content;

    public CheckboxParser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<input type=\"checkbox\" disabled=\"\">%1$s", content);
    }
}

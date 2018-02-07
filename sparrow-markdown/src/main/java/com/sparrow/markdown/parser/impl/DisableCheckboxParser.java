package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class DisableCheckboxParser implements MarkParser {
    private String content;

    public DisableCheckboxParser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<input type=\"checkbox\" disabled=\"\" checked=\"checked\">%1$s", this.content);
    }
}

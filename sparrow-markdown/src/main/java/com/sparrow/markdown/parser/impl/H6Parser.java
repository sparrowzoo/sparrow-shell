package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class H6Parser implements MarkParser {
    private String content;

    public H6Parser(String content) {
        this.content = content;
    }
    @Override
    public String parse() {
        return String.format("<h6>%1$s</h6>", content);
    }
}

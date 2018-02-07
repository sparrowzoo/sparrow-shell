package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class H2Parser implements MarkParser {
    private String content;

    public H2Parser(String content) {
        this.content = content;
    }
    @Override
    public String parse() {
        return String.format("<h2>%1$s</h2>", content);
    }
}

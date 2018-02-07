package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class H1Parser implements MarkParser {
    private String content;

    public H1Parser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<h1>%1$s</h1>", content);
    }
}

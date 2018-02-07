package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class H3Parser implements MarkParser {
    private String content;

    public H3Parser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<h3>%1$s</h3>", content);
    }
}

package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class H4Parser implements MarkParser {
    private String content;

    public H4Parser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<h4>%1$s</h4>", content);
    }
}

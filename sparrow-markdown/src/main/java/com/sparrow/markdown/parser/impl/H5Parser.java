package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class H5Parser implements MarkParser {
    private String content;

    public H5Parser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<h5>%1$s</h5>", this.content);
    }
}

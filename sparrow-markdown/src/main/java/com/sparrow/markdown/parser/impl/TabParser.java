package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * Created by harry on 2018/2/7.
 */
public class TabParser implements MarkParser {
    private String content;

    public TabParser(String content) {
        this.content = content;
    }
    @Override
    public String parse() {
        return String.format("<p class=\"tab\">%1$s</p>", content);
    }
}

package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class QuoteParser implements MarkParser {
    private String content;

    public QuoteParser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<p class=\"quote\">%1$s</p>", content);
    }
}

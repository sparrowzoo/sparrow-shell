package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class UnderlineParser implements MarkParser {
    private String content;

    public UnderlineParser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<span class=\"underline\">%1$s</span>", content);
    }
}

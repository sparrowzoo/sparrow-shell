package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class CodeParser implements MarkParser {
    private String content;

    public CodeParser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return String.format("<p class=\"code\">%1$s</p>", content);
    }
}

package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harr
 * @date 2018/2/6
 */
public class LiteraryParser implements MarkParser {
    private String content;

    public LiteraryParser(String content) {
        this.content = content;
    }

    @Override
    public String parse() {
        return content;
    }
}

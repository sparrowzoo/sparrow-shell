package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class QuoteParser implements MarkParser {
    private String content;

    @Override
    public String parse(MarkContext parser) {
        return String.format("<p class=\"quote\">%1$s</p>", content);
    }

    @Override public MARK mark() {
        return MARK.QUOTE;
    }
}

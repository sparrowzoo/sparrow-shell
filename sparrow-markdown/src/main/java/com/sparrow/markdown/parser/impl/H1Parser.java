package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class H1Parser implements MarkParser {
    @Override
    public void parse(MarkContext context) {
       context.append(String.format("<h1>%1$s</h1>", context.getHtml()));
    }

    @Override public MARK mark() {
        return MARK.H1;
    }
}

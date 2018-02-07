package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;

/**
 * Created by harry on 2018/2/6.
 */
public class MarkdownParserComposite implements MarkParser {
    @Override
    public String parse(MarkContext markContext) {
        do {
            markContext.detectStartMark(null);
            if (markContext.getCurrentMark() != null) {
                MarkContext.MARK_PARSER_MAP.get(markContext.getCurrentMark()).parse(markContext);
                continue;
            }
            MarkContext.MARK_PARSER_MAP.get(MARK.LITERARY).parse(markContext);
        }
        while (markContext.getCurrentPointer() <= markContext.getContentLength());
        return "";
    }

    @Override public MARK mark() {
        return null;
    }
}

package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;
import sun.jvm.hotspot.oops.Mark;

/**
 * @author harry
 * @date 2018/2/6
 */
public class BoldParser implements MarkParser {
    private String content;

    @Override
    public String parse(MarkContext markContext) {
        int endMarkIndex=markContext.getContent().indexOf(this.mark().getEnd());
        return String.format("<span class=\"bold\">%1$s</span>", content);
    }

    @Override public MARK mark() {
        return MARK.BOLD;
    }
}

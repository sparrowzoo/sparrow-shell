package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 */
public class BoldParser extends AbstractWithEndTagParser {
    @Override
    public MARK mark() {
        return MARK.BOLD;
    }
}

package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;

/**
 * @author harry
 * @date 2018/2/6
 */
public class ItalicParser extends AbstractWithEndTagParser {

    @Override public MARK mark() {
        return MARK.ITALIC;
    }
}

package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;

/**
 * ![image](url)
 *
 * @author harry
 */
public class ImageParser extends AbstractWithUrlParser {
    @Override
    public MARK mark() {
        return MARK.IMAGE;
    }
}

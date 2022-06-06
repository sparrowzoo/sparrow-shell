package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;

/**
 * [link](url)
 *
 * @author harry
 */
public class HyperLinkParser extends AbstractWithUrlParser {

    @Override
    public MARK mark() {
        return MARK.HYPER_LINK;
    }
}

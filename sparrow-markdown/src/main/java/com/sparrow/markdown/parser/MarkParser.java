package com.sparrow.markdown.parser;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;

/**
 * @author harry
 * @date 2018/2/6
 */
public interface MarkParser {
    String parse(MarkContext markContext);
    MARK mark();
}

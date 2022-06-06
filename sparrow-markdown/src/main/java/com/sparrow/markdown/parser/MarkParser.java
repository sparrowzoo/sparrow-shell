package com.sparrow.markdown.parser;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;

/**
 * @author harry
 * @date 2018/2/6
 */
public interface MarkParser {
    boolean detectStartMark(MarkContext markContext);
    MarkEntity validate(MarkContext mark);
    void parse(MarkContext markContext);
    MARK mark();
}

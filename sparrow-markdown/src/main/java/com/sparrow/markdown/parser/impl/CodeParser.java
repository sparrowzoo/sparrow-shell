package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class CodeParser extends AbstractWithEndTagParser {

    @Override public MarkEntity validate(MarkContext markContext) {
        int startIndex = markContext.getCurrentPointer() + this.mark().getStart().length();
        int endMarkIndex = markContext.getContent().indexOf(this.mark().getEnd(), startIndex);
        if (endMarkIndex <= 1) {
            return null;
        }
        if (endMarkIndex <= startIndex) {
            return null;
        }
        String content = markContext.getContent().substring(markContext.getCurrentPointer()
            + this.mark().getStart().length(), endMarkIndex);
        MarkEntity markEntity = MarkEntity.createCurrentMark(this.mark(), endMarkIndex);
        markEntity.setContent(content);
        return markEntity;
    }

    @Override public MARK mark() {
        return MARK.CODE;
    }
}

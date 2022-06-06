package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;

/**
 * Created by harry on 2018/2/7.
 */
public class TabParser extends AbstractWithEndTagParser {

    @Override public MarkEntity validate(MarkContext markContext) {
        String line;
        //-4 represent tab key
        int pointer=markContext.getCurrentPointer()+this.mark().getStart().length()-4;
        int start=pointer;
        while ((line = markContext.readLine(pointer)).startsWith("    ")) {
            pointer += line.length();
        }
        MarkEntity markEntity = MarkEntity.createCurrentMark(this.mark(), pointer);
        markEntity.setContent(markContext.getContent().substring(start, pointer).replaceAll("\n+","<br/>"));
        return markEntity;
    }

    @Override
    public MARK mark() {
        return MARK.TAB;
    }
}

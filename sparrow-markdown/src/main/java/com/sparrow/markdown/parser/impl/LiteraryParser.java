package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 */
public class LiteraryParser implements MarkParser {

    @Override public boolean detectStartMark(MarkContext markContext) {
        return true;
    }

    @Override
    public MarkEntity validate(MarkContext markContext) {
        int originalPointer = markContext.getCurrentPointer();
        while (markContext.getCurrentPointer()<markContext.getContentLength()&&!markContext.detectNextMark(this.mark())){
            markContext.skipPointer(1);
        }
        MarkEntity currentMark = MarkEntity.createCurrentMark(this.mark(), markContext.getCurrentPointer());
        markContext.setPointer(originalPointer);
        if(markContext.getTempNextMark()!=null){
            currentMark.setNextEntity(markContext.getTempNextMark());
            markContext.setTempNextMark(null);
        }
        return currentMark;
    }

    @Override
    public void parse(MarkContext markContext) {
        String content = markContext.getContent().substring(markContext.getCurrentPointer(), markContext.getCurrentMark().getEnd());
        markContext.setPointer(markContext.getCurrentMark().getEnd());
        markContext.append(String.format(this.mark().getFormat(), content.replaceAll("\n+", "<br/>")));
    }

    @Override
    public MARK mark() {
        return MARK.LITERARY;
    }
}

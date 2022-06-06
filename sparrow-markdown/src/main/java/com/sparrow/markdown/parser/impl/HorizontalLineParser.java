package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;

/**
 * @author harry
 * @date 2018/2/6
 */
public class HorizontalLineParser extends AbstractWithEndTagParser {


    @Override
    public MarkEntity validate(MarkContext markContext) {
        String title = markContext.readLine(markContext.getCurrentPointer() + 1);
        String line = markContext.readLine(markContext.getCurrentPointer()+ title.length(),2);
        if (line.equals(this.mark().getEnd())) {
            MarkEntity markEntity= MarkEntity.createCurrentMark(this.mark(),markContext.getCurrentPointer()+title.length());
            markEntity.setTitle(title);
            return markEntity;
        }
        return null;
    }

    @Override
    public void parse(MarkContext markContext) {
        String title=markContext.getContent().substring(markContext.getCurrentPointer()+1,markContext.getCurrentMark().getEnd());
        markContext.append(String.format(this.mark().getFormat(), title));
        markContext.setPointer(markContext.getCurrentMark().getEnd()+this.mark().getEnd().length());
    }


    @Override
    public MARK mark() {
        return MARK.HORIZONTAL_LINE;
    }
}

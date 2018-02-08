package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkWithIndex;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harr
 * @date 2018/2/6
 */
public class LiteraryParser implements MarkParser {

    @Override
    public void parse(MarkContext markContext) {
        MarkWithIndex mark = markContext.detectStartMark(false);
        //当前文本结束
        if (mark != null) {
            markContext.append(markContext.parseComplex(mark.getPointer(),this.mark()).getHtml());
            return;
        }
        markContext.append(markContext.parseComplex(markContext.getContentLength(),this.mark()).getHtml());
    }

    @Override
    public MARK mark() {
        return MARK.LITERARY;
    }
}

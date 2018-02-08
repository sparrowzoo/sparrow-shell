package com.sparrow.markdown.parser.impl;

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;

/**
 * @author harry
 * @date 2018/2/6
 */
public class CheckboxParser implements MarkParser {
    @Override
    public void parse(MarkContext markContext) {
        int endMarkIndex = markContext.getContent().indexOf(this.mark().getEnd(), markContext.getCurrentPointer());
        if (endMarkIndex > 0) {
            MarkContext innerMarkContext = markContext.parseComplex(endMarkIndex, this.mark());
            markContext.append(String.format("<input type=\"checkbox\" disabled=\"\">%1$s", innerMarkContext.getHtml()));
            return;
        }
        MarkContext.MARK_PARSER_MAP.get(MARK.LITERARY).parse(markContext);
    }

    @Override
    public MARK mark() {
        return MARK.CHECK_BOX;
    }
}

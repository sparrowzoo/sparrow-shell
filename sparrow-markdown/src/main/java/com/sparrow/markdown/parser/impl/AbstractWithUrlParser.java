package com.sparrow.markdown.parser.impl;

import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;
import com.sparrow.markdown.parser.MarkParser;

/**
 * Created by harry on 2018/2/12.
 */
public abstract class AbstractWithUrlParser extends AbstractWithEndTagParser implements MarkParser {
    @Override
    public MarkEntity validate(MarkContext markContext) {
        int startIndex = markContext.getCurrentPointer() + this.mark().getStart().length();
        int endMarkIndex = markContext.getContent().indexOf(this.mark().getEnd(), startIndex);
        if (endMarkIndex == DIGIT.NEGATIVE_ONE) {
            return null;
        }

        String content = markContext.getContent().substring(startIndex, endMarkIndex);

        int split = content.indexOf("](");
        if (split > 0) {
            String title = content.substring(0, split);
            String url = content.substring(split + 2, content.length());
            MarkEntity markEntity = MarkEntity.createCurrentMark(this.mark(), endMarkIndex);
            markEntity.setTitle(title);
            markEntity.setUrl(url);
            return markEntity;
        }
        return null;
    }

    @Override
    public void parse(MarkContext markContext) {
        String title = markContext.getCurrentMark().getTitle();
        String utl = markContext.getCurrentMark().getUrl();

        //如果包含复杂结构，至少需要两个字符
        if (title.length() <= 2 || MarkContext.CHILD_MARK_PARSER.get(markContext.getCurrentMark().getMark()) == null) {
            markContext.append(String.format(this.mark().getFormat(), utl, title));
            markContext.setPointer(markContext.getCurrentMark().getEnd() + this.mark().getEnd().length());
            return;
        }
        markContext.append(String.format(this.mark().getFormat(), utl, markContext.getInnerHtml(this.mark(), title)));
        markContext.setPointer(markContext.getCurrentMark().getEnd() + this.mark().getEnd().length());
    }
}

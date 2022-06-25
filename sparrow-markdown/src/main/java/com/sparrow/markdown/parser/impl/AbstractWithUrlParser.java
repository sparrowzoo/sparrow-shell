/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.markdown.parser.impl;

import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;
import com.sparrow.markdown.parser.MarkParser;

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

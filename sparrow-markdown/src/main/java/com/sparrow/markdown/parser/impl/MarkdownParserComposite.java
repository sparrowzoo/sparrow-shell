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

import com.sparrow.protocol.constant.magic.CharSymbol;
import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;
import com.sparrow.markdown.parser.MarkParser;

/**
 * Created by harry on 2018/2/6.
 */
public class MarkdownParserComposite implements MarkParser {
    private static MarkdownParserComposite instance = new MarkdownParserComposite();

    private MarkdownParserComposite() {
    }

    public static MarkdownParserComposite getInstance() {
        return instance;
    }

    @Override public boolean detectStartMark(MarkContext markContext) {
        return false;
    }

    @Override
    public MarkEntity validate(MarkContext mark) {
        return null;
    }

    @Override
    public void parse(MarkContext markContext) {
        if (markContext.getContentLength() == 0) {
            return;
        }
        //if first char is not \n then fill
        if (markContext.getParentMark() == null) {
            if (markContext.getContent().charAt(0) != CharSymbol.ENTER) {
                markContext.setContent(CharSymbol.ENTER + markContext.getContent());
            }
            if (markContext.getContent().charAt(markContext.getContentLength() - 1) != CharSymbol.ENTER) {
                markContext.setContent(markContext.getContent() + CharSymbol.ENTER);
            }
        }
        do {
            //detect start mark
            if (markContext.getCurrentMark() != null && markContext.getCurrentMark().getNextEntity() != null) {
                markContext.setCurrentMark(markContext.getCurrentMark().getNextEntity());
            } else {
                markContext.setCurrentMark(null);
                markContext.detectCurrentMark(markContext.getParentMark());
            }
            if (markContext.getCurrentMark() != null) {
                MarkContext.MARK_PARSER_MAP.get(markContext.getCurrentMark().getMark()).parse(markContext);
                continue;
            }

            MarkParser literaryParse = MarkContext.MARK_PARSER_MAP.get(MARK.LITERARY);
            MarkEntity markEntity = literaryParse.validate(markContext);
            if (markEntity != null) {
                markContext.setCurrentMark(markEntity);
                //按文本处理
                literaryParse.parse(markContext);
            }
        }
        while (markContext.getCurrentPointer() < markContext.getContentLength());
    }

    @Override
    public MARK mark() {
        return null;
    }
}

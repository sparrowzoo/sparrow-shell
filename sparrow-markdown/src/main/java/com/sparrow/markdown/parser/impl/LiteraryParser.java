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

import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;
import com.sparrow.markdown.parser.MarkParser;

public class LiteraryParser implements MarkParser {

    @Override public boolean detectStartMark(MarkContext markContext) {
        return true;
    }

    @Override
    public MarkEntity validate(MarkContext markContext) {
        int originalPointer = markContext.getCurrentPointer();
        while (markContext.getCurrentPointer() < markContext.getContentLength() && !markContext.detectNextMark(this.mark())) {
            markContext.skipPointer(1);
        }
        MarkEntity currentMark = MarkEntity.createCurrentMark(this.mark(), markContext.getCurrentPointer());
        markContext.setPointer(originalPointer);
        if (markContext.getTempNextMark() != null) {
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

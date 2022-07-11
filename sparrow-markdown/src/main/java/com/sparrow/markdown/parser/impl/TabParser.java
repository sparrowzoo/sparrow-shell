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

public class TabParser extends AbstractWithEndTagParser {

    @Override public MarkEntity validate(MarkContext markContext) {
        String line;
        //-4 represent tab key
        int pointer = markContext.getCurrentPointer() + this.mark().getStart().length() - 4;
        int start = pointer;
        while ((line = markContext.readLine(pointer)).startsWith("    ")) {
            pointer += line.length();
        }
        MarkEntity markEntity = MarkEntity.createCurrentMark(this.mark(), pointer);
        markEntity.setContent(markContext.getContent().substring(start, pointer).replaceAll("\n+", "<br/>"));
        return markEntity;
    }

    @Override
    public MARK mark() {
        return MARK.TAB;
    }
}

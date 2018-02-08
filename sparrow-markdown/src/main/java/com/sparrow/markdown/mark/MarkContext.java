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
package com.sparrow.markdown.mark;

import com.sparrow.markdown.parser.MarkParser;
import com.sparrow.markdown.parser.impl.CheckboxParser;
import com.sparrow.markdown.parser.impl.CodeParser;
import com.sparrow.markdown.parser.impl.DisableCheckboxParser;
import com.sparrow.markdown.parser.impl.ErasureParser;
import com.sparrow.markdown.parser.impl.H1Parser;
import com.sparrow.markdown.parser.impl.H2Parser;
import com.sparrow.markdown.parser.impl.H3Parser;
import com.sparrow.markdown.parser.impl.H4Parser;
import com.sparrow.markdown.parser.impl.H5Parser;
import com.sparrow.markdown.parser.impl.H6Parser;
import com.sparrow.markdown.parser.impl.HighlightParser;
import com.sparrow.markdown.parser.impl.HorizontalLineParser;
import com.sparrow.markdown.parser.impl.ItalicParser;
import com.sparrow.markdown.parser.impl.LiteraryParser;
import com.sparrow.markdown.parser.impl.QuoteParser;
import com.sparrow.markdown.parser.impl.UnderlineParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by harry
 */
public class MarkContext {
    public MarkContext(String content) {
        this.content = content;
        this.currentPointer = 0;
        this.markStatus = MARK_STATUS.START_DETECTING;
        this.contentLength = this.content.length();
    }

    public static final int MAX_MARK_LENGTH = 7;
    private static final int MARK_COUNT = 20;
    public static final List<MARK> CONTAINER = new ArrayList<MARK>(MARK_COUNT);
    public static final Map<MARK, MarkParser> MARK_PARSER_MAP = new HashMap<MARK, MarkParser>(MARK_COUNT);

    static {
        CONTAINER.add(MARK.H1);
        CONTAINER.add(MARK.H2);
        CONTAINER.add(MARK.H3);
        CONTAINER.add(MARK.H4);
        CONTAINER.add(MARK.H5);
        CONTAINER.add(MARK.H6);
        CONTAINER.add(MARK.HORIZONTAL_LINE);
        CONTAINER.add(MARK.QUOTE);
        CONTAINER.add(MARK.CHECK_BOX);
        CONTAINER.add(MARK.DISABLE_CHECK_BOX);
        CONTAINER.add(MARK.CODE);
        CONTAINER.add(MARK.HIGHLIGHT);
        CONTAINER.add(MARK.UNDERLINE);
        CONTAINER.add(MARK.ERASURE);
        CONTAINER.add(MARK.ITALIC);
        CONTAINER.add(MARK.BOLD);

        MARK_PARSER_MAP.put(MARK.H1, new H1Parser());
        MARK_PARSER_MAP.put(MARK.H2, new H2Parser());
        MARK_PARSER_MAP.put(MARK.H3, new H3Parser());
        MARK_PARSER_MAP.put(MARK.H4, new H4Parser());
        MARK_PARSER_MAP.put(MARK.H5, new H5Parser());
        MARK_PARSER_MAP.put(MARK.H6, new H6Parser());
        MARK_PARSER_MAP.put(MARK.HORIZONTAL_LINE, new HorizontalLineParser());
        MARK_PARSER_MAP.put(MARK.QUOTE, new QuoteParser());
        MARK_PARSER_MAP.put(MARK.CHECK_BOX, new CheckboxParser());
        MARK_PARSER_MAP.put(MARK.DISABLE_CHECK_BOX, new DisableCheckboxParser());
        MARK_PARSER_MAP.put(MARK.CODE, new CodeParser());
        MARK_PARSER_MAP.put(MARK.HIGHLIGHT, new HighlightParser());
        MARK_PARSER_MAP.put(MARK.UNDERLINE, new UnderlineParser());
        MARK_PARSER_MAP.put(MARK.ERASURE, new ErasureParser());
        MARK_PARSER_MAP.put(MARK.LITERARY, new LiteraryParser());
        MARK_PARSER_MAP.put(MARK.ITALIC, new ItalicParser());

    }

    private int contentLength;
    private MARK_STATUS markStatus;
    /**
     * 当前字符指针
     */
    private int currentPointer;
    private String content = null;
    private List<MarkParser> markParserList = new ArrayList<MarkParser>();
    /**
     * 当前mark
     */
    private MARK currentMark;

    public int getCurrentPointer() {
        return currentPointer;
    }

    public void forwardPointer(int currentPointer) {
        this.currentPointer += currentPointer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MARK getCurrentMark() {
        return currentMark;
    }

    public void setCurrentMark(MARK currentMark) {
        this.currentMark = currentMark;
    }

    public List<MarkParser> getMarkParserList() {
        return markParserList;
    }

    public void setMarkParserList(List<MarkParser> markParserList) {
        this.markParserList = markParserList;
    }

    public MARK_STATUS getMarkStatus() {
        return markStatus;
    }

    public void setMarkStatus(MARK_STATUS markStatus) {
        this.markStatus = markStatus;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String readMaxMark() {
        return this.content.substring(this.currentPointer, this.currentPointer + MAX_MARK_LENGTH);
    }

    public String readLine() {
        StringBuilder line = new StringBuilder(100);
        char enter = '\n';
        char current;
        while ((current = this.content.charAt(currentPointer)) != enter) {
            line.append(current);
        }
        return line.toString();
    }

    public void detectStartMark(Boolean isLine) {
        if (this.getCurrentMark() != null) {
            return;
        }

        String markContent = this.readMaxMark();
        for (MARK mark : MarkContext.CONTAINER) {
            if (markContent.startsWith(mark.getStart()) && isLine == null || isLine == mark.isLine()) {
                this.setCurrentMark(mark);
                this.forwardPointer(mark.getStart().length());
                return;
            }
        }
    }
}

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

import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.markdown.mark.MARK;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.mark.MarkEntity;
import com.sparrow.markdown.parser.MarkParser;
import java.util.ArrayList;
import java.util.List;

/**
 * @author harry
 */
public class TableParser extends AbstractWithEndTagParser {

    @Override
    public MarkEntity validate(MarkContext markContext) {
        int pointer = markContext.getCurrentPointer() + this.mark().getStart().length();
        String title = markContext.readLine(pointer);
        if (!title.contains(SYMBOL.VERTICAL_LINE)) {
            return null;
        }
        pointer += title.length();
        String split = markContext.readLine(pointer);
        if (!split.contains(SYMBOL.VERTICAL_LINE)) {
            return null;
        }

        pointer += split.length();
        String tdLine = markContext.readLine(pointer);
        if (!tdLine.contains(SYMBOL.VERTICAL_LINE)) {
            return null;
        }

        String verticalLineSplit="\\|";
        String[] titleArray = title.trim().split(verticalLineSplit);
        String[] splitArray = split.trim().split(verticalLineSplit);
        String[] tdArray = tdLine.trim().split(verticalLineSplit);

        if (titleArray.length != splitArray.length || titleArray.length != tdArray.length) {
            return null;
        }

        String matchSplit = "---";
        for (String s : splitArray) {
            if (!matchSplit.equals(s)) {
                return null;
            }
        }

        List<String[]> tdList = new ArrayList<String[]>(32);
        tdList.add(tdArray);
        pointer += tdLine.length();
        do {
            tdLine = markContext.readLine(pointer);
            tdArray = tdLine.split(verticalLineSplit);
            if (tdArray.length != titleArray.length) {
                break;
            }
            tdList.add(tdArray);
            pointer += tdLine.length();
        }
        while (true);

        MarkEntity markEntity = MarkEntity.createCurrentMark(this.mark(), pointer);
        markEntity.setTdList(tdList);
        markEntity.setTitleArray(titleArray);
        return markEntity;
    }

    @Override
    public void parse(MarkContext markContext) {
        String[] titleArray = markContext.getCurrentMark().getTitleArray();
        List<String[]> tdList = markContext.getCurrentMark().getTdList();
        StringBuilder table = new StringBuilder(1024);
        table.append("<table class=\"pure-table pure-table-horizontal\"><thead><tr>");
        for (String title : titleArray) {
            table.append(String.format("<th>%1$s</th>", title));
        }
        table.append("<tr/></thead>");
        table.append("<tbody>");
        for (String[] tdArray : tdList) {
            table.append("<tr>");
            for (String td : tdArray) {
                table.append(String.format("<td>%1$s</td>", td));
            }
            table.append("</tr>");
        }
        table.append("</tbody></table>");
        markContext.append(table.toString());
        markContext.setPointer(markContext.getCurrentMark().getEnd());
    }

    @Override
    public MARK mark() {
        return MARK.TABLE;
    }
}

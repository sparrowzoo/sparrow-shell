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

package com.sparrow.mvc.ui.grid;

import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.StringUtility;

public class Head {
    private boolean checkBox;
    private int colSpan;
    private String head;
    private String gridId;

    private Head(String config, String gridId) {
        this.gridId = gridId;
        this.colSpan = DIGIT.ONE;
        if ("checkBox".equalsIgnoreCase(config)) {
            this.checkBox = true;
            this.head = config;
        } else {
            this.checkBox = false;
            if (config.contains(Symbol.DOLLAR)) {
                String[] headArray = config.split("\\$");
                this.head = headArray[DIGIT.ZERO];
                this.colSpan = Integer.valueOf(headArray[DIGIT.ONE]);
            } else {
                this.head = config;
            }
        }
    }

    public static String parse(String config, String gridId, int indent) {
        String[] headTitle = config.split("\\|");
        String indent1 = StringUtility.getIndent(indent + DIGIT.ONE);
        String indent2 = StringUtility.getIndent(indent + DIGIT.TOW);
        StringBuilder html = new StringBuilder(indent1);
        html.append("<tr>");
        for (String head : headTitle) {
            html.append(indent2);
            html.append(new Head(head, gridId).toString());
        }
        html.append(indent1);
        html.append("</tr>");
        return html.toString();
    }

    @Override
    public String toString() {
        if (this.checkBox) {
            return String.format(
                "<th style=\"text-align:center;\"><input id=\"%1$sCheckAll\" onclick=\"$.gridView.allCheckClick(this);\" type=\"checkbox\"/></th>",
                this.gridId
            );
        } else {
            String headHtml;
            if (this.colSpan > DIGIT.ONE) {
                headHtml = String.format("<th style=\"text-align:center;\" colspan=\"%2$s\">%1$s</th>",
                    this.head,
                    this.colSpan);
            } else {
                headHtml = String.format("<th style=\"text-align:center;\">%1$s</th>", this.head);
            }
            return headHtml;
        }
    }
}

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

import com.sparrow.protocol.constant.Constant;

public enum MARK {
    H1(Constant.ENTER_TEXT_N + "# ", Constant.ENTER_TEXT_N, "<h1>%1$s</h1>"),
    H2(Constant.ENTER_TEXT_N + "## ", Constant.ENTER_TEXT_N, "<h2>%1$s</h2>"),
    H3(Constant.ENTER_TEXT_N + "### ", Constant.ENTER_TEXT_N, "<h3>%1$s</h3>"),
    H4(Constant.ENTER_TEXT_N + "#### ", Constant.ENTER_TEXT_N, "<h4>%1$s</h4>"),
    H5(Constant.ENTER_TEXT_N + "##### ", Constant.ENTER_TEXT_N, "<h5>%1$s</h5>"),
    H6(Constant.ENTER_TEXT_N + "###### ", Constant.ENTER_TEXT_N, "<h6>%1$s</h6>"),
    HORIZONTAL_LINE(Constant.ENTER_TEXT_N, Constant.ENTER_TEXT_N + "---" + Constant.ENTER_TEXT_N, "<br/>%1$s<br/><hr/>"),
    QUOTE(Constant.ENTER_TEXT_N + "&gt;", Constant.ENTER_TEXT_N + Constant.ENTER_TEXT_N, "<p class=\"md-quote\">%1$s</p>"),
    TAB(Constant.ENTER_TEXT_N + Constant.ENTER_TEXT_N + "    ", null, "<p class=\"md-tab\">%1$s</p>"),
    CHECK_BOX(Constant.ENTER_TEXT_N + "- [ ] ", Constant.ENTER_TEXT_N, "<input type=\"checkbox\" disabled=\"\"><label>%1$s</label>"),
    DISABLE_CHECK_BOX(Constant.ENTER_TEXT_N + "- [x] ", Constant.ENTER_TEXT_N, "<input type=\"checkbox\" disabled=\"\" checked=\"checked\"><label>%1$s</label>"),
    CODE(Constant.ENTER_TEXT_N + "```" + Constant.ENTER_TEXT_N, Constant.ENTER_TEXT_N + "```" + Constant.ENTER_TEXT_N, "<pre class=\"prettyprint linenums:1\">%1$s</pre>"),
    HIGHLIGHT("==", "==", "<span class=\"md-highlight\">%1$s</span>"),
    UNDERLINE("++", "++", "<span class=\"md-underline\">%1$s</span>"),
    ERASURE("~~", "~~", "<span class=\"md-erasure\">%1$s</span>"),
    ITALIC("*", "*", "<span class=\"md-italic\">%1$s</span>"),
    LITERARY("", null, "%1$s"),
    BOLD("**", "**", "<span class=\"md-bold\">%1$s</span>"),
    IMAGE("![", ")", "<div class=\"md-image-package\"><img src=\"%1$s\"/><br/><label>%2$s</label></div>"),
    HYPER_LINK("[", ")", "<a href=\"%1$s\">%2$s</a>"),
    ORDERED_LIST(Constant.ENTER_TEXT_N + "1. ", null, null),
    UNORDERED_LIST(Constant.ENTER_TEXT_N + "- ", null, null),
    TABLE(Constant.ENTER_TEXT_N, null, null);

    private String start;
    private String end;
    private String format;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getFormat() {
        return format;
    }

    MARK(String start, String end, String format) {
        this.start = start;
        this.end = end;
        this.format = format;
    }
}

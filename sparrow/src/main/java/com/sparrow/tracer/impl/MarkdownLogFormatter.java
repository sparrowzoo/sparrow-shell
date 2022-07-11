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

package com.sparrow.tracer.impl;


import com.sparrow.tracer.LogFormatter;
import com.sparrow.tracer.Span;
import com.sparrow.tracer.TagPair;

import java.util.Map;

public class MarkdownLogFormatter implements LogFormatter {
    private String spanToMarkdown(SpanImpl span) {
        return span.getId() + "|"
                + (span.parent() == null ? -1 : span.parent().getId()) + "|"
                + (span.isFollower() ? "F" : "C") + "|"
                + span.getName() + "|"
                + span.getStartTime() + "|"
                + ((span.getEndTime() == null ? System.currentTimeMillis() : span.getEndTime()) - span.getStartTime()) + "ms";
    }

    @Override
    public String format(String traceId,Map<Span, Integer> logContainer) {
        StringBuilder walking = new StringBuilder("traceId:" + traceId);
        walking.append("\ndepth|span-id|parent-id|F/C|span-name | start|duration|logs");
        walking.append("\n---|---|---|---|---|---|---|---");
        for (Span span : logContainer.keySet()) {
            walking.append("\n");
            walking.append(logContainer.get(span));
            walking.append("|");
            walking.append(this.spanToMarkdown((SpanImpl) span));
            walking.append("|");
            walking.append(this.logs((SpanImpl) span));
        }
        return walking.toString();
    }

    private String logs(SpanImpl span) {
        StringBuilder logs = new StringBuilder();
        int i = 1;
        for (TagPair tagPair: span.getTag()) {
            logs.append(String.format("%s. %s:%s<br/>", i++, tagPair.getKey(), tagPair.getValue()));
        }
        return logs.toString();
    }
}

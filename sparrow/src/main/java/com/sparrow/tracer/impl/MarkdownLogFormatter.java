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

package com.sparrow.tracer.impl;

import com.sparrow.tracer.LogFormatter;
import com.sparrow.tracer.Span;

import java.util.Map;

public class LineLogFormatter implements LogFormatter {
    @Override
    public String format(String traceId, Map<Span, Integer> logContainer) {
        StringBuilder sb = new StringBuilder();
        for (Span span : logContainer.keySet()) {
            sb.append(span.getName() + "-" + traceId + "\n");
        }
        return sb.toString();
    }
}

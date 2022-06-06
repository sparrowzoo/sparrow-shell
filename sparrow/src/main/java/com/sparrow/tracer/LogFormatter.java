package com.sparrow.tracer;

import java.util.Map;

public interface LogFormatter {
    String format(String traceId, Map<Span, Integer> logContainer);
}

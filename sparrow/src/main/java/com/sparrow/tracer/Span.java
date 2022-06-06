package com.sparrow.tracer;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface Span {
    Tracer getTracer();

    void finish();

    String getName();

    String getId();

    Span parent();

    Span follower();

    List<Span> children();

    int duration();

    /**
     * Set a key:value tag on the Span.
     */
    Span setTag(String key, String value);

    void format(String format, Object... argArray);

    /**
     * Set a key:value tag on the Span.
     */
    Span setTag(String key);

    /**
     * Same as {@link #setTag(String, String)}, but for boolean values.
     */
    Span setTag(String key, boolean value);

    /**
     * Same as {@link #setTag(String, String)}, but for numeric values.
     */
    Span setTag(String key, Number value);

    Span setTag(String key, Collection t);
}

package com.sparrow.tracer;

/**
 * @author zhanglz
 */
public class TagPair {
    private String key;
    private Object value;

    public TagPair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}

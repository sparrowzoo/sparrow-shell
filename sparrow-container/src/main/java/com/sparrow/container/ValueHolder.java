package com.sparrow.container;

/**
 * @author by harry
 */
public class ValueHolder {

    private  String name;

    private  Object value;

    private Class type;

    private boolean ref;

    public ValueHolder(String name, Object value, Class type,boolean ref) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.ref=ref;
    }

    public ValueHolder(String name, Object value,boolean ref) {
        this.name = name;
        this.value = value;
        this.ref=ref;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public Class getType() {
        return type;
    }

    public boolean isRef() {
        return ref;
    }
}

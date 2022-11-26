package com.sparrow.jdk.hash;

import java.util.Objects;

public class Obj {
    private String name;

    public Obj(String name) {
        this.name = name;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Obj obj = (Obj) o;
        return Objects.equals(name, obj.name);
    }

    @Override public int hashCode() {
        System.out.println("hashcode:" + super.hashCode());
        int newHashCode = Objects.hash(name);
        System.out.println("new hash code:" + newHashCode);
        return newHashCode;
    }
}

package com.sparrow.protocol;

public class IdentityVO<T> implements VO {
    public IdentityVO(Long id, T data) {
        this.id = id;
        this.data = data;
    }

    public IdentityVO(Long id) {
        this.id = id;
    }

    private Long id;
    private T data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

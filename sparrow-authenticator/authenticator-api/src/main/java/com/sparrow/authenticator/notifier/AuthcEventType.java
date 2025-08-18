package com.sparrow.authenticator.notifier;

public enum AuthcEventType implements EventType {
    LOGIN(1),
    AUTHC(2);

    private Integer type;

    private AuthcEventType(Integer type) {
        this.type = type;
    }

    @Override
    public Integer getType() {
        return this.type;
    }

    public static AuthcEventType getEventType(Integer type) {
        for (AuthcEventType e : AuthcEventType.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}

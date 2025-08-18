package com.sparrow.authenticator.notifier;

public interface Event<T> {
   EventType getEventType();
   T getBody();
}

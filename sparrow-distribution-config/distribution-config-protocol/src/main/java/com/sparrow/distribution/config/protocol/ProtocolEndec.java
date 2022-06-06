package com.sparrow.distribution.config.protocol;

public interface ProtocolEndec {
    byte[] encode(Protocol protocol);

    Protocol decode(byte[] bytes);
}

package com.sparrow.distribution.config.repository;

import com.sparrow.distribution.config.protocol.Protocol;

import java.util.Properties;

public interface Repository {
    void fetch(String fileName);

    void receive(Protocol protocol);

    Properties read(String fileName);
}

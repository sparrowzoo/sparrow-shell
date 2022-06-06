package com.sparrow.distribution.config.repository;

import java.util.Properties;
import java.util.Set;

public interface PropertiesContainer {
    void put(String fileName);

    Properties read(String fileName);

    void remove(String fileName);

    Set<String> getAllFileName();
}

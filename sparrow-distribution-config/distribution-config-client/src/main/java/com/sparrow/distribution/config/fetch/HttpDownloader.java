package com.sparrow.distribution.config.fetch;

import java.nio.ByteBuffer;

public interface HttpDownloader {
    ByteBuffer fetch(String fileName);
}

package com.sparrow.distribution.config.fetch;

import com.sparrow.utility.HttpClient;

import java.nio.ByteBuffer;

public class DefaultHttpHttpDownloader implements HttpDownloader{
    private String httpUrl;

    public DefaultHttpHttpDownloader(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public ByteBuffer fetch(String fileName) {
        return HttpClient.download(this.httpUrl + "fileName=?" + fileName);
    }
}

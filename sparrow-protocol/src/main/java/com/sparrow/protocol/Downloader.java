package com.sparrow.protocol;

public interface Downloader {
    String downloadImage(String imageUrl, Long authorId) throws BusinessException;
}

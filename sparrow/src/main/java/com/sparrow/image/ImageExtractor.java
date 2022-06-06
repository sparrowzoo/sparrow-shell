package com.sparrow.image;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Downloader;
import com.sparrow.protocol.dto.ImageDTO;

import java.util.List;

public interface ImageExtractor {
    boolean hasOuterImage(List<ImageDTO> images);

    List<ImageDTO> extractImage(String content, Long authorId, Downloader downloader) throws BusinessException;

    String replace(String content, List<ImageDTO> images);

    String filter(String content);

    String restore(String content, List<ImageDTO> images);
}

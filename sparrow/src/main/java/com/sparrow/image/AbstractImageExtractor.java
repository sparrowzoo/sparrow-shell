package com.sparrow.image;

import com.sparrow.constant.Config;
import com.sparrow.constant.Regex;
import com.sparrow.constant.SparrowError;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Downloader;
import com.sparrow.protocol.constant.EXTENSION;
import com.sparrow.protocol.dto.ImageDTO;
import com.sparrow.support.file.FileNameProperty;
import com.sparrow.utility.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractImageExtractor implements ImageExtractor, ContainerAware {
    private ImageExtractorRegistry imageExtractorRegistry;

    public void setImageExtractorRegistry(ImageExtractorRegistry imageExtractorRegistry) {
        this.imageExtractorRegistry = imageExtractorRegistry;
    }

    public boolean hasOuterImage(List<ImageDTO> images) {
        if (CollectionsUtility.isNullOrEmpty(images)) {
            return false;
        }
        for (ImageDTO image : images) {
            if (!image.getInner()) {
                return true;
            }
        }
        return false;
    }

    protected abstract String getImageRegexMark();

    protected abstract String getImageUrl(Matcher matcher);

    protected abstract String getRemark(Matcher matcher);

    public List<ImageDTO> extractImage(String content, Long authorId, Downloader downloader) throws BusinessException {
        Matcher imageMatcher = Pattern.compile(this.getImageRegexMark(),
                Regex.OPTION).matcher(content);
        List<ImageDTO> images = new ArrayList<>();
        while (imageMatcher.find()) {
            String imageUrl = this.getImageUrl(imageMatcher);
            FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(imageUrl);
            if (!StringUtility.isNullOrEmpty(imageUrl)) {
                boolean inner = false;
                String fileId =null;
                String extension = fileNameProperty.getExtension();
                //非站内资源引用img1.sparrowzoo.net
                //非资源文件r.sparrowzoo.net
                if (!RegexUtility.matches(imageUrl, Regex.URL_INNER_IMAGE.pattern()) &&
                        !imageUrl.startsWith(ConfigUtility.getValue(Config.RESOURCE))) {
                    fileId = downloader.downloadImage(imageUrl, authorId);
                    if (extension == null) {
                        extension = EXTENSION.PNG;
                    }
                    if (fileId == null) {
                        throw new BusinessException(SparrowError.IMAGE_EXTENSION_NOT_FOUND, Collections.singletonList(imageUrl));
                    }
                } else {
                    fileId =fileNameProperty.getName();
                    inner = true;
                }
                String originImgHtml = imageMatcher.group();
                images.add(new ImageDTO(fileId, originImgHtml, extension, this.getRemark(imageMatcher), inner));
            }
        }
        return images;
    }

    @Override
    public void aware(Container container, String beanName) {
        this.imageExtractorRegistry.register(this);
    }
}

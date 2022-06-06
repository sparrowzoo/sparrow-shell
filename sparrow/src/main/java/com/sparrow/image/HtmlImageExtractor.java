package com.sparrow.image;

import com.sparrow.constant.File;
import com.sparrow.constant.Regex;
import com.sparrow.protocol.constant.CONSTANT;
import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.protocol.dto.ImageDTO;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.HtmlUtility;

import java.util.List;
import java.util.regex.Matcher;

public class HtmlImageExtractor extends AbstractImageExtractor {


    @Override
    public String getImageRegexMark() {
        return Regex.TAG_IMAGE;
    }

    @Override
    public String getImageUrl(Matcher matcher) {
        return matcher.group(2);
    }

    @Override
    protected String getRemark(Matcher matcher) {
        return SYMBOL.EMPTY;
    }

    @Override
    public String replace(String content, List<ImageDTO> images) {
        if (CollectionsUtility.isNullOrEmpty(images)) {
            return content;
        }
        //先将img 替换为非html标签，防止下边过滤html时被过滤
        for (ImageDTO image : images) {
            content = content.replace(
                    image.getImageMark(), String.format(CONSTANT.IMAGE_TEMP_MARK, image.getImageId()));
        }
        return content;
    }

    @Override
    public String filter(String content) {
        return HtmlUtility.filterHTML(content);
    }

    @Override
    public String restore(String content, List<ImageDTO> images) {
        if (CollectionsUtility.isNullOrEmpty(images)) {
            return content;
        }
        for (ImageDTO image : images) {
            String url = FileUtility.getInstance().getShufflePath(
                    image.getImageId(), image.getExtension(), true,
                    File.SIZE.BIG);
            String imageHtml = String.format(CONSTANT.IMAGE_HTML_MARK_FORMAT,
                    url);
            content = content.replace(String.format(CONSTANT.IMAGE_TEMP_MARK, image.getImageId()), imageHtml);
        }
        return content;
    }
}

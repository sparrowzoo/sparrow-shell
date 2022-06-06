package com.sparrow.markdown.controller;

import com.sparrow.image.ImageExtractor;
import com.sparrow.image.ImageExtractorRegistry;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;
import com.sparrow.markdown.parser.impl.MarkdownParserComposite;
import com.sparrow.markdown.vo.MarkdownVO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Downloader;
import com.sparrow.protocol.dto.ImageDTO;
import com.sparrow.protocol.enums.EDITOR_TYPE;
import com.sparrow.utility.StringUtility;

import java.util.List;

public class MarkdownPreviewController {
    private ImageExtractorRegistry imageExtractorRegistry;
    private Downloader downloader;

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public void setImageExtractorRegistry(ImageExtractorRegistry imageExtractorRegistry) {
        this.imageExtractorRegistry = imageExtractorRegistry;
    }

    public MarkdownVO preview(String markdown, Long authorId) throws BusinessException {
        if (StringUtility.isNullOrEmpty(markdown)) {
            return new MarkdownVO("");
        }


        ImageExtractor imageExtractor = this.imageExtractorRegistry.get(EDITOR_TYPE.MARKDOWN);
        List<ImageDTO> images = imageExtractor.extractImage(markdown, authorId, this.downloader);
        markdown = imageExtractor.replace(markdown, images);

        MarkContext markContext = new MarkContext(markdown);
        MarkParser markParser = MarkdownParserComposite.getInstance();
        markParser.parse(markContext);
        String html = markContext.getHtml();
        MarkdownVO markdownVO = new MarkdownVO(html);
        if (imageExtractor.hasOuterImage(images)) {
            markdownVO.setMarkdown(markdown);
        }
        return markdownVO;
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.markdown.controller;

import com.sparrow.image.ImageExtractor;
import com.sparrow.image.ImageExtractorRegistry;
import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;
import com.sparrow.markdown.parser.impl.MarkdownParserComposite;
import com.sparrow.markdown.vo.MarkdownVO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.support.Downloader;
import com.sparrow.protocol.dto.ImageDTO;
import com.sparrow.protocol.enums.EditorType;
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

        ImageExtractor imageExtractor = this.imageExtractorRegistry.get(EditorType.MARKDOWN);
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

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

package com.sparrow.image;

import com.sparrow.constant.File;
import com.sparrow.constant.Regex;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
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
        return Symbol.EMPTY;
    }

    @Override
    public String replace(String content, List<ImageDTO> images) {
        if (CollectionsUtility.isNullOrEmpty(images)) {
            return content;
        }
        //先将img 替换为非html标签，防止下边过滤html时被过滤
        for (ImageDTO image : images) {
            content = content.replace(
                image.getImageMark(), String.format(Constant.IMAGE_TEMP_MARK, image.getImageId()));
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
            String imageHtml = String.format(Constant.IMAGE_HTML_MARK_FORMAT,
                url);
            content = content.replace(String.format(Constant.IMAGE_TEMP_MARK, image.getImageId()), imageHtml);
        }
        return content;
    }
}

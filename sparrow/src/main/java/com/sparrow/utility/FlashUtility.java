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

package com.sparrow.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sparrow.constant.*;
import com.sparrow.constant.File.SIZE;
import com.sparrow.protocol.constant.magic.Digit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlashUtility {
    Logger logger = LoggerFactory.getLogger(FlashUtility.class);
    private final String youku = "http:\\/\\/player.youku.com\\/player.php.*?sid\\/(.*?)\\/v\\.swf";
    private final String sohu = "http:\\/\\/share.vrs.sohu.com\\/([0-9]*?)\\/v\\.swf.*";

    private final String youkuAPI = "http://v.youku.com/player/getPlayList/VideoIDS/%1$s";
    private final String sohuapi = "http://hot.vrs.sohu.com/vrs_flash.action?vid=%1$s";

    private static FlashUtility d3FalshUtil = new FlashUtility();

    public static FlashUtility getInstance() {
        return d3FalshUtil;
    }

    // http://player.youku.com/player.php/Type/Folder/Fid/17354925/Ob/1/Pt/0/sid/XMzgwODczNDY4/v.swf
    private final Pattern youkuPatter = Pattern.compile(this.youku,
        Regex.OPTION);

    // http://share.vrs.sohu.com/637677/v.swf&autoplay=false
    private final Pattern sohuPatter = Pattern.compile(this.sohu, Regex.OPTION);

    private final Pattern youkuPic = Pattern.compile("\"logo\":\"(.*?)\"",
        Regex.OPTION);
    private final Pattern sohuPic = Pattern.compile("\"coverImg\":\"(.*?)\"",
        Regex.OPTION);

    public String getThumbnailUrl(String swfSrc) {
        // flash API
        String flashAPI = null;
        // 接口返回结果
        String result = null;
        // 缩略图
        String flashThumbnailUrl = null;
        try {
            // 优酷
            Matcher youkuMatcher = this.youkuPatter.matcher(swfSrc);
            // 搜狐
            Matcher sohuMatcher = this.sohuPatter.matcher(swfSrc);
            if (youkuMatcher.find()) {
                result = HttpClient.get(String.format(this.youkuAPI,
                    youkuMatcher.group(1)));
                Matcher youkuPicMatcher = youkuPic.matcher(result);
                if (youkuPicMatcher.find()) {
                    flashThumbnailUrl = youkuPicMatcher.group(1).replace("\\",
                        "");
                }
            }
            // 搜狐
            else if (sohuMatcher.find()) {
                flashAPI = String.format(sohuapi, sohuMatcher.group(1));
                result = HttpClient.get(flashAPI);
                Matcher sohuPicMatch = this.sohuPic.matcher(result);
                if (sohuPicMatch.find()) {
                    flashThumbnailUrl = sohuPicMatch.group(Digit.ONE);
                }
            }

            if (!StringUtility.isNullOrEmpty(flashThumbnailUrl)) {
                if (HttpClient.getResponseCode(flashThumbnailUrl) != 200) {
                    return null;
                }
            }
            return flashThumbnailUrl;
        } catch (Exception e) {
            logger.error("flash thumbnail error", e);
            return null;
        }
    }

    /**
     * 获取播放形式的flash HTML
     *
     * @param flashUrl
     * @param size
     * @return
     */
    public String getPlayHtml(String flashUrl, String size) {
        // 只显示第一个 flash
        String thumbnail = JSUtility.decodeURIComponent(flashUrl.split("\\#")[1])
            + ".jpg";

        if (size.equals(SIZE.SMALL)) {
            thumbnail = thumbnail.replace(File.SIZE.BIG, File.SIZE.SMALL);
        }
        return String
            .format("<div class=\"flash\"><img onload=\"init_play(this,'%2$s');\" src=\"%1$s\"/></div>",
                thumbnail, flashUrl);
    }

    /**
     * 视频的html
     * <p>
     * 因为.jpg形式无法编码，使视频无法播放，故此处过滤掉
     * <p>
     * 上传 时使用
     *
     * @param flashUrl
     * @param thumbnailUrl
     * @return
     */

    public String getHtml(String flashUrl, String thumbnailUrl) {
        String videoHtml = String
            .format("<embed src=\"%1$s\" quality=\"high\" wmode=\"opaque\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\" width=\"500\" height=\"500\"/>",
                flashUrl
                    + "#"
                    + JSUtility.encodeURIComponent(thumbnailUrl
                    .substring(0,
                        thumbnailUrl.lastIndexOf('.'))));
        return videoHtml;
    }
}

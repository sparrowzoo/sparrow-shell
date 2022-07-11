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

package com.sparrow.constant;

import java.util.regex.Pattern;

public class Regex {

    /**
     * 常用匹配模式选项
     */
    public static final int OPTION = Pattern.MULTILINE
        | Pattern.CASE_INSENSITIVE | Pattern.CANON_EQ;

    /**
     * 破坏的HTML标签 br除外
     */
    public static final Pattern TAG_BREAK_HTML = Pattern.compile("<^br*?>", OPTION);
    public static final Pattern TAG_URL = Pattern.compile("href=([\"'])(.*?)\\1", OPTION);
    /**
     * 页面标题
     */
    public static final Pattern PAGE_TITLE = Pattern.compile("<title>([\\s\\S]*?)<\\/title>", OPTION);

    /**
     * 内网图片的url
     */
    public static final Pattern URL_INNER_IMAGE = Pattern.compile("http:\\/\\/img[0-9]+\\.$image_website\\.net\\/.*?", OPTION);
    /**
     * 站内图片格式image图片标签
     * <p/>
     * img.zhuaququ.com
     */
    public static final Pattern TAG_INNER_IMAGE = Pattern.compile("<img[\\s\\S]*?src=([\"\'])(http:\\/\\/img[0-9]+\\.$image_website\\.net\\/.*?)\\1[\\s\\S]*?\\/?>", OPTION);

    /**
     * 临时图片存储格式
     */
    public static final Pattern TAG_TEMP_IMAGE_FORMAT = Pattern.compile("<img src=\"%1$s\" id=\"tempImage%2$s\"/>", OPTION);
    /**
     * 临时image图片标签 temp.zhuaququ.com
     */
    public static final String TAG_TEMP_IMAGE = "<img[\\s\\S]*?src=([\"\'])(http:\\/\\/temp\\.$image_website\\.net\\/.*?)\\1[\\s\\S]*?\\/?>";
    /**
     * 第三方网站（抓取时使用）图片标签 所有图片格式
     */
    public static final String TAG_IMAGE = "<img[\\s\\S]*?src=([\"\'])(.*?)\\1[\\s\\S]*?\\/?>";

    /**
     * MARK DOWN 图片标签
     */
    public static final String TAG_MARKDOWN_IMAGE = "!\\[(.*)]\\((.*)\\)";
    /**
     * flashTag标签
     */
    public static final String TAG_FLASH = "<embed.*?src=([\"']?)(.*?)\\1\\s.*?>(<\\/embed>)?";
    /**
     * 回车换行
     */
    public static final String TAG_BR = "<br/?>+";
    /**
     * HTML空格
     */
    public static final String TAG_HTML_SPACE = "&nbsp;+";

    /**
     * 转义字符
     */
    public static final String TAG_HTML_ESCAPE = "&[A-Za-z]*?;";
    /**
     * 块标签
     * <p/>
     * div|p|ul|table|dl|h[1-6]
     */
    public static final String TAG_BLOCK = "</?div>|</?p>|<br/?>|</?dl>|</?table>|<hr/?>|</?h[1-6]>";

    /**
     * 带内容的块标签
     */
    public static final String TAG_BLOCK_WITH_CONTENT = "<(div|p|ul|table|dl|h[1-6])>(.*?)<\\/\\1>";

    public static final String IMG_URL_RULE = "^\\/([a-z0-9_]*)\\/";
    /**
     * 注释标记
     */
    public static final String ANNOTATION_HTML = "<!--[\\s\\S]*?-->";

    /**
     * 密码格式
     */
    public static final String PASSWORD = "^[\\w!@#$%\\^&\\*\\(\\)_]{6,20}$";

    public static final Pattern BAIDU_IMAGE_SEARCH = Pattern.compile(
        "var imgdata=([\\s\\S]*?)<\\/script>", Regex.OPTION);
}

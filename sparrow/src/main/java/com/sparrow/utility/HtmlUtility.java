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

import com.sparrow.constant.Regex;

import com.sparrow.protocol.constant.magic.Escaped;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.protocol.dto.SimpleItemDTO;

import java.util.ArrayList;
import java.util.List;

public class HtmlUtility {

    public static int getCountWithoutHtml(String sourceHTML) {
        // 过滤多余的回车
        sourceHTML = sourceHTML.replaceAll(Regex.TAG_BR, "<br/>");
        // 过滤多余的空格
        sourceHTML = sourceHTML.replaceAll(Regex.TAG_HTML_SPACE, "&nbsp;");
        // 字符数组
        char[] chars = sourceHTML.toCharArray();
        // 字符索引
        int index = 0;
        // 是否标签字符标记
        boolean isHTMLTag = false;
        // 当前字符
        char currentChar;
        int count = 0;
        while (index < chars.length) {
            currentChar = chars[index++];
            // HTML标签开始
            if (currentChar == '<') {
                // 说明以下内容是标签部分
                isHTMLTag = true;
            }
            // 标签部分
            if (isHTMLTag) {
                // HTML标签结束
                if (currentChar == '>') {
                    // 当前标签结束
                    isHTMLTag = false;
                }
                // 正文部分
            } else {
                // 当前字符不是空
                if (currentChar != '\n' && currentChar != '\r') {
                    count++;
                }
            }
        }
        return count;
    }

    public static String filterHTML(String sourceHTML) {
        String result = filterHTML(sourceHTML, "</p><p>");
        result = result.replaceAll("(<p>)+", "<p>");
        result = result.replaceAll("(<\\/p>)+", "</p>");
        result = result.replaceAll("(<p><\\/p>)*", "");
        result = result.replaceAll("(<\\/p><p>)+", "</p><p>");
        if (result.startsWith("</p><p>")) {
            result = result.substring("</p><p>".length());
        }
        if (result.endsWith("</p><p>")) {
            result = result.substring(0, result.length() - "</p><p>".length());
        }
        return result;
    }

    /**
     * 过滤HTML字符中的html标记 包括img标签 要过滤掉html的转义字符
     *
     * @param sourceHTML
     * @return
     */
    public static String filterHTML(String sourceHTML, String splitTag) {
        // 过滤多余的回车
        sourceHTML = sourceHTML.replaceAll(Regex.TAG_BR, " ");
        // 过滤转义字符
        sourceHTML = sourceHTML.replaceAll(Regex.TAG_HTML_ESCAPE, "");
        // 字符数组
        char[] chars = sourceHTML.toCharArray();
        // 字符索引
        int index = 0;
        // 是否标签字符标记
        boolean isHTMLTag = false;
        // 目录字符串
        StringBuilder desc = new StringBuilder();
        // 标签名
        StringBuffer tagName = null;
        // 标签队列
        List<String> tagQueue = new ArrayList<String>();
        // 当前字符
        char currentChar;
        // 段落标记
        while (index < chars.length) {
            currentChar = chars[index++];
            // HTML标签开始
            if (currentChar == '<') {
                // 说明以下内容是标签部分
                isHTMLTag = true;
                // 新标签开始
                tagName = new StringBuffer();
            }
            // 标签部分
            if (isHTMLTag) {
                tagName.append(currentChar);
                // HTML标签结束
                if (currentChar == '>') {
                    // 当前标签结束
                    isHTMLTag = false;
                    // 标签结束后加入标签队列
                    tagQueue.add(tagName.toString().trim());
                }
                // 正文部分
            } else {
                // 内容开始 检测标签队列
                if (tagQueue.size() > 0) {
                    // 如果多个标签紧接着出现，则只分一段
                    for (String tag : tagQueue) {
                        // 是否为块标签
                        if (isBlockTag(tag)) {
                            desc.append(splitTag);
                            break;
                        }
                    }
                    // 内容开始标签队列清空
                    tagQueue.clear();
                }
                // 当前字符不是空
                if (currentChar != '\n' && currentChar != '\r'
                    && currentChar != ' ' && currentChar != '\t') {
                    desc.append(currentChar);
                }
            }
        }

        return desc.toString();
    }

    /**
     * 是否块标签
     *
     * @param tagName
     * @return
     */
    public static boolean isBlockTag(String tagName) {
        return RegexUtility.matches(tagName, Regex.TAG_BLOCK);
    }

    /**
     * 防止XSS攻击 Cross Site Scripting
     *
     * @param html
     * @return
     */
    public static String encode(String html) {
        char[] array = html.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : array) {
            String s = c + Symbol.EMPTY;
            switch (c) {
                case '&':
                    s = Escaped.AND;
                    break;
                case '<':
                    s = Escaped.LESS_THEN;
                    break;
                case '>':
                    s = Escaped.GREAT_THEN;
                    break;
                case '"':
                    s = Escaped.DOUBLE_QUOTES;
                    break;
                //case ' ':
                //    s = "&nbsp;";
                //break;
                default:
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String decode(String html) {
        html = html.replace(Escaped.AND, Symbol.AND);
        html = html.replace(Escaped.LESS_THEN, Symbol.LESS_THEN);
        html = html.replace(Escaped.GREAT_THEN, Symbol.GREATER_THAN);
        html = html.replace(Escaped.DOUBLE_QUOTES, Symbol.DOUBLE_QUOTES);
        html = html.replace(Escaped.NO_BREAK_SPACE, Symbol.BLANK);
        return html;
    }

    public static String assembleHyperLink(SimpleItemDTO simpleItem, String openType) {
        String title = simpleItem.getTitle();
        if (!StringUtility.isNullOrEmpty(simpleItem.getCoverImage())) {
            title = "<img title='" + title + "' src='" + simpleItem.getCoverImage()
                + "'/>";
        }
        return String.format("<a href=\"%1$s\" target=\"%2$s\">%3$s</a>",
            simpleItem.getUrl() == null ? "" : simpleItem.getUrl().trim(), openType,
            title);
    }
}

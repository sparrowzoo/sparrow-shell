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

import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class JSUtility {
    /**
     * 获取js的安全脚本 '单引号 " 双引号
     *
     * @param str
     * @return
     */
    public static String getSafeJsString(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("'", "&apos;").replace("\"", "&quot;")
            .replace("\r\n", "<br/>").replace("\n", "<br/>");
    }

    /**
     * 支持js decodeURIComponent
     *
     * @param content
     * @return
     */
    public static String encodeURIComponent(String content) {
        try {
            if (StringUtility.isNullOrEmpty(content)) {
                return Symbol.EMPTY;
            }
            return URLEncoder.encode(content, Constant.CHARSET_UTF_8).replace(Symbol.ADD, "%20");
        } catch (Exception e) {
            return Symbol.EMPTY;
        }
    }

    /**
     * 支持js encodeURIComponent
     *
     * @param content
     * @return
     */
    public static String decodeURIComponent(String content) {
        try {
            return URLDecoder.decode(content, Constant.CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            return Symbol.EMPTY;
        }
    }

//    /**
//     * 通过js 设置Editor的编辑器内容 在后台设置以免在前端被html截断
//     *
//     * @param editorContent
//     * @param editor
//     * @return
//     */
//    public static String getEditorContent(String editor,
//                                          String editorContent, String attach) {
//        return String
//                .format("%1$s.initContent=function(){"
//                                + "%1$s.setEditorContent(decodeURIComponent(\"%2$s\"));};"
//                                + "%1$s.attach.setParentObject(%1$s);"
//                                + "%1$s.config.attach.uploadedJson=decodeURIComponent('%3$s');",
//                        editor, encodeURIComponent(editorContent),
//                        encodeURIComponent(attach));
//    }
}

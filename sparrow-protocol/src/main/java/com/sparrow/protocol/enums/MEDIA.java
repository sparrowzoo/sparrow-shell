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

package com.sparrow.protocol.enums;

import java.io.Serializable;

/**
 * @author harry
 */

public enum MEDIA implements Serializable {

    /**
     * 图片
     */
    NEWS("图文","isNews"),

    /**
     * 链接
     */
    LINK("链接","isLink"),

    /**
     * 文本
     */
    TEXT("文本","isText"),

    /**
     * 图片
     */
    IMAGE("图片","isPicture"),

    /**
     * flash
     */
    FLASH("视频","isVideo"),

    /**
     * 语音
     */
    VOICE("语音","isVoice"),

    /**
     * 视频
     */
    VIDEO("视频","isVideo"),

    /**
     * 音乐
     */
    MUSIC("音乐","isMusic");

    private String text;
    private String field;

    MEDIA(String text, String field) {
        this.text = text;
        this.field = field;
    }

    public String value() {
        return super.toString().toLowerCase();
    }

    @Override
    public String toString() {
        return this.text + "|" + this.field + "|" + super.toString();
    }

    public String getField() {
        return field;
    }

    public String getText() {
        return text;
    }
}

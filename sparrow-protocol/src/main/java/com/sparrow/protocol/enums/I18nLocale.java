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

import com.sparrow.protocol.EnumIdentityAccessor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 国际化语言枚举
 * - lcid       ：数值型编码（Windows LCID），如 zh-CN = 2052
 * - shortCode  ：语言简写，如 zh、en
 * - fullCode   ：BCP 47 完整标签，如 zh-CN、en-US
 * - zhName     ：中文名
 * - displayName：母语名
 */
public enum I18nLocale implements EnumIdentityAccessor {

    /* ---------- 中文 ---------- */
    ZH_CN(2052, "zh", "zh-CN", "简体中文", "简体中文", Locale.SIMPLIFIED_CHINESE),
    ZH_TW(1028, "zh", "zh-TW", "繁體中文（台灣）", "繁体中文（台湾）", Locale.TRADITIONAL_CHINESE),
    ZH_HK(3076, "zh", "zh-HK", "繁體中文（香港）", "繁体中文（香港）", new Locale("zh", "HK")),

    /* ---------- 英语 ---------- */
    EN_US(1033, "en", "en-US", "English (US)", "英语（美国）", Locale.US),
    EN_GB(2057, "en", "en-GB", "English (UK)", "英语（英国）", Locale.UK),
    EN_AU(3081, "en", "en-AU", "English (Australia)", "英语（澳大利亚）", new Locale("en", "AU")),
    EN_CA(4105, "en", "en-CA", "English (Canada)", "英语（加拿大）", Locale.CANADA),
    EN_IN(16393, "en", "en-IN", "English (India)", "英语（印度）", new Locale("en", "IN")),

    /* ---------- 日韩 ---------- */
    JA_JP(1041, "ja", "ja-JP", "日本語", "日语", Locale.JAPAN),
    KO_KR(1042, "ko", "ko-KR", "한국어", "韩语", Locale.KOREA),

    /* ---------- 欧洲主要语言 ---------- */
    FR_FR(1036, "fr", "fr-FR", "Français", "法语（法国）", Locale.FRANCE),
    FR_CA(3084, "fr", "fr-CA", "Français (Canada)", "法语（加拿大）", Locale.CANADA_FRENCH),
    DE_DE(1031, "de", "de-DE", "Deutsch", "德语", Locale.GERMANY),
    ES_ES(3082, "es", "es-ES", "Español", "西班牙语（西班牙）", new Locale("es", "ES")),
    ES_MX(2058, "es", "es-MX", "Español (México)", "西班牙语（墨西哥）", new Locale("es", "MX")),
    IT_IT(1040, "it", "it-IT", "Italiano", "意大利语", Locale.ITALY),
    PT_BR(1046, "pt", "pt-BR", "Português (Brasil)", "葡萄牙语（巴西）", new Locale("pt", "BR")),
    PT_PT(2070, "pt", "pt-PT", "Português", "葡萄牙语（葡萄牙）", new Locale("pt", "PT")),
    NL_NL(1043, "nl", "nl-NL", "Nederlands", "荷兰语", new Locale("nl", "NL")),
    PL_PL(1045, "pl", "pl-PL", "Polski", "波兰语", new Locale("pl", "PL")),
    RU_RU(1049, "ru", "ru-RU", "Русский", "俄语", new Locale("ru", "RU")),
    TR_TR(1055, "tr", "tr-TR", "Türkçe", "土耳其语", new Locale("tr", "TR")),
    SV_SE(1053, "sv", "sv-SE", "Svenska", "瑞典语", new Locale("sv", "SE")),
    NB_NO(1044, "nb", "nb-NO", "Norsk Bokmål", "挪威语", new Locale("nb", "NO")),
    DA_DK(1030, "da", "da-DK", "Dansk", "丹麦语", new Locale("da", "DK")),
    FI_FI(1035, "fi", "fi-FI", "Suomi", "芬兰语", new Locale("fi", "FI")),
    CS_CZ(1029, "cs", "cs-CZ", "Čeština", "捷克语", new Locale("cs", "CZ")),
    EL_GR(1032, "el", "el-GR", "Ελληνικά", "希腊语", new Locale("el", "GR")),
    UK_UA(1058, "uk", "uk-UA", "Українська", "乌克兰语", new Locale("uk", "UA")),

    /* ---------- 中东 / 非洲 ---------- */
    AR_SA(1025, "ar", "ar-SA", "العربية (السعودية)", "阿拉伯语（沙特）", new Locale("ar", "SA")),
    AR_AE(14337, "ar", "ar-AE", "العربية (الإمارات)", "阿拉伯语（阿联酋）", new Locale("ar", "AE")),
    HE_IL(1037, "he", "he-IL", "עברית", "希伯来语", new Locale("he", "IL")),

    /* ---------- 东南亚 / 南亚 ---------- */
    HI_IN(1081, "hi", "hi-IN", "हिन्दी", "印地语", new Locale("hi", "IN")),
    TH_TH(1054, "th", "th-TH", "ไทย", "泰语", new Locale("th", "TH")),
    VI_VN(1066, "vi", "vi-VN", "Tiếng Việt", "越南语", new Locale("vi", "VN")),
    ID_ID(1057, "id", "id-ID", "Bahasa Indonesia", "印度尼西亚语", new Locale("id", "ID")),
    MS_MY(1086, "ms", "ms-MY", "Bahasa Melayu", "马来语", new Locale("ms", "MY")),
    FIL_PH(1124, "fil", "fil-PH", "Filipino", "菲律宾语", new Locale("fil", "PH"));

    /**
     * 数值型编码（Windows LCID），如 zh-CN = 2052
     */
    private final int lcid;
    /**
     * 语言简写，如 zh、en
     */
    private final String shortCode;
    /**
     * BCP 47 完整标签，如 zh-CN、en-US
     */
    private final String fullCode;
    /**
     * 母语名
     */
    private final String locateName;
    /**
     * 中文名
     */
    private final String zhName;
    /**
     * Java Locale 对象
     */
    private final Locale locale;

    I18nLocale(int lcid, String shortCode, String fullCode,
               String displayName, String zhName, Locale locale) {
        this.lcid = lcid;
        this.shortCode = shortCode;
        this.fullCode = fullCode;
        this.locateName = displayName;
        this.zhName = zhName;
        this.locale = locale;
    }

    /* ---------------- Getter ---------------- */

    public int getLCID() {
        return lcid;
    }


    public Locale getLocale() {
        return locale;
    }

    /* ---------------- 查表 ---------------- */

    public static final I18nLocale DEFAULT = EN_US;

    private static final Map<Integer, I18nLocale> LCID_MAP = new HashMap<>();

    static {
        for (I18nLocale v : values()) {
            LCID_MAP.put(v.lcid, v);
        }
    }


    /**
     * 按数值编码查找
     */
    public static I18nLocale fromLcid(int lcid) {
        return LCID_MAP.get(lcid);
    }

    public static I18nLocale fromLcidOrDefault(int lcid) {
        I18nLocale v = LCID_MAP.get(lcid);
        return v != null ? v : DEFAULT;
    }

    @Override
    public Integer getIdentity() {
        return this.lcid;
    }

    @Override
    public String getDisplayName() {
        return this.fullCode + "-" + this.zhName + "-" + this.locateName;
    }
}

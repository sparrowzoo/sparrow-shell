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

import com.sparrow.constant.Config;
import com.sparrow.constant.ConfigKeyLanguage;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import com.sparrow.protocol.constant.magic.CharSymbol;
import com.sparrow.protocol.constant.magic.Symbol;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StringUtility {

    public static String newUuid() {
        return UUID.randomUUID().toString().replace(Symbol.HORIZON_LINE, Symbol.EMPTY);
    }

    /**
     * @param array
     * @param key
     * @return
     */
    public static boolean existInArray(Object[] array, Object key) {
        if (array == null || array.length == 0 || StringUtility.isNullOrEmpty(key)) {
            return false;
        }
        for (Object s : array) {
            if (s == null) {
                continue;
            }
            if (s.toString().trim().equalsIgnoreCase(key.toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean existInArray(String array, String key) {
        return existInArray(array.split(","), key);
    }

    /**
     * 拆分数组的关键字编码 例如:key1:value1,key2:value2 先对边界字进行编码,以防原码有出来边界字符而无法显示 该函数代码顺序不能变
     *
     * @param str 需要折分的字符串 格式如:key1:value1,key2:value2
     */
    public static String encodeSplitKey(String str, boolean onlyDot) {
        if (str.contains(Symbol.POUND_SIGN)) {
            str = str.replace(Symbol.POUND_SIGN, "#limit");
        }

        if (!onlyDot) {
            if (str.contains(Symbol.COLON)) {
                str = str.replace(Symbol.COLON, "#colon#");
            }
        }

        if (str.contains(Symbol.COMMA)) {
            str = str.replace(Symbol.COMMA, "#dot#");
        }
        return str;
    }

    /**
     * 对split拆分数组的关键字解码 解码过程与编码过程相反
     */
    public static String decodeSplitKey(String str, boolean onlyDot) {
        if (!onlyDot) {
            if (str.contains("#colon#")) {
                str = str.replace("#colon#", Symbol.COLON);
            }
        }
        if (str.contains("#dot#")) {
            str = str.replace("#dot#", Symbol.COMMA);
        }

        if (str.contains("#limit")) {
            str = str.replace("#limit", Symbol.POUND_SIGN);
        }
        return str;
    }

    /**
     * 按字节剪切字符串
     */
    public static String subStringByByte(String str, int len) {
        if (len <= -1) {
            return str;
        }
        return subStringByByte(str, len, "...");
    }

    public static String json(POJO pojo) {
        return JsonFactory.getProvider().toString(pojo);
    }

    /**
     * 按字节剪切字符串，超过长度显示elide
     */
    public static String subStringByByte(String str, int len, String elide) {
        if (str == null) {
            return Symbol.EMPTY;
        }
        int strLen = length(str);
        if (len >= strLen || len < 1) {
            return str;
        }
        char[] charArray = str.toCharArray();
        int length = 0;
        StringBuilder descBuilder = new StringBuilder();
        for (char c : charArray) {
            try {
                length += String.valueOf(c).getBytes(Constant.CHARSET_UTF_8).length;
            } catch (UnsupportedEncodingException ignore) {
            }
            if (length > len) {
                break;
            }
            descBuilder.append(c);
        }
        return descBuilder.toString() + elide.trim();
    }

    public static int length(String str) {
        if (isNullOrEmpty(str)) {
            return 0;
        }
        try {
            return str.getBytes(Constant.CHARSET_UTF_8).length;
        } catch (UnsupportedEncodingException ignore) {
            return 0;
        }
    }

    /**
     * 获取字符串中的img标签
     */
    public static String getImageUrlFromString(String str) {
        str = str.toLowerCase();
        int a = str.indexOf("<img");
        int b = str.substring(a).indexOf(">");
        String imageURL = str.substring(a, b);
        a = imageURL.indexOf("src=\"");
        b = imageURL.lastIndexOf("\"");
        imageURL = imageURL.substring(a + 5, b - a - 5).trim();
        return imageURL;
    }

    /**
     * 设置首字母大写
     */
    public static String setFirstByteUpperCase(String srcString) {
        if (srcString == null || srcString.length() == 0) {
            return Symbol.EMPTY;
        }
        char[] s = srcString.toCharArray();
        int firstCase = s[0];
        if (firstCase >= 'a' && firstCase <= 'z') {
            firstCase -= 32;
            s[0] = (char) firstCase;
        }
        return new String(s);
    }

    /**
     * 设置首字母小写
     */
    public static String setFirstByteLowerCase(String srcString) {
        if (srcString == null || srcString.length() == 0) {
            return Symbol.EMPTY;
        }
        char[] s = srcString.toCharArray();
        int firstCase = s[0];
        if (firstCase >= 'A' && firstCase <= 'Z') {
            firstCase += 32;
            s[0] = (char) firstCase;
        }
        return new String(s);
    }

    /**
     * 获取缩进字符
     */
    public static String getIndent(int indentCount) {
        return generateSomeCharacter(indentCount, Symbol.BLANK);
    }

    /**
     * 获取缩进字符不足用c代替
     */
    public static String generateSomeCharacter(int characterCount, String c) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < characterCount; i++) {
            sb.append(c == null ? Symbol.UNDERLINE : c);
        }
        return sb.toString();
    }

    /**
     * null或""为true 否则为false
     */
    public static boolean isNullOrEmpty(Object str) {
        return str == null || Symbol.EMPTY.equals(str.toString().trim());
    }

    /**
     * 获取在线QQ字符串
     */
    public static String getOnlineQQ(String qq) {
        return "<a target=blank href=\"http://wpa.qq.com/msgrd?V=1&Uin={0}&Exe=QQ&Site="
            + ConfigUtility.getLanguageValue(ConfigKeyLanguage.WEBSITE_NAME,
            "zh_cn")
            + "&Menu=No\"><img border=\"0\" src=\"http://wpa.qq.com/pa?p=1:"
            + qq + ":1\" alt=\"给我发消息\"></a>";
    }

    /**
     * 将HTML中的br转换成回车文本
     */
    public static String convertHtmlBrToEnterText(String html) {
        if (!html.contains("<br/>")) {
            return html;
        }
        return html.toLowerCase().replace("<br/>", Constant.ENTER_TEXT);
    }

    /**
     * 将回车文本转换成html中的br
     */
    public static String convertEnterTextToHtmlBr(String text) {
        if (!text.contains(Constant.ENTER_TEXT)) {
            return text;
        }
        return text.replace(Constant.ENTER_TEXT, "<br/>");
    }

    /**
     * 一定是双位数
     *
     * @param bytes byte[]
     * @return String
     */
    public static String bytes2HexString(byte[] bytes) {
        String ret = "";
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" byte[]{0x2B, 0x44, 0xEF,0xD9}
     *
     * @param src String
     * @return byte[]
     */
    public static byte[] hexString2Bytes(String src) {
        byte[] tmp = src.getBytes();
        byte[] ret = new byte[tmp.length / 2];
        for (int i = 0; i < tmp.length / 2; i++) {
            byte src0 = tmp[i * 2];
            byte src1 = tmp[i * 2 + 1];
            byte b0 = Byte.decode("0x" + new String(new byte[] {src0}));
            b0 = (byte) (b0 << 4);
            byte b1 = Byte.decode("0x" + new String(new byte[] {src1}));
            ret[i] = (byte) (b0 ^ b1);
        }
        return ret;
    }

    /**
     * 从数组array中排除exceptArray并拼接成数组 用于标签删除时的帖子标签更新
     */
    public static String join(Object[] array, char separator,
        Object[] exceptArray) {
        StringBuilder sb = new StringBuilder();
        for (Object object : array) {
            if (existInArray(exceptArray, object)) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(object);
        }
        return sb.toString();
    }

    public static String join(String separator, Object... array) {
        StringBuilder sb = new StringBuilder();
        for (Object object : array) {
            if (object == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(object);
        }
        return sb.toString();
    }

    public static String join(Map<Integer, String> map) {
        return join(map, Symbol.COMMA);
    }

    public static String join(Map<Integer, String> map, String joinChar) {
        StringBuilder sb = new StringBuilder();
        for (Integer key : map.keySet()) {
            if (sb.length() > 0) {
                sb.append(joinChar);
            }
            sb.append(map.get(key));
        }
        return sb.toString();
    }

    public static String join(Iterable<?> collection) {
        if (collection == null) {
            return Symbol.EMPTY;
        }
        return join(collection, Symbol.COMMA);
    }

    public static String join(Iterable<?> collection, String joinChar) {
        StringBuilder sb = new StringBuilder();
        for (Object object : collection) {
            if (object == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(joinChar);
            }
            sb.append(object.toString().trim());
        }
        return sb.toString();
    }

    public static String join(List<List<String>> collections, String outerJoin, String innerJoin) {
        StringBuilder sb = new StringBuilder();
        for (Collection<?> collection : collections) {
            if (sb.length() > 0) {
                sb.append(outerJoin);
            }
            sb.append(join(collection, innerJoin));
        }
        return sb.toString();
    }

    public static String decodeForGet(String text) {
        try {
            return new String(text.getBytes(Constant.CHARSET_ISO_8859_1), Constant.CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 驼峰格式变小写_分隔串
     */
    public static String humpToLower(String source) {
        return humpToLower(source, CharSymbol.UNDERLINE);
    }

    public static String humpToLower(String source, char split) {
        source = source.replaceAll("(?<=[a-z])(?=[A-Z])", String.valueOf(split));
        return source.toLowerCase();
    }

    public static String underlineToHump(String source) {
        return toHump(source, Symbol.UNDERLINE);
    }

    public static String toHump(String source, String split) {
        if (isNullOrEmpty(split)) {
            return setFirstByteLowerCase(source);
        }
        String[] underlineArray = source.split(split);
        StringBuilder stringBuilder = new StringBuilder();
        for (String underline : underlineArray) {
            stringBuilder.append(setFirstByteUpperCase(underline));
        }
        return setFirstByteLowerCase(stringBuilder.toString());
    }

    public static String leftPad(String s, char c, int maxLength) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = s.length(); i < maxLength; i++) {
            sb.insert(0, c);
        }
        return sb.toString();
    }

    public static InputStream inputStream(String s) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(s.getBytes(Constant.CHARSET_UTF_8));
    }

    public static String replace(String source, Map<String, String> rep) {
        if (isNullOrEmpty(source)) {
            return Symbol.EMPTY;
        }
        for (String key : rep.keySet()) {
            if (source.contains(key) && rep.containsKey(key) && rep.get(key) != null) {
                source = source.replace(key, rep.get(key));
            }
        }
        return source;
    }

    /**
     * 将字节数组转换为十六进制字符串
     */
    public static String byteToStr(byte[] byteArray) {
        String strDigest = Symbol.EMPTY;
        for (byte b : byteArray) {
            strDigest += byteToHexStr(b);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     */
    private static String byteToHexStr(byte mByte) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = digit[mByte & 0X0F];
        return new String(tempArr);
    }

    public static String wrap(String source, String wrap, String lineSplit) {
        if (isNullOrEmpty(source)) {
            return Symbol.EMPTY;
        }

        if (isNullOrEmpty(wrap)) {
            wrap = "<p>%1$s</p>";
        }

        if (isNullOrEmpty(lineSplit)) {
            lineSplit = Constant.ENTER_TEXT;
        }

        String[] lineArray = source.split(lineSplit);
        StringBuilder builder = new StringBuilder();
        for (String line : lineArray) {
            builder.append(String.format(wrap, line));
        }
        return builder.toString();
    }

    public static String wrap(String source, String wrap) {
        return wrap(source, wrap, Constant.ENTER_TEXT);
    }

    public static String wrap(String source) {
        return wrap(source, "<p>%1$s</p>", Constant.ENTER_TEXT);
    }

    public static String subString(String source, String c) {
        if (isNullOrEmpty(source)) {
            return Symbol.EMPTY;
        }
        if (source.contains(c)) {
            source = source.substring(0, source.indexOf(c));
        }
        return source;
    }

    public static boolean matchUrl(String source, String desc) {
        return matchUrl(source, desc, false);
    }

    public static boolean matchUrlWithParameter(String source, String desc) {
        return matchUrl(source, desc, true);
    }

    private static boolean matchUrl(String source, String target, boolean withParameter) {
        if (isNullOrEmpty(source) || isNullOrEmpty(target)) {
            return false;
        }
        if (source.equalsIgnoreCase(target)) {
            return true;
        }
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        if (source.startsWith(rootPath)) {
            source = source.substring(rootPath.length());
        }
        if (target.startsWith(rootPath)) {
            target = target.substring(rootPath.length());
        }
        source = subString(source, Symbol.POUND_SIGN);
        target = subString(target, Symbol.POUND_SIGN);
        if (!withParameter) {
            source = subString(source, Symbol.QUESTION_MARK);
            target = subString(target, Symbol.QUESTION_MARK);
        }

        String extension = ConfigUtility.getValue(Config.DEFAULT_PAGE_EXTENSION, Extension.JSP);
        if (source.endsWith(extension)) {
            source = source.replace(extension, Symbol.EMPTY);
        }

        if (target.endsWith(extension)) {
            target = target.replace(extension, Symbol.EMPTY);
        }

        if (source.startsWith(Symbol.SLASH)) {
            source = source.replace(Symbol.SLASH, Symbol.EMPTY);
        }
        if (target.startsWith(Symbol.SLASH)) {
            target = target.replace(Symbol.SLASH, Symbol.EMPTY);
        }
        return source.equals(target);
    }

    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String format(String format, Object... args) {
        if (format == null) {
            return Symbol.EMPTY;
        }

        if (!format.contains("{}") || args == null || args.length == 0) {
            return format;
        }

        for (Object arg : args) {
            format = format.replaceFirst("\\{\\}", String.valueOf(arg));
        }
        return format;
    }

    public static String printStackTrace(String msg, Throwable t) {
        PrintWriter pw = null;
        try {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            t.printStackTrace(pw);

            StringBuilder exceptionString = new StringBuilder();
            if (!StringUtility.isNullOrEmpty(msg)) {
                exceptionString.append(msg);
                exceptionString.append(Constant.ENTER_TEXT);
            }
            exceptionString.append(sw.toString());
            return exceptionString.toString();
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
    }

    public static int ignoreCaseIndexOf(String str, String subStr) {
        if (str == null || subStr == null) {
            return -1;
        }
        return str.toLowerCase().indexOf(subStr.toLowerCase());
    }

    public static String getDigit(String str, int start) {
        char c;
        StringBuilder digit = new StringBuilder(10);
        while (start < str.length() && Character.isDigit(c = str.charAt(start++))) {
            digit.append(c);
        }
        return digit.toString();
    }

    public static int getPrefixCount(String str, String prefix) {
        int count = 0;
        StringBuilder prefixBuilder = new StringBuilder();
        while (str.startsWith(prefixBuilder.append(prefix).toString())) {
            count++;
        }
        return count;
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>More specifically, this method returns {@code true} if the
     * {@code CharSequence} is not {@code null}, its length is greater than 0, and it contains at least one
     * non-whitespace character.
     * <p><pre class="code">
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater than 0, and it does
     * not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        return str != null && str.length() > 0 && containsText(str);
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}

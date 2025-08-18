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

import com.sparrow.core.TypeConverter;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.CharSymbol;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.web.WebConfigReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class StringUtility {
    public static <T> String[] getStringArray(Iterable<T> values) {
        List<String> valueList = new ArrayList<>();
        TypeConverter typeConverter = new TypeConverter(String.class);
        for (T value : values) {
            if (value == null) {
                valueList.add(Symbol.EMPTY);
                continue;
            }
            valueList.add(typeConverter.convert(value).toString());
        }
        String[] valueArray = new String[valueList.size()];
        valueList.toArray(valueArray);
        return valueArray;
    }

    public static String newUuid() {
        return UUID.randomUUID().toString().replace(Symbol.HORIZON_LINE, Symbol.EMPTY);
    }

    /**
     * @param array 数组
     * @param key   key
     * @return exist in array
     */
    public static boolean existInArray(Object[] array, Object key) {
        if (array == null || array.length == 0 || StringUtility.isNullOrEmpty(key)) {
            return false;
        }
        String trimKey = key.toString().trim();
        for (Object s : array) {
            if (s == null) {
                continue;
            }
            if (s.toString().trim().equalsIgnoreCase(trimKey)) {
                return true;
            }
        }
        return false;
    }

    public static boolean existInArray(String array, String key) {
        return existInArray(array.split(Symbol.COMMA), key);
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
            length += String.valueOf(c).getBytes(StandardCharsets.UTF_8).length;
            if (length > len) {
                break;
            }
            descBuilder.append(c);
        }
        return descBuilder + elide.trim();
    }

    public static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }

    public static int getMaxAllowLength(int maxAllowLength, String input) {
        int remarkLength = StringUtility.length(input);
        if (remarkLength > 0) {
            maxAllowLength = maxAllowLength - remarkLength;
        }
        return maxAllowLength;
    }

    public static int length(String str) {

        if (isNullOrEmpty(str)) {
            return 0;
        }
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            char a = str.charAt(i);
            if (isChineseChar(a)) {
                len += 2;
            } else {
                len += 1;
            }
        }
        return len;
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
     * 生成指定个字符，并以splitter 分隔
     */
    public static String generateCharacter(int characterCount, char c, char splitter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < characterCount; i++) {
            if (sb.length() > 0) {
                sb.append(splitter);
            }
            sb.append(c);
        }
        return sb.toString();
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

    public static InputStream inputStream(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
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

        WebConfigReader configReader = ApplicationContext.getContainer().getBean(WebConfigReader.class);
        String rootPath = configReader.getRootPath();
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

        String extension = configReader.getTemplateEngineSuffix();
        if (source.endsWith(extension)) {
            source = source.replace(extension, Symbol.EMPTY);
        }

        if (target.endsWith(extension)) {
            target = target.replace(extension, Symbol.EMPTY);
        }

        if (source.startsWith(Symbol.SLASH)) {
            source = source.replaceFirst(Symbol.SLASH, Symbol.EMPTY);
        }
        if (target.startsWith(Symbol.SLASH)) {
            target = target.replaceFirst(Symbol.SLASH, Symbol.EMPTY);
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

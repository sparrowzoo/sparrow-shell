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

import com.sparrow.cg.MethodAccessor;
import com.sparrow.container.Container;
import com.sparrow.core.Pair;
import com.sparrow.core.TypeConverter;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryStringParser {

    private static Logger logger = LoggerFactory.getLogger(QueryStringParser.class);

    /**
     * de serial.
     *
     * @param requestToken request token
     * @return map
     */
    public static Map<String, String> deserial(String requestToken) {
        Map<String, String> tokens = new TreeMap<String, String>();

        if (StringUtility.isNullOrEmpty(requestToken)) {
            return null;
        }

        requestToken = requestToken.trim();
        if (requestToken.startsWith(Symbol.QUESTION_MARK) || requestToken.startsWith(Symbol.AND)) {
            requestToken = requestToken.substring(1);
        }

        String[] requestArray = requestToken.split(Symbol.AND);
        for (String pair : requestArray) {
            Pair<String, String> p = Pair.split(pair, Symbol.EQUAL);
            tokens.put(p.getFirst(), p.getSecond());
        }
        return tokens;
    }

    public static String serial(Map<String, String> parameters) {
        return serial(parameters, true);
    }

    public static String serial(Map<String, String> parameters,
        boolean isEncode) {
        return serial(parameters, isEncode, null);
    }

    /**
     * serial.
     *
     * @param parameters    parameters
     * @param isEncode      is encode
     * @param exceptKeyList except key list
     * @return string
     */
    public static String serial(Map<String, String> parameters,
        boolean isEncode, List<String> exceptKeyList) {
        StringBuilder serialParameters = new StringBuilder();
        for (String key : parameters.keySet()) {
            String v = parameters.get(key);
            if (StringUtility.isNullOrEmpty(v)) {
                continue;
            }
            if (exceptKeyList != null) {
                if (exceptKeyList.contains(key)) {
                    continue;
                }
            }
            if (serialParameters.length() > 0) {
                serialParameters.append(Symbol.AND);
            }
            if (isEncode) {
                try {
                    serialParameters.append(key + Symbol.EQUAL
                        + URLEncoder.encode(v, Constant.CHARSET_UTF_8));
                } catch (UnsupportedEncodingException ignore) {
                    logger.error("serial error", ignore);
                }

            } else {
                serialParameters.append(key + Symbol.EQUAL + v);
            }
        }
        return serialParameters.toString();
    }

    /**
     * replace query string.
     *
     * @param queryString  query string
     * @param currentQuery current query
     * @return new query
     */
    public static String replace(String queryString, String currentQuery) {
        if (queryString == null) {
            queryString = Symbol.EMPTY;
        }
        if (StringUtility.isNullOrEmpty(currentQuery)) {
            if (!StringUtility.isNullOrEmpty(queryString)) {
                queryString = "?" + queryString;
            }
            return queryString;
        }
        Map<String, String> map = new HashMap<String, String>();

        if (!StringUtility.isNullOrEmpty(queryString)) {
            map = deserial(queryString);
        }

        Pair<String, String> pair = Pair.split(currentQuery, "=");
        if (map == null) {
            return Symbol.EMPTY;
        }
        if (map.containsKey(pair.getFirst())) {
            map.remove(pair.getFirst());
        }
        if (!StringUtility.isNullOrEmpty(pair.getSecond())) {
            map.put(pair.getFirst(), pair.getSecond());
        }
        return serial(map);
    }

    /**
     * get parameter.
     *
     * @param entity pojo
     */
    public static String getParameter(POJO entity) {
        Container container = ApplicationContext.getContainer();
        List<TypeConverter> fieldList = container.getFieldList(entity.getClass());
        MethodAccessor methodAccessor = container.getProxyBean(entity.getClass());
        StringBuilder sb = new StringBuilder();
        for (TypeConverter field : fieldList) {
            Object o = methodAccessor.get(entity, field.getName());
            if (StringUtility.isNullOrEmpty(o)) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(Symbol.DOLLAR);
            }
            try {
                sb.append(StringUtility.setFirstByteLowerCase(field.getName())
                    + Symbol.EQUAL
                    + URLEncoder.encode(String.valueOf(o), Constant.CHARSET_UTF_8));
            } catch (UnsupportedEncodingException ignore) {
                logger.error("get parameter error", ignore);
            }
        }
        return sb.toString();
    }
}

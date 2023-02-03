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

package com.sparrow.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderParser {
    private static final int OPTION = Pattern.MULTILINE
        | Pattern.CASE_INSENSITIVE | Pattern.CANON_EQ;

    private static Pattern p = Pattern
        .compile("(\\{[a-z][0-9a-zA-Z_-]*\\})", OPTION);

    public static String parse(String parameterWithPlaceHolder, PropertyAccessor propertyAccessor) {
        if (!parameterWithPlaceHolder.contains("{")) {
            return parameterWithPlaceHolder;
        }
        Matcher m = p.matcher(parameterWithPlaceHolder);
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                String placeHolder = m.group(i);
                String property = placeHolder.substring(1, placeHolder.length() - 1);
                parameterWithPlaceHolder = parameterWithPlaceHolder.replace(placeHolder, String.valueOf(propertyAccessor.getProperty(property)));
            }
        }
        return parameterWithPlaceHolder;
    }
}

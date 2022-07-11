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
import com.sparrow.constant.Regex;
import com.sparrow.core.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtility {
    /**
     * 只判断是否匹配 且不做后续处理 例如:判断电子邮件或手机号码是否匹配等
     *
     * @param source
     * @param regex
     * @return
     */
    public static boolean matches(String source, String regex) {
        if (Constant.REPLACE_MAP != null) {
            regex = StringUtility.replace(regex, Constant.REPLACE_MAP);
        }
        return Pattern.compile(regex, Regex.OPTION).matcher(source).matches();
    }

    public static String group(String source, String regex) {
        List<String> group = groups(source, regex);
        if (group != null && group.size() > 0) {
            return group.get(0);
        }
        return null;
    }

    public static List<String> groups(String source, String regex) {
        if (Constant.REPLACE_MAP != null) {
            regex = StringUtility.replace(regex, Constant.REPLACE_MAP);
        }
        Pattern p = Pattern
            .compile(regex, Regex.OPTION);
        Matcher m = p.matcher(source);
        List<String> groupList = null;
        if (!m.find()) {
            return null;
        }
        groupList = new ArrayList<String>();
        for (int i = 1; i <= m.groupCount(); i++) {
            groupList.add(m.group(i));
        }
        return groupList;
    }

    public static List<List<String>> multiGroups(String source, String regex) {
        if (Constant.REPLACE_MAP != null) {
            regex = StringUtility.replace(regex, Constant.REPLACE_MAP);
        }
        Pattern p = Pattern
            .compile(regex, Regex.OPTION);
        Matcher m = p.matcher(source);
        List<List<String>> multiGroupList = new ArrayList<List<String>>();
        while (m.find()) {
            List<String> groupList = new ArrayList<String>();
            for (int i = 1; i <= m.groupCount(); i++) {
                groupList.add(m.group(i));
            }
            multiGroupList.add(groupList);
        }
        return multiGroupList;
    }

    /**
     * @param actionKey thread-{thread_id}-{page_index}
     * @return thread-([a-z0-9]*)-([a-z0-9]*) -- thread_id -- page_index
     */
    public static Pair<String, List<String>> getActionRegex(String actionKey) {
        String configParameter = "(\\{[a-z0-9]*\\})";
        String digitalAndLetter = "([a-z0-9\\-]*)";
        Pattern p = Pattern
            .compile(configParameter, Regex.OPTION);
        Matcher m = p.matcher(actionKey);
        String urlRegex = actionKey;
        List<String> parameters = new ArrayList<String>();
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                String group = m.group(i);
                urlRegex = urlRegex.replace(group, digitalAndLetter);
                String parameter = group.substring(1, group.length() - 1);
                parameters.add(parameter);
            }
        }
        return Pair.create(urlRegex, parameters);
    }
}

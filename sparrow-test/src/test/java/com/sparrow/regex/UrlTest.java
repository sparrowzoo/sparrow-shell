package com.sparrow.regex;

import com.sparrow.constant.Regex;
import com.sparrow.utility.RegexUtility;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by harry on 2018/2/1.
 */
public class UrlTest {
    @Test
    public void testUrlMatch() {
        String regex = "(\\{[a-z0-9]*\\})";
        String anyChar = "([a-z0-9]*)";
        String actionKey = "thread-{threadId}-{pageIndex}";
        Pattern p = Pattern
                .compile(regex, Regex.OPTION_MULTILINE_CASE_INSENSITIVE);
        Matcher m = p.matcher(actionKey);


        String urlRegex = actionKey;
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                String group = m.group(i);
                urlRegex = urlRegex.replace(group, anyChar);
                System.out.println(group);
                String str = group.substring(1, group.length() - 1);
                System.out.println(str);
            }
        }
        System.out.println(urlRegex);

        String userUrl = "thread-10-100";
        List<List<String>> lists = RegexUtility.multiGroups(userUrl, urlRegex);
        for (List<String> list : lists) {
            for (String s : list) {
                System.out.println(s);
            }
        }
    }
}

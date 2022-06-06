package com.sparrow.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupTest {
    public static void main(String[] args) {

        Pattern p2 = Pattern.compile("([a-z]+)(\\d+)([a-z]+)(\\d)");
        Matcher m2 = p2.matcher("aa123b222d bb234cc789");
        while (m2.find()) {
            for (int i = 0; i < m2.groupCount(); i++) {
                System.out.println(m2.group(i));
            }
        }
    }
}

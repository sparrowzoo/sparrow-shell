package com.sparrow.jdk.date;

import com.sparrow.constant.DateTime;
import com.sparrow.core.Pair;
import com.sparrow.enums.DateTimeUnit;
import com.sparrow.utility.DateTimeUtility;

public class TimeSegmentTest {
    public static void main(String[] args) {
        Pair<Long, Long> pair = DateTimeUtility.getTimeSegment(DateTimeUnit.YEAR, System.currentTimeMillis());
        format(DateTimeUnit.YEAR,pair);
        pair = DateTimeUtility.getTimeSegment(DateTimeUnit.MONTH, System.currentTimeMillis());
        format(DateTimeUnit.MONTH,pair);
        pair = DateTimeUtility.getTimeSegment(DateTimeUnit.WEEK, System.currentTimeMillis());
        format(DateTimeUnit.WEEK,pair);
        pair = DateTimeUtility.getTimeSegment(DateTimeUnit.DAY, System.currentTimeMillis());
        format(DateTimeUnit.DAY,pair);
        pair = DateTimeUtility.getTimeSegment(DateTimeUnit.HOUR, System.currentTimeMillis());
        format(DateTimeUnit.HOUR,pair);
        pair = DateTimeUtility.getTimeSegment(DateTimeUnit.MINUTE, System.currentTimeMillis());
        format(DateTimeUnit.MINUTE,pair);
        pair = DateTimeUtility.getTimeSegment(DateTimeUnit.SECOND, System.currentTimeMillis());
        format(DateTimeUnit.SECOND, pair);
    }

    private static void format(DateTimeUnit unit,Pair<Long, Long> pair) {
        System.out.print(unit+"|"+DateTimeUtility.getFormatTime(pair.getFirst(), DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS_MS)+"|");

        System.out.println(DateTimeUtility.getFormatTime(pair.getSecond(), DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS_MS));
    }
}

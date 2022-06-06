package com.sparrow.jdk.date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by harry on 16/7/28.
 */
public class TimeZoneTest {
    public static void main(String[] args) throws FileNotFoundException, ParseException {

        String var1 = System.getProperty("java.home") + File.separator + "lib";
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(var1, "tzdb.dat")));


        /**
         *  public TzdbZoneRulesProvider() {
         *         try {
         *             String libDir = System.getProperty("java.home") + File.separator + "lib";
         *             try (DataInputStream dis = new DataInputStream(
         *                      new BufferedInputStream(new FileInputStream(
         *                          new File(libDir, "tzdb.dat"))))) {
         *                 load(dis);
         *             }
         *         } catch (Exception ex) {
         *             throw new ZoneRulesException("Unable to load TZDB time-zone rules", ex);
         *         }
         *     }
         */

        Date date = DateUtils.parseDateStrictly("2019-05-08 08:00:00", "yyyy-MM-dd HH:mm:ss");
        System.out.println(TimeZone.getDefault());
        System.out.println(date.getTime());
        date = DateUtils.parseDateStrictly("2019-05-08 08:00:00", "yyyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());
        //String[] availableIds= TimeZone.getAvailableIDs();

        TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(date);
        calendar.setTimeZone(tz);
        System.out.println(DateFormatUtils.format(calendar, "yyyy-MM-dd HH:mm:ss"));

        tz = TimeZone.getTimeZone("Asia/Shanghai");
        calendar.setTime(date);
        calendar.setTimeZone(tz);

        System.out.println(DateFormatUtils.format(calendar, "yyyy-MM-dd HH:mm:ss"));
    }
}

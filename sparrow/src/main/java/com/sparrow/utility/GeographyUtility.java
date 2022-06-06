package com.sparrow.utility;

/**
 * @author: zh_harry@163.com
 * @date: 2019-03-23 18:19
 * @description:
 */
public class GeographyUtility {
    
    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @param lat1 纬度
     * @param lng1 经度
     * @param lat2 目标纬度
     * @param lng2 目标经度
     * @return 单位千米
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }
}

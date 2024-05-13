package com.jsdc.zhtc.utils;

public class Point {
    private static final Double PI = Math.PI;

    private static final Double PK = 180 / PI;

    /**
     * @param lat_a a的经度
     * @param lng_a a的维度
     * @param lat_b b的经度
     * @param lng_b b的维度
     * @return 距离
     * @Description: 根据经纬度计算两点之间的距离
     * @author yangzhenlong
     */
    public static String getDistance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double t1 =
                Math.cos(lat_a / PK) * Math.cos(lng_a / PK) * Math.cos(lat_b / PK) * Math.cos(lng_b / PK);
        double t2 =
                Math.cos(lat_a / PK) * Math.sin(lng_a / PK) * Math.cos(lat_b / PK) * Math.sin(lng_b / PK);
        double t3 = Math.sin(lat_a / PK) * Math.sin(lat_b / PK);

        double tt = Math.acos(t1 + t2 + t3);
        String res = (6366000 * tt) + "";
        return res.substring(0, res.indexOf("."));
    }

    public static String getDistance2(double lat_a, double lat_b, double lng_a, double lng_b) {
        //System.out.println( lat_a + "--" + lat_b + "--" + lng_a + "--" + lng_b );
        double lon1 = (Math.PI / 180) * lat_a;
        double lat1 = (Math.PI / 180) * lat_b;

        double lon2 = (Math.PI / 180) * lng_a;
        double lat2 = (Math.PI / 180) * lng_b;
        // 地球半径
        double R = 6371;

        double distanceAdress = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        String juli = String.valueOf(distanceAdress);
        return juli;
    }

    /*Double x;
    Double y;
    public double getDistance(Point p){

        double r = Math.sqrt(p.x-x)*(p.x-x)+(p.y-y)*(p.y-y);
        return r;
    }*/
}

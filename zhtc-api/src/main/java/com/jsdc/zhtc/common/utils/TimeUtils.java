package com.jsdc.zhtc.common.utils;

import com.jsdc.core.base.Base;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * ClassName: TimeUtil
 * Description:
 * date: 2022/1/4 22:01
 *
 * @author zonglina
 */
public class TimeUtils {
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(Date starttime, Date endtime) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long time1 = starttime.getTime();
        long time2 = endtime.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        return day + "天" + hour + "小时" + min + "分";
    }

    //计算分钟
    public static Integer computeMinute(Date starttime, Date endtime) {
        return Math.toIntExact(ChronoUnit.MINUTES.between(Instant.ofEpochMilli(starttime.getTime()), Instant.ofEpochMilli(endtime.getTime())));
    }

    public static Integer computeSeconds(Date starttime, Date endtime) {
        return Math.toIntExact(ChronoUnit.SECONDS.between(Instant.ofEpochMilli(starttime.getTime()), Instant.ofEpochMilli(endtime.getTime())));
    }

    //根据分钟转小时
    public static String formatTime(Integer minuteStr) {
        String resultStr = "";
        if (null != minuteStr) {
            if (minuteStr != 0) {
                if (minuteStr / 60 == 0) {
                    resultStr = minuteStr % 60 + "分";
                } else {
                    if (minuteStr % 60 == 0) {
                        resultStr = minuteStr / 60 + "小时";
                    } else {
                        resultStr = (minuteStr / 60 + "小时" + minuteStr % 60 + "分");
                    }
                }
            } else {
                resultStr = "0分";
            }
        } else {
            resultStr = "0分";
        }
        return resultStr;
    }

    public static String minConvertDayHourMin(Integer min) {

        String html = "0分";

        if (min != null) {

            Integer m = (Integer) min;

            String format;

            Object[] array;

            Integer days = (int) (m / (60 * 24));

            Integer hours = (int) (m / (60) - days * 24);

            Integer minutes = (int) (m - hours * 60 - days * 24 * 60);

            if (days > 0) {
                format = "%1$,d天%2$,d时%3$,d分";

                array = new Object[]{days, hours, minutes};

            } else if (hours > 0) {

                format = "%1$,d时%2$,d分";

                array = new Object[]{hours, minutes};

            } else {

                format = "%1$,d分";

                array = new Object[]{minutes};

            }
            html = String.format(format, array);

        }

        return html;

    }

    /**
     * 车牌号验证
     *
     * @return
     */
    public static boolean carPlate(String car_no) {
        String PlateNumMatch = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";
        if (Base.notEmpty(car_no)) {
            return car_no.matches(PlateNumMatch);
        }
        return false;
    }
}

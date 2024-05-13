package com.jsdc.zhtc.utils;

import com.jsdc.zhtc.vo.order.PaymentVo;
import org.joda.time.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ChargingUtils
 * Description: 计费方法
 * date: 2022/1/12 15:01
 *
 * @author wh
 **/
public class ChargingUtils {


    /**
     * create by wp at 2022/2/14 13:39
     * description:
     *
     * @param xj        24小时限价
     * @param sfsd1     白天收费时段开始
     * @param sfsd2     白天收费时段结束
     * @param mftime    白天免费停车时长
     * @param wmftime   晚上免费停车时长
     * @param fd        白天分段限价
     * @param wfd       晚上分段限价
     * @param sfdw      白天收费单位
     * @param wsfdw     晚上收费单位
     * @param scjg1     白天分段计时
     * @param scmoney1  白天分段计时价格
     * @param wscjg1    晚上分段计时
     * @param wscmoney1 晚上分段计时价格
     * @param rcsj      驶入时间
     * @param csj       驶出时间
     * @param wsfsd1    晚上收费时段开始
     * @param wsfsd2    晚上收费时段结束
     * @return com.jsdc.zhtc.vo.order.PaymentVo
     */
    public static PaymentVo charging(Double xj, String sfsd1, String sfsd2, Integer mftime, Integer wmftime, Double fd, Double wfd, Integer sfdw, Integer wsfdw,
                                     Integer[] scjg1, Double[] scmoney1, Integer[] wscjg1, Double[] wscmoney1, String rcsj, String csj, String wsfsd1, String wsfsd2) {
        double zje = 0.0;
        double wzje = 0.0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //车辆进场时间
            DateTime dateTime = new DateTime(format.parse(rcsj));
            //车辆出场时间
            DateTime outTime = new DateTime(format.parse(csj));
            //计算是否满1天
            Period p = new Period(dateTime, outTime, PeriodType.days());
            int days = p.getDays();
            //停车总时长
            long zd = new Duration(dateTime, outTime).getStandardMinutes();
            //如果天数大于等于1直接24小时限额
            if (days >= 1 && xj != 0) {
                zje = days * xj;
                zd = zd - days * 24 * 60;
            } else if (days >= 1 && xj == 0) {
                zje += sumDays(scjg1, scmoney1, wscjg1, wscmoney1, days, fd, wfd, sfdw, wsfdw);
                zd = zd - days * 24 * 60;
            }
            //如果是整天，直接返回金额，不再进行后续计算
            if (0 == zd) {
                PaymentVo paymentVo = new PaymentVo();
                paymentVo.setDiscount_money("0");
                paymentVo.setPay_money(String.valueOf(zje));
                return paymentVo;
            }
            if (days > 0) {
                dateTime = dateTime.plusDays(days);
            }
            String time = dateTime.toString("yyyy-MM-dd") + "T";
            String time4 = outTime.toString("yyyy-MM-dd") + "T";
            String time2 = outTime.toString("yyyy-MM-dd").substring(0, 8) + String.valueOf(Integer.parseInt(outTime.toString("yyyy-MM-dd").substring(8)) - days) + "T";
            String time3 = outTime.toString("yyyy-MM-dd").substring(0, 8) + String.valueOf(Integer.parseInt(outTime.toString("yyyy-MM-dd").substring(8)) - days + 1) + "T";
            //判断是否白天入场
            boolean isday;
            Interval i;
            if (Integer.parseInt(sfsd2.substring(0, 2)) < Integer.parseInt(sfsd1.substring(0, 2))) {
                //白天跨天就判断是否在夜间

                i = new Interval(new DateTime(time + wsfsd1), new DateTime(time + wsfsd2));
                if (i.contains(dateTime)) {
                    isday = false;
                } else {
                    isday = true;
                }
            } else {
                i = new Interval(new DateTime(time + sfsd1), new DateTime(time + sfsd2));
                if (i.contains(dateTime)) {
                    isday = true;
                } else {
                    isday = false;
                }
            }
            long b;
            long wb;
            double sumMinutes;
            boolean flag = false;
            boolean wflag = false;
            Map<String, Double> map;
            if (isday) {

                //判断结束时间是否在夜间 ------------------------begin----------------------------------
                //先判断夜间时段是否跨天
                if (Integer.parseInt(wsfsd2.substring(0, 2)) < Integer.parseInt(wsfsd1.substring(0, 2))) {//跨天
                    Interval interval = new Interval(new DateTime(time4 + sfsd1), new DateTime(time4 + sfsd2));
                    //出场时间是夜间
                    if (!interval.contains(outTime)) {
                        wb = new Duration(new DateTime(time4 + sfsd2), outTime).getStandardMinutes();
                        if (wb < 0) {
                            wb = new Duration(new DateTime(time4 + sfsd2).plusDays(-1), outTime).getStandardMinutes();
                        }
                        b = zd - wb;
                    } else {//出场时间是白天
                        //两种情况: 1.白-白 2.白-夜-白
                        Interval interval1 = new Interval(dateTime, outTime);
                        if (interval1.contains(new DateTime(time + sfsd2))) { //白-夜-白
                            wb = new Duration(new DateTime(time + wsfsd1), new DateTime(time4 + wsfsd2)).getStandardMinutes();
                            b = zd - wb;
                        } else {
                            b = new Duration(dateTime, outTime).getStandardMinutes();
                            wb = 0;
                        }

                    }
                } else {//不跨天
                    Interval interval = new Interval(new DateTime(time4 + sfsd2), new DateTime(time4 + wsfsd2));
                    if (interval.contains(outTime)) {
                        wb = new Duration(new DateTime(time + sfsd2), outTime).getStandardMinutes();
                        b = zd - wb;
                    } else {//出场时间是白天
                        //两种情况: 1.白-白 2.白-夜-白
                        Interval interval1 = new Interval(dateTime, outTime);
                        if (interval1.contains(new DateTime(time4 + sfsd2))) { //白-夜-白
                            wb = new Duration(new DateTime(time + wsfsd1), new DateTime(time + wsfsd2)).getStandardMinutes();
                            b = zd - wb;
                        } else {
                            b = new Duration(dateTime, outTime).getStandardMinutes();
                            wb = 0;
                        }
                    }
                }
                //判断结束时间是否在夜间 --------------------------end----------------------------------
                map = sumM2(scjg1, scmoney1, zd > b ? b - mftime : zd - mftime, sfdw);
                zje += map.get("money");
                zje = fd < zje && fd != 0 ? fd : zje;
//            sumMinutes = map.get("sumMinutes");
                sumMinutes = zd - b;
                if (sumMinutes > 0) {
                    wzje += sumM2(wscjg1, wscmoney1, sumMinutes - wmftime, wsfdw).get("money");
                    wzje = wfd < wzje && wfd != 0 ? wfd : wzje;
                    wflag = true;
                }
                flag = true;
            } else {
                //判断结束时间是否在白天 ------------------------begin----------------------------------
                //先判断白天时段是否跨天
                if (Integer.parseInt(sfsd2.substring(0, 2)) < Integer.parseInt(sfsd1.substring(0, 2))) {//跨天
                    Interval interval = new Interval(new DateTime(time4 + wsfsd1), new DateTime(time4 + wsfsd2));
                    //出场时间是白天
                    if (!interval.contains(outTime)) {
                        b = new Duration(new DateTime(time4 + sfsd1), outTime).getStandardMinutes();
                        if (b < 0) {
                            b = new Duration(new DateTime(time4 + sfsd1).plusDays(-1), outTime).getStandardMinutes();
                        }
                        wb = zd - b;
                    } else {//出场时间是夜间
                        //两种情况 1.夜-夜 2.夜-白-夜
                        Interval interval1 = new Interval(dateTime, outTime);
                        if (interval1.contains(new DateTime(time + wsfsd2))) {//夜-白-夜
                            b = new Duration(new DateTime(time + sfsd1), new DateTime(time2 + sfsd2)).getStandardMinutes();
                            wb = zd - b;
                        } else { //夜-夜
                            wb = new Duration(dateTime, outTime).getStandardMinutes();
                            b = 0;
                        }
                    }
                } else {//不跨天
                    Interval interval = new Interval(new DateTime(time4 + sfsd1), new DateTime(time4 + sfsd2));
                    if (interval.contains(outTime)) {
                        b = new Duration(new DateTime(time4 + sfsd1), outTime).getStandardMinutes();
                        wb = zd - b;
                    } else {//出场时间是夜间
                        //两种情况 1.夜-夜 2.夜-白-夜
                        Interval interval1 = new Interval(dateTime, outTime);
                        if (interval1.contains(new DateTime(time4 + sfsd1)) || interval1.contains(new DateTime(time + sfsd1))) {//夜-白-夜
                            b = new Duration(new DateTime(time + sfsd1), new DateTime(time + sfsd2)).getStandardMinutes();
                            wb = zd - b;
                        } else { //夜-夜
                            wb = new Duration(dateTime, outTime).getStandardMinutes();
                            b = 0;
                        }

                    }
                }
                //判断结束时间是否在白天 --------------------------end----------------------------------
                map = sumM2(wscjg1, wscmoney1, zd > wb ? wb - mftime : zd - wmftime, wsfdw);
                wzje += map.get("money");
                wzje = wfd < wzje && wfd != 0 ? wfd : wzje;
//            sumMinutes = map.get("sumMinutes");
                sumMinutes = zd - wb;
                if (sumMinutes > 0) {
                    map = sumM2(scjg1, scmoney1, sumMinutes - mftime, sfdw);
                    zje += map.get("money");
                    zje = fd < zje && fd != 0 ? fd : zje;
                    flag = true;
                }
                wflag = true;
            }
            sumMinutes = map.get("sumMinutes");
            if (sumMinutes > 0) {
                if (flag && wflag && i.contains(dateTime)) {
                    zje += sumM2(scjg1, scmoney1, sumMinutes, sfdw).get("money");
                    zje = fd < zje && fd != 0 ? fd : zje;
                }
                if (flag && wflag && !i.contains(dateTime)) {
                    wzje += sumM2(wscjg1, wscmoney1, sumMinutes, wsfdw).get("money");
                    wzje = wfd < wzje && wfd != 0 ? wfd : wzje;
                }
            }
            BigDecimal money = new BigDecimal(String.valueOf((zje + wzje) > xj && xj != 0 ? xj : zje + wzje));
            PaymentVo paymentVo = new PaymentVo();
            paymentVo.setDiscount_money("0");
            if (money.compareTo(BigDecimal.ZERO) > 0) {
                paymentVo.setPay_money(money.toString());
            } else {
                paymentVo.setPay_money("0");
            }
            return paymentVo;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new PaymentVo();
    }

    public static double sumDays(Integer[] scjg1, Double[] scmoney1, Integer[] wscjg1, Double[] wscmoney1, Integer day, Double fd, Double wfd, Integer sfdw, Integer wsfdw) {
        double je = 0;
        double wje = 0;
        for (int i = 0; i < scmoney1.length; i++) {
            if (i == 0) {
                je += (scjg1[i + 1] / sfdw) == 0 ? 0 : (scjg1[i + 1] / sfdw) * scmoney1[i];
            } else {
                je += ((scjg1[i * 2 + 1] - scjg1[i * 2]) / sfdw) == 0 ? 0 : ((scjg1[i * 2 + 1] - scjg1[i * 2]) / sfdw) * scmoney1[i];
            }
        }
        if (fd > 0 && je > fd) {
            je = fd;
        }
        for (int i = 0; i < wscmoney1.length; i++) {
            if (i == 0) {
                wje += (wscjg1[i + 1] / wsfdw) == 0 ? 0 : (wscjg1[i + 1] / wsfdw) * wscmoney1[i];
            } else {
                wje += ((wscjg1[i * 2 + 1] - wscjg1[i * 2]) / wsfdw) == 0 ? 0 : ((wscjg1[i * 2 + 1] - wscjg1[i * 2]) / wsfdw) * wscmoney1[i];
            }
        }
        if (wfd > 0 && wje > wfd) {
            wje = wfd;
        }
        return (je + wje) * day;
    }

    public static Map<String, Double> sumM2(Integer[] times, Double[] x, double minutes, Integer sfdwnew) {
        double money = 0;
        double sumMinutes = 0;
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < x.length; i++) {
            if (i == 0 && minutes - times[i + 1] <= 0) {
                money = Math.ceil(minutes / sfdwnew) * x[i];
                map.put("money", money);
                map.put("sumMinutes", sumMinutes > 0 ? sumMinutes : 0);
                return map;
            } else {
                sumMinutes = i == 0 ? minutes - (times[i + 1]) : sumMinutes - (times[i * 2 + 1] - times[i * 2]);
                if (sumMinutes > 0) {
                    money += i == 0 ? Math.ceil(times[i + 1] / sfdwnew) * x[i] : Math.ceil((times[i * 2 + 1] - times[i * 2]) / sfdwnew) * x[i];
                } else if (sumMinutes <= 0) {
                    money += Math.ceil((minutes - times[i * 2]) / sfdwnew) * x[i];
                    map.put("money", money);
                    map.put("sumMinutes", sumMinutes > 0 ? sumMinutes : 0);
                    return map;
                }
            }
        }
        map.put("money", money);
        map.put("sumMinutes", sumMinutes > 0 ? sumMinutes : 0);
        return map;
    }
}

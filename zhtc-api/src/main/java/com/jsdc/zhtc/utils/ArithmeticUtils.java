package com.jsdc.zhtc.utils;

import com.jsdc.zhtc.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 类 名: 数字计算等操作
 * 描 述: ArithmeticUtils
 * 作 者: lw
 * 创 建：2021/9/15 11:27
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
public class ArithmeticUtils {

    /**
     * 加法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static BigDecimal add(Object num1, Object num2) {
        return new BigDecimal(num1.toString()).add(new BigDecimal(num2.toString()));
    }

    /**
     * 减法
     *
     * @param num1 被减数
     * @param num2 减数
     * @return
     */
    public static BigDecimal subtract(Object num1, Object num2) {
        return new BigDecimal(num1.toString()).subtract(new BigDecimal(num2.toString()));
    }

    /**
     * 乘法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static BigDecimal multiply(Object num1, Object num2) {
        return new BigDecimal(num1.toString()).multiply(new BigDecimal(num2.toString()));
    }

    /**
     * 绝对值
     *
     * @param num
     * @return
     */
    public static BigDecimal abs(Object num) {
        return new BigDecimal(num.toString()).abs();
    }


    /**
     * 除法
     *
     * @param num1   被除数
     * @param num2   除数
     * @param figure 保留位数
     * @param genre  小数取舍类型（可参考下方 注解1 ）
     * @return
     */
    public static BigDecimal divide(Object num1, Object num2, Integer figure, Integer genre) {
        return new BigDecimal(num1.toString()).divide(new BigDecimal(num2.toString()), figure, genre);
    }

    /**
     * 判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNum(String str) {
        try {
            BigDecimal num = new BigDecimal(str);
        } catch (Exception e) {//抛异常必定不是数字
            return false;
        }
        return true;
    }


    /**
     * 获取秒数
     *
     * @param hour 小时数
     * @return
     */
    public static Long getSecond(double hour) {
        //每小时秒数
        String hourSeconds = "3600";

        BigDecimal multiply = multiply(new BigDecimal(hour + ""), new BigDecimal(hourSeconds));
        return multiply.longValue();
    }


    /**
     * 判断两个数字相等
     *
     * @param num1
     * @param num2
     * @return
     */
    public static boolean compareTo1(Object num1, Object num2) {
        BigDecimal a = new BigDecimal(num1.toString());
        BigDecimal b = new BigDecimal(num2.toString());
        if (a.compareTo(b) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数字1 是否大于数字二
     *
     * @param num1
     * @param num2
     * @return
     */
    public static boolean compareTo2(Object num1, Object num2) {
        BigDecimal a = new BigDecimal(num1.toString());
        BigDecimal b = new BigDecimal(num2.toString());
        if (a.compareTo(b) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 描 述： TODO( 保留小数标准 )
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param num1
     * @param extent 保留位数
     * @param type   取舍类型
     * @return {@link BigDecimal}
     */
    public static BigDecimal setScale1(Object num1, Integer extent, Integer type) {
        if (StringUtils.isNotBlank(num1.toString())) {
            return new BigDecimal(num1.toString()).setScale(extent, type);
        } else {
            return new BigDecimal("0.0");
        }

    }

    /**
     * 描 述： TODO(减天数)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param date
     * @param days
     * @return {@link Date}
     */
    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }

    /**
     * 描 述： TODO(增加月)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param date
     * @param month
     * @return {@link Date}
     */
    public static Date subMonth(Date date, int month) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    //计算比例
    public static String getBl(String sumAmount, String sumAmount2) {
        String bl = "↑0%";
        if (!sumAmount2.equals("0")) {
            if (!compareTo1(sumAmount, sumAmount2)) {
                if (compareTo1(sumAmount, sumAmount2)) {
                    // 上升
                    BigDecimal subtract1 = ArithmeticUtils.subtract(sumAmount, sumAmount2);
                    BigDecimal divide = ArithmeticUtils.divide(subtract1, sumAmount2, 4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal multiply = ArithmeticUtils.multiply(divide, new BigDecimal("100"));
                    multiply = ArithmeticUtils.setScale1(multiply, 2, BigDecimal.ROUND_HALF_UP);
                    bl = "↑" + multiply.toString() + "%";
                } else {
                    // 下降
                    BigDecimal subtract1 = ArithmeticUtils.subtract(sumAmount2, sumAmount);
                    BigDecimal divide = ArithmeticUtils.divide(subtract1, sumAmount2, 4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal multiply = ArithmeticUtils.multiply(divide, new BigDecimal("100"));
                    multiply = ArithmeticUtils.setScale1(multiply, 2, BigDecimal.ROUND_HALF_UP);
                    bl = "↓" + multiply.toString() + "%";
                }
            }
        } else {
            bl = "--";
        }

        return bl;
    }


    /**
     * 注解1
     * 八种舍入模式解释如下
     * 1、ROUND_UP
     *
     * 舍入远离零的舍入模式。
     *
     * 在丢弃非零部分之前始终增加数字(始终对非零舍弃部分前面的数字加1)。
     *
     * 注意，此舍入模式始终不会减少计算值的大小。
     *
     * 2、ROUND_DOWN
     *
     * 接近零的舍入模式。
     *
     * 在丢弃某部分之前始终不增加数字(从不对舍弃部分前面的数字加1，即截短)。
     *
     * 注意，此舍入模式始终不会增加计算值的大小。
     *
     * 3、ROUND_CEILING
     *
     * 接近正无穷大的舍入模式。
     *
     * 如果 BigDecimal 为正，则舍入行为与 ROUND_UP 相同;
     *
     * 如果为负，则舍入行为与 ROUND_DOWN 相同。
     *
     * 注意，此舍入模式始终不会减少计算值。
     *
     * 4、ROUND_FLOOR
     *
     * 接近负无穷大的舍入模式。
     *
     * 如果 BigDecimal 为正，则舍入行为与 ROUND_DOWN 相同;
     *
     * 如果为负，则舍入行为与 ROUND_UP 相同。
     *
     * 注意，此舍入模式始终不会增加计算值。
     *
     * 5、ROUND_HALF_UP
     *
     * 向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。
     *
     * 如果舍弃部分 >= 0.5，则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同。
     *
     * 注意，这是我们大多数人在小学时就学过的舍入模式(四舍五入)。
     *
     * 6、ROUND_HALF_DOWN
     *
     * 向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为上舍入的舍入模式。
     *
     * 如果舍弃部分 > 0.5，则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同(五舍六入)。
     *
     * 7、ROUND_HALF_EVEN
     *
     * 向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
     *
     * 如果舍弃部分左边的数字为奇数，则舍入行为与 ROUND_HALF_UP 相同;
     *
     * 如果为偶数，则舍入行为与 ROUND_HALF_DOWN 相同。
     *
     * 注意，在重复进行一系列计算时，此舍入模式可以将累加错误减到最小。
     *
     * 此舍入模式也称为“银行家舍入法”，主要在美国使用。四舍六入，五分两种情况。
     *
     * 如果前一位为奇数，则入位，否则舍去。
     *
     * 以下例子为保留小数点1位，那么这种舍入方式下的结果。
     *
     * 1.15>1.2 1.25>1.2
     *
     * 8、ROUND_UNNECESSARY
     *
     * 断言请求的操作具有精确的结果，因此不需要舍入。
     *
     * 如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。
     */


}

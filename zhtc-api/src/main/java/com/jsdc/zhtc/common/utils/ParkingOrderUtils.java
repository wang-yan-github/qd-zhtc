package com.jsdc.zhtc.common.utils;

import cn.hutool.core.util.IdUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName: OrderUtils
 * Description:停车场订单规则
 * date: 2022/1/6 8:38
 * st+年月日+P(R)+停车场id+00001
 *
 * @author zonglina
 */
public class ParkingOrderUtils {

//    private static String flag = "";
//    private static int startIndex = 100000;


    /**
     * 停车场订单号生成规则
     */
    public static String getParkingIdByUUId(String type, Integer id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentStr = sdf.format(new Date());
        return type + IdUtil.getSnowflakeNextId();

//        if ("".equals(flag) || !currentStr.equals(flag)) {
//            startIndex = 100000;
//            flag = currentStr;
//        }
//        startIndex = startIndex + 1;
//        return "ST" + currentStr + type + id + String.valueOf(startIndex);
    }

    /**
     * 上传市交控的流水号（场库编号+ 入口编号+ 时间戳 yyyy MMdd HHmmsssss ）
     */
    public static String createUid(String park_code, String serialNo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = sdf.format(new Date());
        return park_code + serialNo + time;
    }

    public static void main(String[] args) {
        System.out.println(ParkingOrderUtils.createUid("10042606", "QFS8223061001401"));
    }
}

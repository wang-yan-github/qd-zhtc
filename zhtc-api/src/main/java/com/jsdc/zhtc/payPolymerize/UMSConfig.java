package com.jsdc.zhtc.payPolymerize;

import lombok.experimental.UtilityClass;

/**
 * @projectName: zhtc
 * @className: YlConfig
 * @author: wp
 * @description:
 * @date: 2023/5/20 14:34
 */
@UtilityClass
public class UMSConfig {

    public String APPID = "8a81c1be831e628801881d4c22bf4e4d";

    public String APPKEY = "1434d3b184624fadaa30f331c07edac7";

    public String MID = "89832536513127K";

    public String TID = "X2S0Q7J4";

    public String TOKEN = "";

    public String REDIS_TOKEN = "accesstoken";
    public String REDIS_QRCODE_URL = "accesstoken";

    //待支付二维码支付回调
    //public String NOTIFY_URL = "http://tctest.aldwxa.top/api/ums/notify";
    public String NOTIFY_URL = "https://zhtc.aldwxa.top/api/ums/notify";
    //在停二维码支付回调
    //public String NOTIFY_STOP_URL = "http://tctest.aldwxa.top/api/ums/stopNotify";
    public String NOTIFY_STOP_URL = "https://zhtc.aldwxa.top/api/ums/stopNotify";
    /**
     * 签名算法
     */
    public String SIGN_METHOD = "SHA256";

    /**
     * 订单支付状态
     */
    public static String BILL_STATUS_PAID = "PAID";
    public static String BILL_STATUS_UNPAID = "UNPAID";
    public static String BILL_STATUS_REFUND = "REFUND";
    public static String BILL_STATUS_CLOSED = "CLOSED";
    public static String BILL_STATUS_UNKNOWN = "UNKNOWN";

    /**
     * 支付通知接口响应
     */
    public static String NOTIFY_RESPONSE_SUCCESS = "SUCCESS";
    public static String NOTIFY_RESPONSE_FAILED = "FAILED";

    /**
     * 生产环境地址
     */
    public String HOST_ONLINE_BASE = "https://api-mop.chinaums.com/v1";
    /**
     * 测试环境地址
     */
    public String HOST_TEST_BASE = "https://test-api-open.chinaums.com/v1";

    //获取TOKEN
    public String HOST_TOKEN = "/token/access";
    //二维码获取
    public String HOST_GETQRCODE = "/netpay/bills/get-qrcode";
    //二维码更新
    public String HOST_UPDATE_QRCODE = "/netpay/bills/update-qrcode";
    //二维码关闭
    public String HOST_CLOSE_QRCODE = "/netpay/bills/close-qrcode";
    //账单查询
    public String HOST_QUERY_BILL = "/netpay/bills/query";
    //退款
    public String HOST_REFUND = "/netpay/bills/refund";
    //查询二维码静态信息
    public String HOST_QUERY_QRCODE_INFO = "/netpay/bills/query-qrcode-info";


}

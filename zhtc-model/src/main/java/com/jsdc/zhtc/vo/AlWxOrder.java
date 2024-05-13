package com.jsdc.zhtc.vo;

import com.jsdc.zhtc.vo.ticket.AlWxPayInfo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 类 名: 阿里微信订单信息
 * 描 述: AlWxOrder
 * 作 者: lw
 * 创 建：2021/8/25 15:18
 * 版 本：v1.0.0
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class AlWxOrder {

    /**
     * 付款
     */
    //商户订单号
    private String outTradeNo = "";
    //微信用户本平台唯一id(openid)
    private String openid = "oPfPf5TBcdsmldkjek4-HYV3rDYk";
    //支付场景
    private String scene = "bar_code";
    //支付授权码(条形码)
    private String authCode = "";
    //订单标题（商品名称等）
    private String subject = "tcfy";
    //商户门店编号
    private String storeId = "2088621956329861";
    //订单相对超时时间
    private String timeoutExpress = "50m";
    //订单总金额
    private String totalAmount = "1";
    //设备号
    private String deviceInfo = "1000";
    //用户ip地址
    private String ip = "";
    //回调地址
    private String url = "https://zhtc.aldwxa.top/pos"; // -authtoken=a3c60ae40ac6acfb

    /**
     * 退款
     */
    //支付宝、微信交易号(退款用)
    private String trade_no;
    //退款金额
    private String refund_amount;
    //退款原因
    private String refund_reason;
    //退款请求号
    private String out_request_no;

    //操作状态码
    private AlWxPayInfo alWxPayInfo = new AlWxPayInfo();


    /**
     * 描 述： TODO(生成支付订单号)
     * 作 者： lw
     * 历 史： (版本) 作者 时间 注释
     *
     * @param code 固定编码 可为空
     * @param type 生成编码类型 1支付  2退款
     * @return {@link java.lang.String}
     */
    public void createOutTradeNo(String code, int type) {
        String orderNo = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String formatTime = sdf.format(new Date());
        //生成随机数
        String symbols = "0123456789";
        Random random = new SecureRandom();
        char[] nonceChars = new char[10];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = symbols.charAt(random.nextInt(symbols.length()));
        }
        String randomStr = new String(nonceChars);

        //判断是否有前缀
        if (StringUtils.isNotBlank(code))
            orderNo = code;
        if (type == 1) {
            this.outTradeNo = orderNo + formatTime + randomStr;
        } else {
            this.out_request_no = orderNo + formatTime + randomStr;
        }
        //return this.outTradeNo;
    }


    /**
     * 连接超时时间
     */
    public static Integer connectionTimeout;

    @Value("${https.connectionTimeout}")
    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * 连接超时时间
     */
    public static Integer readTimeout;

    @Value("${https.readTimeout}")
    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }


}

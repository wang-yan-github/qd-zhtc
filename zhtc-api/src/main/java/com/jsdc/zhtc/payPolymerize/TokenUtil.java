package com.jsdc.zhtc.payPolymerize;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.common.utils.UUID;
import com.jsdc.zhtc.payPolymerize.dto.RefundDto;
import com.jsdc.zhtc.payPolymerize.dto.RequestQrcode;
import com.jsdc.zhtc.utils.SecureUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @projectName: zhtc
 * @className: TokenUtil
 * @author: wp
 * @description:
 * @date: 2023/5/20 14:40
 */
@Component
public class TokenUtil {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取TOKEN
     * 注意以下几点：
     * 1. 同一个APPID下，AccessToken最大有效个数为10个。
     * 2. AccessToken有效期为1小时
     * 3. 不要频繁获取AccessToken,特别是不要每次调用接口都获取AccessToken
     * 结果示例：
     * {"errCode":"0000","errInfo":"正常","accessToken":"30fabba831a447699f5a1fc056dfd4ee","expiresIn":3600}
     *
     * @return
     */
    public Token getToken() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStamp = format.format(new Date());
        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        String signature = SecureUtil.sha256X16Str(UMSConfig.APPID + timeStamp + nonce + UMSConfig.APPKEY, "UTF-8");
        JSONObject body = new JSONObject();
        body.put("appId", UMSConfig.APPID);
        body.put("timestamp", timeStamp);
        body.put("nonce", nonce);
        body.put("signMethod", UMSConfig.SIGN_METHOD);
        body.put("signature", signature);
        String result = HttpRequest.post(UMSConfig.HOST_ONLINE_BASE + UMSConfig.HOST_TOKEN)
                .body(body.toJSONString())
                .execute()
                .body();
        Token token = JSON.parseObject(result, Token.class);
        if (StringUtils.equals("0000", token.getErrCode())) {
            UMSConfig.TOKEN = token.getAccessToken();
        }
        System.out.println(result);
        return token;
    }

    /**
     * 获取二维码
     *
     * @return
     */
    public String getQrcode(String accessToken, Integer parkId, Integer channelId) {
        //String accessToken = redisTemplate.opsForValue().get(UMSConfig.REDIS_TOKEN).toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String requestTimeStamp = format.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        JSONObject srcReserve = new JSONObject();
        srcReserve.put("parkId", parkId);
        srcReserve.put("channelId", channelId);
        srcReserve.put("parkType", "1");
        RequestQrcode requestQrcode = RequestQrcode.builder()
                .msgId(uuid)
                .requestTimeStamp(requestTimeStamp)
                .srcReserve(srcReserve.toJSONString())
                .mid(UMSConfig.MID)
                .tid(UMSConfig.TID)
                .instMid("QRPAYDEFAULT")
                .notifyUrl(UMSConfig.NOTIFY_URL)
                .totalAmount(1)
                .build();
        String req = JSON.toJSONString(requestQrcode);
        String result = HttpRequest
                .post(UMSConfig.HOST_ONLINE_BASE + UMSConfig.HOST_GETQRCODE)
                .header("Authorization", accessToken)
                .body(req)
                .execute()
                .body();
        System.out.println(result);
        return result;
    }

    /**
     * 获取二维码
     *
     * @return
     */
    public String getAmountQrcode(String accessToken, Integer parkId, String channelId, Integer totalAmount, String parkOrderId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String requestTimeStamp = format.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String billNo = "";
        billNo += "34ZZ";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        billNo += format1.format(new Date());
        billNo += UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
        JSONObject srcReserve = new JSONObject();
        srcReserve.put("parkOrderId", parkOrderId);
        srcReserve.put("parkType", "1");
        RequestQrcode requestQrcode = RequestQrcode.builder()
                .msgId(uuid)
                .requestTimeStamp(requestTimeStamp)
                .billNo(billNo)
                .billDate(DateUtil.formatDate(new Date()))
                .srcReserve(srcReserve.toJSONString())
                .mid(UMSConfig.MID)
                .tid(UMSConfig.TID)
                .instMid("QRPAYDEFAULT")
                .notifyUrl(UMSConfig.NOTIFY_URL)
                .totalAmount(totalAmount)
                .build();
        String req = JSON.toJSONString(requestQrcode);
        String result = HttpRequest
                .post(UMSConfig.HOST_ONLINE_BASE + UMSConfig.HOST_GETQRCODE)
                .header("Authorization", accessToken)
                .body(req)
                .execute()
                .body();
        System.out.println(result);
        return result;
    }

    /**
     * 获取路测二维码
     *
     * @param accessToken
     * @param totalAmount
     * @param roadOrderId
     * @return
     */
    public String getRoadQrcode(String accessToken, Integer totalAmount, String roadOrderId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String requestTimeStamp = format.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        JSONObject srcReserve = new JSONObject();
        srcReserve.put("roadOrderId", roadOrderId);
        srcReserve.put("parkType", "0");
        String billNo = "";
        billNo += "34ZZ";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        billNo += format1.format(new Date());
        billNo += UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
        RequestQrcode requestQrcode = RequestQrcode.builder()
                .msgId(uuid)
                .requestTimeStamp(requestTimeStamp)
                .billNo(billNo)
                .billDate(DateUtil.formatDate(new Date()))
                .srcReserve(srcReserve.toJSONString())
                .mid(UMSConfig.MID)
                .tid(UMSConfig.TID)
                .instMid("QRPAYDEFAULT")
                .notifyUrl(UMSConfig.NOTIFY_URL)
                .totalAmount(totalAmount)
                .build();
        String req = JSON.toJSONString(requestQrcode);
        String result = HttpRequest
                .post(UMSConfig.HOST_ONLINE_BASE + UMSConfig.HOST_GETQRCODE)
                .header("Authorization", accessToken)
                .body(req)
                .execute()
                .body();
        System.out.println(result);
        return result;
    }

    /**
     * 获取二维码
     * 该接口针对停车场内部张贴的二维码扫码支付，因此该二维码直接绑定到，仅作在停车辆支付
     *
     * @param accessToken
     * @param orderId
     * @param totalAmount
     * @return
     */
    public String getStopAmountQrcode(String accessToken, Integer orderId, Integer totalAmount) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String requestTimeStamp = format.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String billNo = "";
        billNo += "34ZZ";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        billNo += format1.format(new Date());
        billNo += UUID.randomUUID().toString().replaceAll("-", "").substring(0, 7);
        JSONObject srcReserve = new JSONObject();
        srcReserve.put("orderId", orderId);
        RequestQrcode requestQrcode = RequestQrcode.builder()
                .msgId(uuid)
                .requestTimeStamp(requestTimeStamp)
                .srcReserve(srcReserve.toJSONString())
                .mid(UMSConfig.MID)
                .tid(UMSConfig.TID)
                .billNo(billNo)
                .billDate(format2.format(new Date()))
                .instMid("QRPAYDEFAULT")
                .notifyUrl(UMSConfig.NOTIFY_STOP_URL)
                .totalAmount(totalAmount)
                .build();
        String req = JSON.toJSONString(requestQrcode);
        String result = HttpRequest
                .post(UMSConfig.HOST_ONLINE_BASE + UMSConfig.HOST_GETQRCODE)
                .header("Authorization", accessToken)
                .body(req)
                .execute()
                .body();
        System.out.println(result);
        return result;
    }

    /**
     * 关闭二维码
     *
     * @param qrCode
     * @return
     */
    public String closeQrcode(String qrCode) {
        String accessToken = redisTemplate.opsForValue().get(UMSConfig.REDIS_TOKEN).toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String requestTimeStamp = format.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        RequestQrcode requestQrcode = RequestQrcode.builder()
                .msgId(uuid)
                .requestTimeStamp(requestTimeStamp)
                .srcReserve("经开区智慧停车")
                .mid(UMSConfig.MID)
                .tid(UMSConfig.TID)
                .instMid("QRPAYDEFAULT")
                .qrCodeId(qrCode)
                .build();
        String req = JSON.toJSONString(requestQrcode);
        String result = HttpRequest
                .post(UMSConfig.HOST_ONLINE_BASE + UMSConfig.HOST_GETQRCODE)
                .header("Authorization", accessToken)
                .body(req)
                .execute()
                .body();
        System.out.println(result);
        return result;
    }

    /**
     * 退款
     *
     * @param accessToken
     * @param billNo
     * @param billDate
     * @param refundAmount
     * @return
     */
    public String refund(String accessToken, String billNo, String billDate, Integer refundAmount) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String requestTimeStamp = format.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        RefundDto refundDto = RefundDto.builder()
                .msgId(uuid)
                .requestTimestamp(requestTimeStamp)
                .srcReserve("")
                .mid(UMSConfig.MID)
                .tid(UMSConfig.TID)
                .instMid("QRPAYDEFAULT")
                .billNo(billNo)
                .billDate(billDate)
                .refundAmount(refundAmount)
                .build();
        String req = JSON.toJSONString(refundDto);
        String result = HttpRequest
                .post(UMSConfig.HOST_ONLINE_BASE + UMSConfig.HOST_REFUND)
                .header("Authorization", accessToken)
                .body(req)
                .execute()
                .body();
        System.out.println(result);
        return result;
    }
}

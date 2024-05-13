package com.jsdc.zhtc.utils.pay;

import com.github.wxpay.sdk.WXPayUtil;
import com.jsdc.zhtc.model.WxPayConfig;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class WxAPIV3SignUtils {


    public static final String schema = "WECHATPAY2-SHA256-RSA2048";

    HttpUrl httpurl = HttpUrl.parse("https://api.mch.weixin.qq.com/v3/merchant/media/upload?a=1&b=0");

    /**
     * @param method
     * @param url
     * @param body
     * @param wxPayConfig 微信配置包含：mchId 商户号、 privateKey 商户证书私钥、serialNo 商户API证书序列号
     * @return
     * @throws Exception
     */
    public static String getToken(String method, HttpUrl url, String body, WxPayConfig wxPayConfig) throws Exception {
        String nonceStr = WXPayUtil.generateNonceStr();
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        String signature = sign(message.getBytes("utf-8"), wxPayConfig.getPrivate_key());

        return schema + " " + "mchid=\"" + wxPayConfig.getMch_id() + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + wxPayConfig.getSerial_no() + "\","
                + "signature=\"" + signature + "\"";
    }

    public static String getQuerySign(String method, HttpUrl url, String body, WxPayConfig wxPayConfig) throws Exception {
        String nonceStr = WXPayUtil.generateNonceStr();
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        String signature = sign(message.getBytes("utf-8"), wxPayConfig.getPrivate_key());
        return schema + " " + "mchid=\"" + wxPayConfig.getMch_id() + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + wxPayConfig.getSerial_no() + "\","
                + "signature=\"" + signature + "\"";
    }

    /**
     * @param appId:
     * @param prepayId:
     * @param wxPayConfig:
     * @return String
     * @author wp
     * @description 微信小程序调起支付获取paySign
     * @date 2022/11/14 8:55
     */
    @SneakyThrows
    public static String getPrepaySign(String appId, String prepayId, String nonceStr, String timestamp, WxPayConfig wxPayConfig) {
        String prepayStr = "prepay_id=" + prepayId;
        String unsign = appId + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + prepayStr + "\n";
        String prepaySign = sign(unsign.getBytes("utf-8"), wxPayConfig.getPrivate_key());
        return prepaySign;
    }

    public static String sign(byte[] message, String privateKey) throws Exception {

        Signature sign = Signature.getInstance("SHA256withRSA");

        PrivateKey privateKey1 = getPrivateKey(privateKey);
        sign.initSign(privateKey1);

        sign.update(message);

        return Base64.getEncoder().encodeToString(sign.sign());
    }

    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }

        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }


    /**
     * String转私钥PrivateKey
     * @param key
     * @return
     * @throws Exception
     */
//    public static PrivateKey getPrivateKey(String key) throws Exception {
//        byte[] keyBytes;
//        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
//        return privateKey;
//    }

    /**
     * 获取私钥。
     *
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)), "utf-8");
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }


}

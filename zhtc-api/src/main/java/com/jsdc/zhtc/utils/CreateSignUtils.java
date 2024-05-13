package com.jsdc.zhtc.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @projectName: zhtc
 * @className: CreateSignUtils
 * @author: wp
 * @description:
 * @date: 2022/10/19 8:46
 */
public class CreateSignUtils {
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String MD5(String s) {
        return MD5(s, "utf-8");
    }

    public static String MD5(String s, String charset) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.reset();
            byte abyte0[] = messagedigest.digest(s.getBytes(charset));
            return byteToString(abyte0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String byteToString(byte abyte0[]) {
        int i = abyte0.length;
        char ac[] = new char[i * 2];
        int j = 0;
        for (int k = 0; k < i; k++) {
            byte byte0 = abyte0[k];
            ac[j++] = hexDigits[byte0 >>> 4 & 0xf];
            ac[j++] = hexDigits[byte0 & 0xf];
        }
        return new String(ac);
    }

    public static String getSign(String data, String ukey) {
        String sign = data + "key=" + ukey;
        sign = MD5(sign).toUpperCase();
        return sign;
    }

    public static void main(String[] args) {
        //密钥由艾停车提供，密钥和车场号一一对应
        String ukey = "K1DIXZCLWNO0J0WV";

        JSONObject data = new JSONObject(true);
        data.put("uid", "大门入口");
        data.put("in_time", 1611731098);
        data.put("car_number", "赣AE3434");
        data.put("c_type", "临时车");
        data.put("in_channel_id", "大门入口");
        data.put("order_id", "1902");
        data.put("force_update", 1);
        data.put("empty_plot", 879);
        data.put("car_type", "");
        data.put("source", "ist");
        data.put("pic_addr", "http://ist-falcon.oss-cn-shenzhen.aliyuncs.com/order-images/31809/in/1902.jpg?Expires=1611817499&OSSAccessKeyId=LTAIQQrl6GICP0QX&Signature=FnKZNrQ5nkdJlhgVTYp4v7Q3ejY%3D");

        // 生成带签名的字符串并使用MD5生成签名，然后转大写
        String sign = data.toJSONString() + "key=" + ukey;
        sign = MD5(sign).toUpperCase();
        System.out.println(sign);//242441AAC1911DB6E47A942A2B6477D5
    }
}

package com.jsdc.zhtc.utils.pay;

import com.jsdc.zhtc.model.WxPayConfig;
import okhttp3.HttpUrl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;


public class HttpsPostUtil {

    // 连接超时
    public static final Integer REQUEST_TIMEOUT = 1;
    // 请求超时
    public static final Integer CONNECT_TIMEOUT = 1;
    // 读取超时
    public static final Integer SOCKET_TIMEOUT = 1;
    // 字符集
    public static final String CHARSET = "UTF-8";


    public static String doPost(String methodType, String url, String dataStr, WxPayConfig wxPayConfig) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            // 设置超时
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(REQUEST_TIMEOUT) // 连接超时
                    .setConnectTimeout(CONNECT_TIMEOUT) // 请求超时
                    .setSocketTimeout(SOCKET_TIMEOUT).build();// 读取超时
            httpPost.setConfig(config);
            HttpUrl httpUrl = HttpUrl.parse(url);
            String token = WxAPIV3SignUtils.getToken(methodType, httpUrl, dataStr, wxPayConfig);

            // 设置请求头
            httpPost.setHeader("Authorization", token);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Linux Android 10 TAS-AN00 Build/HUAWEITAS-AN00 wv) AppleWebKit/537.36 (KHTML,  Gecko) Version/4.0 " +
                    "Chrome/86.0.4240.99 XWEB/3171 MMWEBSDK/20211001 Mobile Safari/537.36 MMWEBID/4700 MicroMessenger/8.0.16.2040(0x280010CC) " +
                    "Process/toolsmp WeChat/arm64 Weixin NetType/WIFI Language/zh_CN ABI/arm64");
            // httpPost.setHeader("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            //httpPost.setHeader("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");

//            httpsUrlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//            httpsUrlConnection.setRequestProperty("Accept", "application/json");
//            //设置User-Agent信息
//            httpsUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

            // 设置参数
            StringEntity entity = new StringEntity(dataStr, "utf-8");
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, CHARSET);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String doGet(String methodType, String url, String dataStr, WxPayConfig wxPayConfig) {
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            // 设置超时
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(REQUEST_TIMEOUT) // 连接超时
                    .setConnectTimeout(CONNECT_TIMEOUT) // 请求超时
                    .setSocketTimeout(SOCKET_TIMEOUT).build();// 读取超时
            httpGet.setConfig(config);
            HttpUrl httpUrl = HttpUrl.parse(url);
            String token = WxAPIV3SignUtils.getQuerySign(methodType, httpUrl, dataStr, wxPayConfig);

            // 设置请求头
            httpGet.setHeader("Authorization", token);
            //httpGet.setHeader("Content-Type" , "application/json");
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux Android 10 TAS-AN00 Build/HUAWEITAS-AN00 wv) AppleWebKit/537.36 (KHTML,  Gecko) Version/4.0 " +
                    "Chrome/86.0.4240.99 XWEB/3171 MMWEBSDK/20211001 Mobile Safari/537.36 MMWEBID/4700 MicroMessenger/8.0.16.2040(0x280010CC) " +
                    "Process/toolsmp WeChat/arm64 Weixin NetType/WIFI Language/zh_CN ABI/arm64");
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, CHARSET);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

package com.jsdc.zhtc.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class HttpClientUtils {

    public static String doPost(String url, List<NameValuePair> params) {
        String body = "";
        HttpPost httpPost = new HttpPost(url);
        // HTTP客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            // HTTP响应
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            // HTTP实体
            HttpEntity entity = httpResponse.getEntity();
            // 输出内容
            body = EntityUtils.toString(entity, "UTF-8");
            System.out.println(body);
            // 销毁HTTP实体
            EntityUtils.consume(entity);
            // 关闭HTTP响应
            httpResponse.close();
            // HTTP请求关闭
            httpPost.abort();
            // 关闭HTTP客户端
            httpClient.close();
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return body;
    }

    public static String doGet(String url, List<NameValuePair> params) {
        // 将请求参数转换成GET方式
        HttpRequestBase httpRequestBase = new HttpGet(url + "?" + URLEncodedUtils.format(params, "UTF-8"));
        // HTTP客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String body = "";
        try {
            // HTTP响应
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequestBase);
            // HTTP实体
            HttpEntity entity = httpResponse.getEntity();
            // 输出内容
            body = EntityUtils.toString(entity, "UTF-8");
            // 销毁HTTP实体
            EntityUtils.consume(entity);
            // 关闭HTTP响应
            httpResponse.close();
            // HTTP请求关闭
            httpRequestBase.abort();
            // 关闭HTTP客户端
            httpClient.close();
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return body;
    }
}

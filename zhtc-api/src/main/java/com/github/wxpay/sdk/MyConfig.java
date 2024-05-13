package com.github.wxpay.sdk;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class MyConfig extends WXPayConfig {
    private byte[] certData;

    public MyConfig() throws Exception {
        // String certPath = "C:/file/apiclient_cert.p12";
        // File file = new File(certPath);
        // InputStream certStream = new FileInputStream(file);
        // this.certData = new byte[(int) file.length()];
        // certStream.read(this.certData);
        // certStream.close();
    }

    /**
     * 开发者ID
     */
    public String appID;

    @Override
    public String getAppID() {
        //return "wxab8acb865bb1637e";
        return this.appID;
    }

    /**
     * 商户号
     */
    public String mchID;

    @Override
    public String getMchID() {
        //return "11473623";
        return this.mchID;
    }

    /**
     * API密钥
     */
    public String key;

    @Override
    public String getKey() {
        //return "2ab9071b06b9f739b950ddb41db2690d";
        return this.key;
    }


    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo("api.mch.weixin.qq.com", false);

            }

        };

    }


}

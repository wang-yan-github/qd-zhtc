package com.jsdc.zhtc.utils.pay;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeyPairFactory {

    private KeyStore store;

    private final Object lock = new Object();


    /**
     * 获取公私钥.
     *
     * @param keyPath  the key path
     * @param keyAlias the key alias
     * @param keyPass  password
     * @return the key pair
     */
    public KeyPair createPKCS12(String keyPath, String keyAlias, String keyPass) {
        ClassPathResource resource = new ClassPathResource(keyPath);
        char[] pem = keyPass.toCharArray();
        try {
            synchronized (lock) {
                if (store == null) {
                    synchronized (lock) {
                        store = KeyStore.getInstance("PKCS12");
                        store.load(resource.getInputStream(), pem);
                    }
                }
            }
            X509Certificate certificate = (X509Certificate) store.getCertificate(keyAlias);
            certificate.checkValidity();
            // 证书的序列号 也有用
            String serialNumber = certificate.getSerialNumber().toString(16).toUpperCase();
            // 证书的 公钥
            PublicKey publicKey = certificate.getPublicKey();
            // 证书的私钥
            PrivateKey storeKey = (PrivateKey) store.getKey(keyAlias, pem);

            return new KeyPair(publicKey, storeKey);

        } catch (Exception e) {
            throw new IllegalStateException("Cannot load keys from store: " + resource, e);
        }
    }

    /**
     * V3  SHA256withRSA 签名.
     *
     * @param method       请求方法  GET  POST PUT DELETE 等
     * @param canonicalUrl 例如  https://api.mch.weixin.qq.com/v3/pay/transactions/app?version=1 ——> /v3/pay/transactions/app?version=1
     * @param timestamp    当前时间戳   因为要配置到TOKEN 中所以 签名中的要跟TOKEN 保持一致
     * @param nonceStr     随机字符串  要和TOKEN中的保持一致
     * @param body         请求体 GET 为 "" POST 为JSON
     * @param keyPair      商户API 证书解析的密钥对  实际使用的是其中的私钥
     * @return the string
     */
    @SneakyThrows
    public String sign(String method, String canonicalUrl, long timestamp, String nonceStr, String body, KeyPair keyPair) {
        String signatureStr = Stream.of(method, canonicalUrl, String.valueOf(timestamp), nonceStr, body)
                .collect(Collectors.joining("\n", "", "\n"));
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(keyPair.getPrivate());
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(sign.sign());
    }


}

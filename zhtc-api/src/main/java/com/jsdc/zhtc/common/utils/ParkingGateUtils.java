package com.jsdc.zhtc.common.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 闸机接口
 *
 * @ClassName ParkingGateUtils
 * @Description TODO
 * @Author xujian
 * @Date 2022/2/11 13:54
 * @Version 1.0
 */
@Component
public class ParkingGateUtils {

    Logger logger = LoggerFactory.getLogger(ParkingGateUtils.class);

    private static String dcGateOpenUrl;

    private static String dcVoiceUrl;

    @Value("${jsdc.gateUrl.dc.open}")
    public void setDcGateOpenUrl(String dcGateOpenUrl) {
        ParkingGateUtils.dcGateOpenUrl = dcGateOpenUrl;
    }

    @Value("${jsdc.gateUrl.dc.voice}")
    public void setDcVoiceUrl(String dcVoiceUrl) {
        ParkingGateUtils.dcVoiceUrl = dcVoiceUrl;
    }

    public boolean mqttOpenGate(String deviceId, String content) {
        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        params.put("content", content);
        String body = HttpRequest.post(dcGateOpenUrl).setConnectionTimeout(5000)
                .form(params)
                .execute().body();
        return true;
    }

    public boolean mqttVoice(String deviceId, String content) {
        JSONObject params = new JSONObject();
        params.put("deviceId", deviceId);
        params.put("content", content);
        String body = HttpRequest.post(dcVoiceUrl).setConnectionTimeout(5000)
                .form(params)
                .execute().body();
        return true;
    }

}

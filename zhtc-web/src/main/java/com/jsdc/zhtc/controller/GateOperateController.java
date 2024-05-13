package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.common.ClientUtils;
import com.jsdc.zhtc.customer.GateCtrl;
import com.jsdc.zhtc.vo.ResultInfo;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: zhtc
 * @className: GateOperateController
 * @author: wp
 * @description:
 * @date: 2023/7/18 11:50
 */
@RestController
@RequestMapping("dc")
public class GateOperateController {

    @Value("${mqtt.pubClientId}")
    String pubClientId;
    @Value("${mqtt.subClientId}")
    String subClientId;

    @RequestMapping("openGate.do")
    public ResultInfo openGate(String deviceId, String content) {
        System.out.println(">>>>>>>>>>>>" + deviceId);
        System.out.println(">>>>>>>>>>>>" + content);
        String topic = "/a1BkUFnh0eA/" + deviceId + "/user/get";
        String voiceTopic = "/a1BkUFnh0eA/" + deviceId + "/user/get";
        GateCtrl.open(topic, voiceTopic, pubClientId, content);
        return ResultInfo.success();
    }

    @RequestMapping("voice.do")
    public ResultInfo voice(String deviceId, String content) {
        System.out.println(">>>>>>>>>>>>" + deviceId);
        System.out.println(">>>>>>>>>>>>" + content);
        String voiceTopic = "/a1BkUFnh0eA/" + deviceId + "/user/get";
        GateCtrl.voice(voiceTopic, pubClientId, content);
        return ResultInfo.success();
    }

    /**
     * 开启推流
     *
     * @param deviceId
     * @return
     */
    @RequestMapping("startRtmp.do")
    public ResultInfo startRtmp(String deviceId) {
        GateCtrl.startRtmp(deviceId, pubClientId);
        return ResultInfo.success();
    }

    /**
     * 关闭推流
     *
     * @param deviceId
     * @return
     */
    @RequestMapping("stopRtmp.do")
    public ResultInfo stopRtmp(String deviceId) {
        GateCtrl.stopRtmp(deviceId, pubClientId);
        return ResultInfo.success();
    }

    @RequestMapping("addTopic.do")
    public ResultInfo addTopic(String topic) {
        MqttClient client = ClientUtils.getClient(subClientId);
        if (null == client) {
            return ResultInfo.error("无mqtt客户端");
        }
        return ResultInfo.success();
    }

    @RequestMapping("rec.do")
    public ResultInfo rec(String content) {
        content.length();
        return ResultInfo.success();
    }
}

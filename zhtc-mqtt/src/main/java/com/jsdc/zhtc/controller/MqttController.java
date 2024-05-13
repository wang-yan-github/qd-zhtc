package com.jsdc.zhtc.controller;

import com.jsdc.zhtc.common.ClientUtils;
import com.jsdc.zhtc.service.ParkDeviceService;
import com.jsdc.zhtc.service.ParkService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: MqttController
 * Description:
 * date: 2023/10/11 15:23
 *
 * @author thr
 */
@Controller
@RequestMapping("mqtt")
public class MqttController {

    @Autowired
    ParkDeviceService deviceService;
    @Autowired
    ParkService parkService;

    @Value("${mqtt.subClientId}")
    String subClientId;

    /**
     * 动态订阅主题
     *
     * @return
     * @paaram deviceCode 道闸设备编码
     */
    @RequestMapping("addTopic")
    @ResponseBody
    public ResultInfo addTopic(String deviceCode) throws InterruptedException {
        try {
//            List<String> subtopicList = deviceCodes.stream().map(x -> "/a1BkUFnh0eA/" + x + "/user/update").collect(Collectors.toList());
//            mqttClient.subscribe(subtopicList.toArray(new String[]{}));

//            InitApplication.mqttClient.unsubscribe("$share/zhtc/a1BkUFnh0eA/" + deviceCode + "/user/update");
//            InitApplication.mqttClient.unsubscribe("/a1BkUFnh0eA/" + deviceCode + "/user/update");

            MqttClient client = ClientUtils.getClient(subClientId);
//            client.subscribe("$share/zhtc//a1BkUFnh0eA/" + deviceCode + "/user/update");
            client.subscribe("/a1BkUFnh0eA/" + deviceCode + "/user/update");

        } catch (MqttSecurityException e) {
            return ResultInfo.error("订阅失败");
        } catch (MqttException e) {
            return ResultInfo.error("订阅失败");
        }
        return ResultInfo.success("订阅成功");
    }

}

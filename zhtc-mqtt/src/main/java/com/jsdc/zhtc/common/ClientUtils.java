package com.jsdc.zhtc.common;

import lombok.experimental.UtilityClass;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: zhtc
 * @className: ClientUtils
 * @author: wp
 * @description:
 * @date: 2023/7/7 14:36
 */
@UtilityClass
public class ClientUtils {
    public Map<String, MqttClient> clientMap = new HashMap<>();

    public MqttClient getClient(String clientId){
        MqttClient client = clientMap.get(clientId);
        return client;
    }

    public void setClient(String clientId, MqttClient client){
        clientMap.put(clientId, client);
    }

    /**
     * 推送消息至指定主题
     * @param clientId
     * @param topic
     * @param msg
     * @return
     * @throws MqttException
     */
    public Boolean sendMsg(String clientId, String topic, String msg) throws MqttException {
        MqttClient client = getClient(clientId);
        if(null == client){
            return false;
        }
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(0);
        client.publish(topic, message);
        return true;
    }
}

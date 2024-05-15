package com.jsdc.zhtc.bean;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.zhtc.common.ClientUtils;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.customer.MessageRcvCallBack;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.ParkDevice;
import com.jsdc.zhtc.service.ParkDeviceService;
import com.jsdc.zhtc.service.ParkService;
import com.jsdc.zhtc.vo.ResultInfo;
import com.xiaoleilu.hutool.util.CollectionUtil;
import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: zhtc
 * @className: InitApplication
 * @author: wp
 * @description:
 * @date: 2023/7/7 14:18
 */
@Component
public class InitApplication {
    @Value("${mqtt.subTopic}")
    String subTopic;
    @Value("${mqtt.pubTopic}")
    String pubTopic;
    @Value("${mqtt.broker}")
    String broker;
    @Value("${mqtt.subClientId}")
    String subClientId;
    @Value("${mqtt.pubClientId}")
    String pubClientId;
    @Value("${mqtt.max_reconnect_time}")
    Integer maxReconnectTime;
    @Autowired
    ParkDeviceService deviceService;
    @Autowired
    ParkService parkService;

    Logger logger = LoggerFactory.getLogger(InitApplication.class);

    public void init() {
        initSub();
        initPub();
    }

    public void initSub() {
        try {
            List<Park> parkList = parkService.selectList(Wrappers.<Park>lambdaQuery().eq(Park::getBrand, "dc").eq(Park::getIs_del, GlobalData.IS_DEL_NO));
            if (CollectionUtil.isEmpty(parkList)) {
                return;
            }
            List<Integer> parkIdList = parkList.stream().map(x -> x.getId()).collect(Collectors.toList());
            List<ParkDevice> parkDeviceList = deviceService.selectList(Wrappers.<ParkDevice>lambdaQuery().in(ParkDevice::getPark_id, parkIdList).eq(ParkDevice::getIs_del, GlobalData.IS_DEL_NO));
            if (CollectionUtil.isEmpty(parkDeviceList)) {
                return;
            }
            List<String> deviceCodeList = parkDeviceList.stream().map(x -> x.getDevice_code()).collect(Collectors.toList());
//            List<String> subtopicList = deviceCodeList.stream().map(x -> "$share/zhtc//a1BkUFnh0eA/" + x + "/user/update").collect(Collectors.toList());
            List<String> subtopicList = deviceCodeList.stream().map(x -> "/a1BkUFnh0eA/" + x + "/user/update").collect(Collectors.toList());
            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient client  = new MqttClient(broker, subClientId, persistence);
            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            // 保留会话
            connOpts.setCleanSession(true);
            // 设置回调
            client.setCallback(new MessageRcvCallBack());
            // 建立连接
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            client.subscribe(subtopicList.toArray(new String[]{}));
            //client.subscribe("/a1BkUFnh0eA/+/user/update");
            ClientUtils.setClient(subClientId, client);
        } catch (MqttSecurityException e) {
            logger.error(e.getMessage());
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * 断线重连
     */
    public void reconnect() {
        new Thread(() -> {
            int times = 1;
            while (true) {
                MqttClient client = ClientUtils.clientMap.get(subClientId);
                if (null != client) {
                    if (!client.isConnected()) {
                        logger.info("订阅客户端第" + times + "次重连");
                        times++;
                        initSub();
                    } else {
                        break;
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        new Thread(() -> {
            int times = 1;
            while (true) {
                MqttClient client = ClientUtils.clientMap.get(pubClientId);
                if (null != client) {
                    if (!client.isConnected()) {
                        logger.info("发布客户端第" + times + "次重连");
                        times++;
                        initPub();
                    } else {
                        break;
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void initPub() {
        try {
            //客户端变更为pubClientId
            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient mqttClient = new MqttClient(broker, pubClientId, persistence);
            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            // 保留会话
            connOpts.setCleanSession(true);
            // 设置回调
            mqttClient.setCallback(new MessageRcvCallBack());
            // 建立连接
            System.out.println("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            ClientUtils.setClient(pubClientId, mqttClient);
        } catch (MqttSecurityException e) {
            logger.error(e.getMessage());
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }
    }

}

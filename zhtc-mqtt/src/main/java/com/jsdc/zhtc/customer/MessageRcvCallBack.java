package com.jsdc.zhtc.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.zhtc.bean.InitApplication;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.ParkingGateUtils;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.config.SpringContextHolder;
import com.jsdc.zhtc.model.Park;
import com.jsdc.zhtc.model.ParkDevice;
import com.jsdc.zhtc.model.ParkingOrderPics;
import com.jsdc.zhtc.service.ParkDeviceService;
import com.jsdc.zhtc.service.ParkService;
import com.jsdc.zhtc.service.ParkingOrderService;
import com.jsdc.zhtc.vo.DcParkInfo;
import com.jsdc.zhtc.vo.ParkingOrderVo;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jsdc.core.base.Base.empty;
import static com.jsdc.core.base.Base.notEmpty;

/**
 * @projectName: zhtc
 * @className: MessageRcvCallBack
 * @author: wp
 * @description:
 * @date: 2023/7/7 14:43
 */
public class MessageRcvCallBack implements MqttCallback {

    Logger logger = LoggerFactory.getLogger(MessageRcvCallBack.class);
    ParkingOrderService parkingOrderService = SpringContextHolder.getBean(ParkingOrderService.class);
    ParkDeviceService deviceService = SpringContextHolder.getBean(ParkDeviceService.class);

    ParkService parkService = SpringContextHolder.getBean(ParkService.class);

    ParkingGateUtils parkingGateUtils = SpringContextHolder.getBean(ParkingGateUtils.class);

    InitApplication initApplication = SpringContextHolder.getBean(InitApplication.class);

    @Override
    public void connectionLost(Throwable throwable) {
        logger.error("连接断开，可以做重连");
        initApplication.reconnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        System.out.println("mqtt订阅心跳===========================");
        try {
//            logger.info("接收消息主题:" + topic);
//            logger.info("接收消息Qos:" + mqttMessage.getQos());
//            logger.info("接收消息内容:" + new String(mqttMessage.getPayload()));
//            logger.info("mqtt订阅设备心跳Payload:" + new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
            JSONObject jsonObject = JSONObject.parseObject(new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
//            logger.info("mqtt订阅设备心跳data:" + jsonObject.toJSONString());
            if (StringUtils.equals("result", jsonObject.getString("cmd"))) {

            } else if (StringUtils.equals("offline", jsonObject.getString("cmd"))) {
                //设备离线
                String deviceCode = jsonObject.getString("device_name");
                ParkDevice parkDevice = deviceService.selectOne(Wrappers.<ParkDevice>lambdaQuery().eq(ParkDevice::getDevice_code, deviceCode).eq(ParkDevice::getIs_del, GlobalData.IS_DEL_NO));
                if (!parkDevice.getStatus().equals("3")) {
                    //状态 1.空闲 2.使用中 3.异常
                    parkDevice.setStatus("3");
                    deviceService.updateById(parkDevice);
                }
            } else if (StringUtils.equals("mqtt_herat", jsonObject.getString("cmd"))) {
                //设备在线
                String deviceCode = jsonObject.getString("device_name");
                ParkDevice parkDevice = deviceService.selectOne(Wrappers.<ParkDevice>lambdaQuery().eq(ParkDevice::getDevice_code, deviceCode).eq(ParkDevice::getIs_del, GlobalData.IS_DEL_NO));
                if (!parkDevice.getStatus().equals("2")) {
                    //状态 1.空闲 2.使用中 3.异常
                    parkDevice.setStatus("2");
                    deviceService.updateById(parkDevice);
                }
            }
        } catch (Exception e) {
            logger.error("mqtt订阅心跳异常===========================");
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
    }



    public void parkIn(String deviceCode, String data) {
        try {
//            data = new String(data.getBytes(), StandardCharsets.UTF_8);

            DcParkInfo dcParkInfo = JSON.parseObject(data, DcParkInfo.class);
            ParkingOrderVo vo = new ParkingOrderVo();

            List<ParkingOrderPics> list = new ArrayList<>();
            String base64 = dcParkInfo.getFull_pic();
            ParkingOrderPics parkingOrderPics = new ParkingOrderPics();
            parkingOrderPics.setPicture_type(GlobalData.PARKING_DIRECTION_IN);
            parkingOrderPics.setBase64(base64);
            list.add(parkingOrderPics);
            vo.setDetails(list);
            vo.setCar_no(StringUtils.isEmpty(dcParkInfo.getPlate_num()) ? "" : dcParkInfo.getPlate_num().toUpperCase());
            switch (dcParkInfo.getPlate_color()) {
                case "蓝色":
                    vo.setCar_type("1");
                    break;
                case "黄色":
                    vo.setCar_type("3");
                    break;
                case "白色":
                    vo.setCar_type("4");
                    break;
                case "绿色":
                    vo.setCar_type("2");
                    break;
                default:
                    vo.setCar_type("1");
                    break;
            }
            ParkDevice parkDevice = deviceService.selectOne(Wrappers.<ParkDevice>lambdaQuery().eq(ParkDevice::getDevice_code, deviceCode).eq(ParkDevice::getIs_del, GlobalData.IS_DEL_NO));
            if (null == parkDevice) {
                logger.error("未查询到设备信息，设备编码: " + deviceCode);
                return;
            }
            Park park = parkService.selectById(parkDevice.getPark_id());
            vo.setParkCode(park.getPark_code());
            vo.setDrivein_gate(deviceCode);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            vo.setDrivein_time(dateFormat.parse(dcParkInfo.getLocal_time()));
            vo.setSource(GlobalData.PARKING_SOURCE_CAMERA);

            String topic = "/a1BkUFnh0eA/" + deviceCode + "/user/get";
            String voiceTopic = "/a1BkUFnh0eA/" + deviceCode + "/user/get";

            String content = "";
            /**
             * 判断该停车场是否开启限流
             * 限流开关 0开启 1关闭
             */
            if (notEmpty(park.getOn_off()) && park.getOn_off().equals("0")) {
                if (notEmpty(park.getFree_count()) && park.getFree_count() <= 0) {
                    //语音提示该停车场车位已满，无法驶入
                    if(StringUtils.equals(park.getBrand(), "dc")){
                        content = "停车场车位已满";
                        parkingGateUtils.mqttVoice(parkDevice.getDevice_code(), content);
                    }
//                    logger.error("语音提示该停车场车位已满，无法驶入");
//                    return ResultInfo.error("语音提示该停车场车位已满，无法驶入");
                }
            }
            if (empty(content)) {
//                GateCtrl.open(topic, voiceTopic, "pub_client_104", vo.getCar_no() + ",欢迎光临");
            GateCtrl.open(topic, voiceTopic, "pub_client", vo.getCar_no() + ",欢迎光临");
                parkingOrderService.entry(vo);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}

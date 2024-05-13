package com.jsdc.zhtc.customer;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.common.ClientUtils;
import com.jsdc.zhtc.utils.Crc16Utils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @projectName: zhtc
 * @className: GateCtrl
 * @author: wp
 * @description:
 * @date: 2023/7/13 13:41
 */
@UtilityClass
public class GateCtrl {

    private final static String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    static byte[] crc16_tab_h = {(byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40};

    static byte[] crc16_tab_l = {(byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5, (byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8, (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB, (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC, (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12,
            (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0, (byte) 0x10, (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35, (byte) 0x34, (byte) 0xF4, (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9, (byte) 0xF8, (byte) 0x38, (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C, (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7,
            (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64, (byte) 0xA4, (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9, (byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD, (byte) 0xBC, (byte) 0x7C, (byte) 0xB4, (byte) 0x74,
            (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0, (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53, (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54, (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58, (byte) 0x98, (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D,
            (byte) 0x4C, (byte) 0x8C, (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81, (byte) 0x80, (byte) 0x40};

    Logger logger = LoggerFactory.getLogger(GateCtrl.class);

    /**
     * 开闸
     *
     * @param topic
     * @param clientId
     */
    @SneakyThrows
    public void open(String topic, String voiceTopic, String clientId, String content) {
        openYy(topic, voiceTopic, clientId, content);
        openKz(topic, voiceTopic, clientId, content);
    }

    /**
     * 开闸
     *
     * @param topic
     * @param voiceTopic
     * @param clientId
     * @param content
     */
    @Async("orderThreadPool")
    public void openKz(String topic, String voiceTopic, String clientId, String content) {
        String cmd = "iooutput";
        String msgId = createMsgId();
        int ionum = 1;
        String action = "on";
        Long utcTs = new Date().getTime() / 1000;
        JSONObject params = new JSONObject();
        params.put("cmd", cmd);
        params.put("msg_id", msgId);
        params.put("ionum", ionum);
        params.put("action", action);
        params.put("utc_ts", utcTs);
        try {
            ClientUtils.sendMsg(clientId, topic, params.toJSONString());
            logger.info("openKz开闸=========clientId：" + clientId + ",topic：" + topic + ",msg：" + params.toJSONString());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播报语音
     *
     * @param topic
     * @param voiceTopic
     * @param clientId
     * @param content
     */
    @Async("orderThreadPool")
    public void openYy(String topic, String voiceTopic, String clientId, String content) {
        String voiceCmd = null;
        try {
            voiceCmd = initVoice(content);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String textCmd = "";
        String[] textLine = content.split(",");
        for (int i = 0; i < textLine.length; i++) {
            try {
                textCmd += initText(textLine[i], i);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray data = new JSONArray();
        jsonObject.put("cmd", "rs485");
        jsonObject.put("msg_id", createMsgId());
        jsonObject.put("encode_type", "hex2string");
        JSONObject jo = new JSONObject();
        jo.put("data", voiceCmd + textCmd);
        data.add(jo);
        jsonObject.put("rs485ch1_data", data);
        try {
            ClientUtils.sendMsg(clientId, voiceTopic, jsonObject.toJSONString());
            logger.info("openYy播报语音=========clientId：" + clientId + ",topic：" + topic + ",msg：" + jsonObject.toJSONString());
            logger.info("openYy播报语音内容：" + content);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void voice(String topic, String clientId, String content) {
        String voiceCmd = initVoice(content);
        String textCmd = "";
        String[] textLine = content.split(",");
        for (int i = 0; i < textLine.length; i++) {
            textCmd += initText(textLine[i], i);
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray data = new JSONArray();
        jsonObject.put("cmd", "rs485");
        jsonObject.put("msg_id", createMsgId());
        jsonObject.put("encode_type", "hex2string");
        JSONObject jo = new JSONObject();
        jo.put("data", voiceCmd + textCmd);
        data.add(jo);
        jsonObject.put("rs485ch1_data", data);
        ClientUtils.sendMsg(clientId, topic, jsonObject.toJSONString());
    }

    /**
     * 开启推流
     *
     * @param deviceId
     * @param clientId
     * @return
     */
    @SneakyThrows
    public Boolean startRtmp(String deviceId, String clientId) {
        JSONObject params = new JSONObject();
        params.put("cmd", "startRtmp");
        params.put("ip", "58.218.188.178");
        params.put("deviceCode", deviceId);
        String topic = "/a1BkUFnh0eA/" + deviceId + "/gateway";
        ClientUtils.sendMsg(clientId, topic, params.toJSONString());
        return true;
    }

    /**
     * 关闭推流
     *
     * @param deviceId
     * @param clientId
     * @return
     */
    @SneakyThrows
    public Boolean stopRtmp(String deviceId, String clientId) {
        JSONObject params = new JSONObject();
        params.put("cmd", "stopRtmp");
        params.put("ip", "58.218.188.178");
        params.put("deviceCode", deviceId);
        String topic = "/a1BkUFnh0eA/" + deviceId + "/gateway";
        ClientUtils.sendMsg(clientId, topic, params.toJSONString());
        return true;
    }


    public static String byteToHex(int n) {
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexArray[d1] + hexArray[d2];
    }

    public static void main(String[] args) throws Exception {
        String msg = "01 " + Crc16Utils.gb2312ToString("欢迎光临,请入场停车");

        String dataLen = byteToHex(msg.split(" ").length);

        String hexString = "00 64 FF FF 30 " + dataLen + " " + msg;
        String[] arr = hexString.split(" ");
        int length = arr.length;
        byte[] hexArr = new byte[length];

        for (int i = 0; i < length; i++) {
            String hexValue = arr[i];
            byte byteValue = (byte) Integer.parseInt(hexValue, 16);
            hexArr[i] = byteValue;
        }
        System.out.println(hexArr);
        String crc16 = calcCrc16(hexArr, 0, hexArr.length, 0);
        String lower = crc16.substring(0, 2);
        String high = crc16.substring(2, 4);
        crc16 = high + lower;
        hexString += crc16;
        String result = hexString.replaceAll(" ", "");
        System.out.println(result);

    }

    public String initVoice(String content) throws UnsupportedEncodingException {
        String msg = "01 " + Crc16Utils.gb2312ToString(content);
        String dataLen = byteToHex(msg.split(" ").length);
        String hexString = "00 64 FF FF 30 " + dataLen + " " + msg;
        String[] arr = hexString.split(" ");
        int length = arr.length;
        byte[] hexArr = new byte[length];

        for (int i = 0; i < length; i++) {
            String hexValue = arr[i];
            byte byteValue = (byte) Integer.parseInt(hexValue, 16);
            hexArr[i] = byteValue;
        }
        System.out.println(hexArr);
        String crc16 = calcCrc16(hexArr, 0, hexArr.length, 0);
        String lower = crc16.substring(0, 2);
        String high = crc16.substring(2, 4);
        crc16 = high + lower;
        hexString += crc16;
        String result = hexString.replaceAll(" ", "");
        System.out.println(result);
        return result;
    }

    public String initText(String content, int rowNo) throws UnsupportedEncodingException {
        String text = Crc16Utils.gb2312ToString(content);
        String head = "00 64 FF FF 62";
        String twid = StringUtils.leftPad(String.valueOf(rowNo), 2, '0');
        String etm = " 15";//文字显示的方式 连续左移
        String ets = " 01";
        String gst = " 20";//屏幕显示秒数
        String dt = " 0F";
        String ra1 = " 01 01";
        String findex = " 03";
        String drs = " 05";
        String tc = " FF FF 00 00";
        String ra2 = " 00 00 00 00";
        String r1 = " 00";

        String dataLen = byteToHex(text.split(" ").length);

        String tl = dataLen;
        String dl = byteToHex(text.split(" ").length + 19);

        String hexString = head + " " + dl + " " + twid + etm + ets + gst + dt + ra1 + findex + drs + tc + ra2 + " " + tl + r1 + " " + text;
        String[] arr = hexString.split(" ");
        int length = arr.length;
        byte[] hexArr = new byte[length];

        for (int i = 0; i < length; i++) {
            String hexValue = arr[i];
            byte byteValue = (byte) Integer.parseInt(hexValue, 16);
            hexArr[i] = byteValue;
        }
        String crc16 = calcCrc16(hexArr, 0, hexArr.length, 0);
        String lower = crc16.substring(0, 2);
        String high = crc16.substring(2, 4);
        crc16 = high + lower;
        hexString += crc16;
        String result = hexString.replaceAll(" ", "");
        return result;
    }

    public String getData(String content) throws UnsupportedEncodingException {
        String msg = "01 " + Crc16Utils.gb2312ToString(content);
        String dataLen = byteToHex(msg.split(" ").length);
        String hexString = "00 64 FF FF 30 " + dataLen + " " + msg;
        String[] arr = hexString.split(" ");
        int length = arr.length;
        byte[] hexArr = new byte[length];

        for (int i = 0; i < length; i++) {
            String hexValue = arr[i];
            byte byteValue = (byte) Integer.parseInt(hexValue, 16);
            hexArr[i] = byteValue;
        }
        System.out.println(hexArr);
        String crc16 = calcCrc16(hexArr, 0, hexArr.length, 0);
        String lower = crc16.substring(0, 2);
        String high = crc16.substring(2, 4);
        crc16 = high + lower;
        hexString += crc16;
        String result = hexString.replaceAll(" ", "");
        System.out.println(result);
        return result;
    }


    public static String calcCrc16(byte[] data, int offset, int len, int preval) {
        preval = 0xffff;
        int ucCRCHi = (preval & 0xff00) >> 8;
        int ucCRCLo = preval & 0x00ff;
        int iIndex;
        for (int i = 0; i < len; ++i) {
            iIndex = (ucCRCLo ^ data[offset + i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_tab_h[iIndex];
            ucCRCHi = crc16_tab_l[iIndex];
        }
        int i = ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
        return StringUtils.leftPad(Integer.toHexString(i).toUpperCase(), 4, '0');
    }

    private String createMsgId() {
        Long millions = new Date().getTime();
        String random = RandomUtil.randomString(7);
        return millions + random;
    }
}

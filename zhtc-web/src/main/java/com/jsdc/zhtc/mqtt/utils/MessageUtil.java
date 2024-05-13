package com.jsdc.zhtc.mqtt.utils;

import io.netty.buffer.ByteBuf;

/**
 * transfer message from Message and MqttMessage
 */
public class MessageUtil {

    public static byte[] readBytesFromByteBuf(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }

}

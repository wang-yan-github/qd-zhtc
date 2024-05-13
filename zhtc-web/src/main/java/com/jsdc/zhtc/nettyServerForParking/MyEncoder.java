package com.jsdc.zhtc.nettyServerForParking;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @projectName: zhtc
 * @className: MyEncoder
 * @author: wp
 * @description:
 * @date: 2022/11/28 10:28
 */
public class MyEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(hexString2Bytes(s));
    }

    public static byte[] hexString2Bytes(String src) {

        int l = src.length() / 2;

        byte[] ret = new byte[l];

        for (int i = 0; i < l; i++) {

            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();

        }

        return ret;

    }
}

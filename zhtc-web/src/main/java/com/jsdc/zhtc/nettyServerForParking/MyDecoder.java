package com.jsdc.zhtc.nettyServerForParking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @projectName: zhtc
 * @className: MyDecoder
 * @author: wp
 * @description:
 * @date: 2022/11/28 15:25
 */
public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //创建字节数组,buffer.readableBytes可读字节长度
        byte[] b = new byte[byteBuf.readableBytes()];
        ByteBufUtil.hexDump(byteBuf);
//复制内容到字节数组b
        byteBuf.readBytes(b);
//字节数组转字符串
        String str = new String(b);

        list.add(toHexString1(b));
    }


    public static String toHexString1(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }

    public static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s.toUpperCase();
        } else {
            return s.toUpperCase();
        }
    }
}

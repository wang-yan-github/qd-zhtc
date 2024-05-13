package com.jsdc.zhtc.nettyServerForParking;

import com.alibaba.fastjson.JSONObject;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @projectName: zhtc
 * @className: BootNettyChannelInboundHandlerAdapter
 * @author: wp
 * @description:
 * @date: 2022/11/25 19:51
 */
@Component
@Slf4j
public class BootNettyChannelInboundHandlerAdapterParking extends ChannelInboundHandlerAdapter {

    public static Integer count = 1;

    /**
     * 从客户端收到新的数据时，这个方法会在收到消息时被调用
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            String body = msg.toString();
            String[] cmds = body.split("\r\n");
            for (String cmd : cmds) {
                if (StringUtils.isNotEmpty(cmd)) {
                    System.out.println("接收客户端数据:" + cmd);
                    JSONObject jsonObject = JSONObject.parseObject(cmd);
                    String name = jsonObject.getString("name");
                    switch (name) {
                        case "connect":
                            dealConnect(ctx, jsonObject);
                            break;
                        case "heartbeat":
                            heartbeat(ctx, jsonObject);
                            break;

                    }
                }
            }
            /*System.out.println("接收客户端数据:" + body);
            JSONObject jsonObject = JSONObject.parseObject(body);
            String name = jsonObject.getString("name");

            switch (name){
                case "connect":
                    dealConnect(ctx, jsonObject);
                    break;
                case "heartbeat":
                    heartbeat(ctx, jsonObject);
                    break;

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dealConnect(ChannelHandlerContext ctx, JSONObject msg) {
        JSONObject input = JSONObject.parseObject(msg.getString("input"));
        String parkid = input.getString("park_id");
        String channelid = input.getString("channel_id");
        String tcpid = ctx.channel().id().asShortText();
        String key = parkid + "_" + channelid + "_" + tcpid;
        GlobalData.CHANNEL_MAP.put(key, ctx.channel());
        JSONObject result = new JSONObject();
        result.put("name", "connect");
        JSONObject output = new JSONObject();
        output.put("park_id", parkid);
        output.put("channel_id", channelid);
        output.put("result", "SUCCESS");
        result.put("output", output);
        System.out.println("返回链接结果：" + result.toJSONString() + "\r\n\r\n");
        ctx.writeAndFlush(result.toJSONString() + "\r\n\r\n");
    }

    private void heartbeat(ChannelHandlerContext ctx, JSONObject msg) {
        String tcpid = ctx.channel().id().asShortText();
        System.out.println(">>>收到心跳：" + msg.toJSONString() + " 通道号：" + tcpid);
    }


    /**
     * 从客户端收到新的数据、读取完成时调用
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
        ctx.flush();
    }

    /**
     * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
        log.info("netty通信异常捕获：");
        cause.printStackTrace();
        ctx.close();//抛出异常，断开与客户端的连接
    }

    /**
     * 客户端与服务端第一次建立连接时 执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelActive(ctx);
        ctx.channel().read();
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        //此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
        log.info("channelActive:" + clientIp);
    }

    /**
     * 客户端与服务端 断连时 执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelInactive(ctx);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        Map<String, Channel> temp = new HashMap<>();
        temp = GlobalData.CHANNEL_MAP;
        ctx.close(); //断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机
        Iterator<Map.Entry<String, Channel>> it = GlobalData.CHANNEL_MAP.entrySet().iterator();
        String channelId = ctx.channel().id().asShortText();
        while (it.hasNext()) {
            Map.Entry<String, Channel> entry = it.next();
            if (entry.getKey().contains(channelId))
                it.remove();//使用迭代器的remove()方法删除元素
        }
//        for(Map.Entry<String, Channel> entry : temp.entrySet()){
//            String key = entry.getKey();
//            String channelId = ctx.channel().id().asShortText();
//            if(key.contains(channelId)){
//                GlobalData.CHANNEL_MAP.remove(key);
//            }
//        }
        log.info("channelInactive:" + clientIp);
    }

    /**
     * 服务端当read超时, 会调用这个方法
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception, IOException {
        super.userEventTriggered(ctx, evt);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        ctx.close();//超时时断开连接
        log.info("userEventTriggered:" + clientIp);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("channelWritabilityChanged");
    }
}

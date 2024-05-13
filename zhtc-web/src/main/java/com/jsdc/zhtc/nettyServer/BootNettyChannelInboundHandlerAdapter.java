package com.jsdc.zhtc.nettyServer;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.zhtc.common.constants.GlobalData;
import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.guidResolve.BangAnMessage;
import com.jsdc.zhtc.guidResolve.HexUtil;
import com.jsdc.zhtc.guidResolve.LesShowYQUtil;
import com.jsdc.zhtc.model.GuidTask;
import com.jsdc.zhtc.service.GuidTaskService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
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
public class BootNettyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

    private GuidTaskService guidTaskService = SpringUtil.getBean(GuidTaskService.class);

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
            String res = msg.toString();
            String length = res.substring(8, 16);
            String l1 = length.substring(6, 8) + length.substring(4, 6) + length.substring(2, 4) + length.substring(0, 2);
            Integer x = Integer.parseInt(l1, 16);
            res = res.substring(0, x * 2);
            BangAnMessage bangAnMessage = new BangAnMessage();
            bangAnMessage.setMagic(res.substring(0, 8));
            bangAnMessage.setFrameLength(l1);
            bangAnMessage.setProtocolVersion(res.substring(16, 18));
            bangAnMessage.setProtocolType(res.substring(18, 20));
            bangAnMessage.setEncryptionType(res.substring(20, 22));
            bangAnMessage.setEncryptionSeed(res.substring(22, 24));
            bangAnMessage.setTextLength(res.substring(24, 32));
            bangAnMessage.setBinaryLength(res.substring(32, 40));
            bangAnMessage.setReserved(res.substring(40, 56));
            bangAnMessage.setMessageSequence(res.substring(56, 60));
            bangAnMessage.setDataCheckSum(res.substring(60, 62));
            bangAnMessage.setHeaderCheckSum(res.substring(62, 64));
            String data = "";
            byte[] bytes = HexUtil.hexString2Bytes(bangAnMessage.getEncryptionSeed());
            byte[] mode = HexUtil.hexString2Bytes(bangAnMessage.getEncryptionType());
            if (res.length() > 64) {
                data = res.substring(64);
                String ret = LesShowYQUtil.decode(data, Integer.parseInt(bangAnMessage.getEncryptionType()), bytes[0]);
                log.info("消息解码：" + ret);
                bangAnMessage.setData(ret);
            }
            if (StringUtils.isBlank(bangAnMessage.getData())) {
                heartBeat(ctx);
            } else {
                JSONObject jsonObject = null;
                try {
                    jsonObject = JSON.parseObject(bangAnMessage.getData());
                } catch (Exception e) {
                    log.error("消息解析异常：" + e.getMessage());
                    return;
                }

                //登录
                if (StringUtils.equals("Login", jsonObject.getString("name"))) {
                    log.info("收到登录消息： " + jsonObject);
                    JSONObject obj = jsonObject.getJSONObject("input");
                    String pid = obj.getString("pid");
                    String channelId = ctx.channel().id().asShortText();
                    String key = pid + "_" + channelId;
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>pid: " + pid);
                    if (!GlobalData.CHANNEL_MAP.containsKey(key)) {
                        GlobalData.CHANNEL_MAP.put(key, ctx.channel());
                    }
                    String resData = "{\"name\":\"Login\",\"output\":{\"action\":\"auth\",\"delay\":\"60\"}}";
                    String r = LesShowYQUtil.pushData(resData, "0100", mode[0], bangAnMessage.getEncryptionSeed());
                    ctx.write(r);
                } else if (StringUtils.equals("downloadFileFromURL", jsonObject.getString("name"))) {
                    log.info("下载文件： " + jsonObject);
                    if (jsonObject.containsKey("output")) {
                        //播放成功
                        for (Map.Entry<String, Channel> entry : GlobalData.CHANNEL_MAP.entrySet()) {
                            String key = entry.getKey();
                            String channelId = ctx.channel().id().asShortText();
                            if (key.contains(channelId)) {
                                String pid = key.substring(0, key.indexOf("_"));
                                GuidTask guidTask = guidTaskService.selectOne(Wrappers.<GuidTask>lambdaQuery().eq(GuidTask::getIs_del, GlobalData.ISDEL_NO).ne(GuidTask::getPlay_state, 2).eq(GuidTask::getPid, pid));
                                guidTask.setDownload_state(2);
                                guidTask.setUpdate_time(new Date());
                                guidTaskService.updateById(guidTask);
                            }
                        }

                    }

                } else if (StringUtils.equals("queryDownloadProgress", jsonObject.getString("name"))) {
                    log.info("查询下载进度： " + jsonObject);
                } else if (StringUtils.equals("play", jsonObject.getString("name"))) {
                    log.info("播放：" + jsonObject);
                    if (jsonObject.containsKey("output")) {
                        //播放成功
                        for (Map.Entry<String, Channel> entry : GlobalData.CHANNEL_MAP.entrySet()) {
                            String key = entry.getKey();
                            String channelId = ctx.channel().id().asShortText();
                            if (key.contains(channelId)) {
                                String pid = key.substring(0, key.indexOf("_"));
                                GuidTask guidTask = guidTaskService.selectOne(Wrappers.<GuidTask>lambdaQuery().eq(GuidTask::getIs_del, GlobalData.ISDEL_NO).ne(GuidTask::getPlay_state, 2).eq(GuidTask::getPid, pid));
                                guidTask.setPlay_state(2);
                                guidTask.setPlay_result(jsonObject.toJSONString());
                                guidTask.setUpdate_time(new Date());
                                guidTaskService.updateById(guidTask);
                            }
                        }

                    }
                } else {
                    log.info("非登录消息： " + jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 心跳
     *
     * @param ctx
     */
    private void heartBeat(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        log.info("收到心跳: " + clientIp);

    }

    /**
     * 设置语言
     *
     * @param ctx
     */
    private void setLanguage(ChannelHandlerContext ctx) {
        String cmd = "{\"name\":\"setLanguage\",\"input\":{\"language\":\"zh_CN\"}}";
        String r = LesShowYQUtil.pushData(cmd, "0200", (byte) 0X01, "23");
        ctx.write(r);
    }

    /**
     * 获取控制卡属性
     *
     * @param ctx
     */
    private void getProperty(ChannelHandlerContext ctx) {
        String cmd = "{\"name\":\"getProperty\",\"input\":{\"controllertype\":\"\",\"screenonoffstatus\":\"\",\"firmwareversion\":\"\"}}";
        String r = LesShowYQUtil.pushData(cmd, "0200", (byte) 0X01, "23");
        ctx.write(r);
    }

    /**
     * 查询文件信息
     *
     * @param ctx
     */
    private void findFile(ChannelHandlerContext ctx) {
        String cmd = "{\"name\":\"findFile\",\"input\":{\"delay\":\"0\",\"items\":[]}}";
        String r = LesShowYQUtil.pushData(cmd, "0200", (byte) 0X01, "23");
        ctx.write(r);
    }

    /**
     * 播放节目
     *
     * @param ctx
     */
    private void play(ChannelHandlerContext ctx) {
        String cmd = "{\"name\":\"UpdateDynamic\",\"input\":{\"immediatelyPlay\":\"0\",\"cover\":\"0\",\"dynamics\":[{\"id\":\"0\",\"xCoord\":\"0\",\"yCoord\":\"0\",\"width\":\"80\",\"height\":\"40\",\"transparency\":\"100\",\"relativeProgram\":\"\",\"runMode\":\"0\",\"updateFrequency\":\"\",\"unit\":[{\"type\":\"Text\",\"order\":\"1\",\"stuntType\":\"1\",\"stuntSpeed\":\"1\",\"stayTime\":\"5\",\"content\":\"aGVsbG8Kd29ybGQ=\",\"bgColor\":\"0xFF000000\",\"fontSize\":\"16\",\"fontName\":\"SimSun\",\"fontSizeType\":\"0\",\"fontColor\":\"0xFFFFFFFF\",\"alignment\":\"0\"}]}]}}";
        String r = LesShowYQUtil.pushData(cmd, "0200", (byte) 0X01, "23");
        ctx.write(r);
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
        ctx.close(); //断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机

        Iterator<Map.Entry<String, Channel>> it = GlobalData.CHANNEL_MAP.entrySet().iterator();
        String channelId = ctx.channel().id().asShortText();
        while (it.hasNext()) {
            Map.Entry<String, Channel> entry = it.next();
            if (entry.getKey().contains(channelId))
                it.remove();//使用迭代器的remove()方法删除元素
        }

//        for(Map.Entry<String, Channel> entry : GlobalData.CHANNEL_MAP.entrySet()){
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

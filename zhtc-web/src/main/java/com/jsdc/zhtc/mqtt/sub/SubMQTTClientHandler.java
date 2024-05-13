package com.jsdc.zhtc.mqtt.sub;

import com.jsdc.zhtc.mqtt.utils.MessageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPingReq;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SubMQTTClientHandler extends SimpleChannelInboundHandler<Object> {
    private static Logger logger = LoggerFactory.getLogger(SubMQTTClientHandler.class);

    private String userName = "eparkingpartner";

    private String passWord = "jiangsu_huizeit_d6b47f42";

    ScheduledFuture<?> scheduledFuture;//取消客户端心跳用

    private static AtomicBoolean firstPingResp = new AtomicBoolean(true);//第一次ping通
    //    //Qos=0的待订阅的话题
//    LinkedBlockingDeque<SubscribeMessage> zeroQosSubQueue=new LinkedBlockingDeque<>();
    //Qos=1的待订阅的话题
    LinkedBlockingDeque<MqttSubscribeMessage> oneQosSubQueue = new LinkedBlockingDeque<>();
    //Qos=1的订阅话题的ack
    LinkedBlockingDeque<MqttSubAckMessage> oneQosSubAckQueue = new LinkedBlockingDeque<>();
    //已经发送订阅请求的map key为messageId value为该SubscribeMessage【用于处理Qos=1下，是否成功收到订阅成功ack】
    ConcurrentHashMap<Integer, MqttSubscribeMessage> alreadySendMap = new ConcurrentHashMap<>();
    //已经成功订阅的话题【本地打印查看】
    LinkedBlockingDeque<MqttSubscribeMessage> alreadySubQueue = new LinkedBlockingDeque<>();

    ExecutorService executorService;

    List<java.util.concurrent.Future> FutureList = new ArrayList<>();
    String topic = "MQTT Examples";
    private Channel channel;

    private Boolean running = new Boolean(true);
    String broker = "tcp://127.0.0.1:12345";
    String clientId = "guest1";
    MemoryPersistence persistence = new MemoryPersistence();

    public SubMQTTClientHandler() throws MqttException {
        //subscribeTopics("MQTT Examples");
        //final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //订阅Qos=1的话题
        java.util.concurrent.Future<?> future2 = executorService.submit(() -> {
            while (running) {
                while (!oneQosSubQueue.isEmpty()) {
                    MqttSubscribeMessage subscribeMessage = oneQosSubQueue.removeFirst();
                    Integer messageID = subscribeMessage.variableHeader().messageId();
                    //还没订阅过该topic消息,需要加入map
                    if (!alreadySendMap.containsKey(messageID)) {
                        alreadySendMap.put(messageID, subscribeMessage);
                        channel.writeAndFlush(subscribeMessage);
//                        org.eclipse.paho.client.mqttv3.MqttMessage message = new org.eclipse.paho.client.mqttv3.MqttMessage("abc".getBytes());
//                        try {
//                            sampleClient.publish(topic, message);
//                        } catch (MqttException e) {
//                            e.printStackTrace();
//                        }
                        //System.out.println("subscribeMessage {} send to server success id {}, qos {}", subscribeMessage.subscriptions().get(0).getTopicFilter(),subscribeMessage.getMessageID(),subscribeMessage.getQos());
                    }
                    oneQosSubQueue.addLast(subscribeMessage);//此时还没成功收到服务端订阅成功的ack，不能移除该消息【在另一个线程中判断是否收到对应的ack来决定是否移除】
                    try {
                        //防止发送过快
                        SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //接受Qos=1订阅话题的ack
        java.util.concurrent.Future<?> future3 = executorService.submit(() -> {
            while (running) {
                while (!oneQosSubAckQueue.isEmpty()) {
                    MqttSubAckMessage subAckMessage = oneQosSubAckQueue.removeFirst();
                    if (alreadySendMap.containsKey(subAckMessage.variableHeader().messageId())) {
                        //加入订阅成功队列
                        alreadySubQueue.add(alreadySendMap.get(subAckMessage.variableHeader().messageId()));
                        //收到订阅成功ack，需要从请求订阅队列移除该订阅消息
                        oneQosSubQueue.remove(alreadySendMap.get(subAckMessage.variableHeader().messageId()));
                        alreadySendMap.remove(subAckMessage.variableHeader().messageId());
                    } else {
                        logger.error("receive error SubAckMessage {}",
                                subAckMessage.variableHeader().messageId());
                    }
                }
                try {
                    //防止运行过快
                    MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //打印成功订阅的话题
        executorService.submit(() -> {
            while (running) {
                if (!alreadySubQueue.isEmpty()) {
                    //System.out.println("alreadySubQueue queue size {}",alreadySubQueue.size());
                    alreadySubQueue.forEach((p) -> {
                        //System.out.println("already sub topic is "+p.payload().topicSubscriptions().get(0).getTopicFilter());
                    });
                }
                try {
                    //防止运行过快
                    SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

//        FutureList.add(future1);
        FutureList.add(future2);
        FutureList.add(future3);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("error message {}", cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client " + ctx.channel().remoteAddress() + " active");
        MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader("MQTT",
                MqttVersion.MQTT_3_1_1.protocolLevel(),
                true, true,
                false, 0, false,
                true, 10000);
        MqttConnectPayload payload = new MqttConnectPayload("1", "", "".getBytes(), userName, passWord.getBytes());
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 2);
        MqttConnectMessage connectMessage = new MqttConnectMessage(fixedHeader, variableHeader, payload);
        ctx.writeAndFlush(connectMessage);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("channelInactive " + ctx.channel().remoteAddress());
        boolean cancel = scheduledFuture.cancel(true);
        // System.out.println("{} client canal heart beat {}",ctx.channel().localAddress(),cancel);
        ctx.close(ctx.newPromise().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                //System.out.println("channel {} close ",ctx.channel().localAddress());
            }
        }));
        if (this.channel != null) {
            channel = null;
        }
        //
        running = false;
        //
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msgx) throws Exception {
        if (msgx == null) {
            return;
        }
        MqttMessage msg = (MqttMessage) msgx;
        //NettyLog.debug("read: {}", msg.fixedHeader().messageType());
        MqttFixedHeader mqttFixedHeader = msg.fixedHeader();
        switch (mqttFixedHeader.messageType()) {
            case CONNACK:
                processConnectAckResp(ctx, msg);
                break;
            case UNSUBACK:
                //clientProtocolProcess.processUnSubBack(ctx.channel(), msg);
                break;
            case PUBLISH:
                processPublish(ctx, msg);
                break;
            case PUBACK:
                //clientProtocolProcess.processPubAck(ctx.channel(), msg);
                break;
            case PUBREC:
                //clientProtocolProcess.processPubRec(ctx.channel(), msg);
                break;
            case PUBREL:
                //clientProtocolProcess.processPubRel(ctx.channel(), msg);
                break;
            case PUBCOMP:
                //clientProtocolProcess.processPubComp(ctx.channel(), msg);
                break;
            case SUBACK:
                processSubAck(ctx, msg);
                break;
            case PINGRESP:
                processPingResp();
            default:
                break;
        }
    }

    /**
     * 收到服务端发来的该客户端话题订阅成功ack
     *
     * @param ctx
     * @param msg
     */
    private void processSubAck(ChannelHandlerContext ctx, Object msg) {
        MqttSubAckMessage subAckMessage = (MqttSubAckMessage) msg;
        if (!oneQosSubAckQueue.contains(subAckMessage)) {
            oneQosSubAckQueue.addLast(subAckMessage);
            logger.error("receive SubAckMessage from server id {}", subAckMessage.variableHeader().messageId());
        } else {
            logger.error("receive error SubAckMessage from server,id {}", subAckMessage.variableHeader().messageId());
        }
    }

    /**
     * 收到其他客户端发布的消息-由服务端发送来
     *
     * @param ctx
     * @param msg
     */
    private void processPublish(ChannelHandlerContext ctx, Object msg) {
        MqttPublishMessage publishMessage = (MqttPublishMessage) msg;
        MqttQoS qos = publishMessage.fixedHeader().qosLevel();
        String topic = publishMessage.variableHeader().topicName();
        System.out.println("收到消息：" + new String(MessageUtil.readBytesFromByteBuf(publishMessage.payload())));
        //System.out.println("receive topic from other client topic {} payload [{}]",publishMessage.variableHeader().topicName(),new String(publishMessage.payload().array()));
    }

    /**
     * 发布的topic Qos=1时会收到pubAck
     *
     * @param ctx
     * @param msg
     */
    private void processPubAck(ChannelHandlerContext ctx, Object msg) {
        MqttPubAckMessage pubAckMessage = (MqttPubAckMessage) msg;
        //boolean result = oneQosSubAckQueue.offerLast(pubAckMessage);
//        if(result==true){
//            System.out.println("receive PubAckMessage {} from MQTTServer,and insert queue success",pubAckMessage.getMessageID());
//        }else{
//            logger.error("failed insert PubAckMessage {} into queue",pubAckMessage.getMessageID());
//        }
    }

    //第一次接收到服务端pingResp后才会发布主题或者订阅主题
    private void processPingResp() {
        if (firstPingResp.get()) {
            logger.debug("first receive ping response , start subscribe topic");
            /**
             *   qos=1只能是1，SubscribeMessage构造器设置
             */
            subscribeTopics(topic);
            subscribeTopics("/service/plateinfo/v1");
            //subscribeTopics("mqttClient/topicTwo3");
            //subscribeTopics("mqttClient/topicTwo4");
            firstPingResp.set(false);
        } else {
            logger.debug("ping response");
        }
    }

    /**
     * 订阅话题【目前Qos支持1】  这里我们默认只能订阅一个话题
     */
    private void subscribeTopics(String topic) {
        MqttMessageBuilders.SubscribeBuilder builder = MqttMessageBuilders.subscribe();
        /**
         *   只能是qos=1，SubscribeMessage构造器设置
         */
        builder.addSubscription(MqttQoS.AT_MOST_ONCE, topic);
        // todo
        MqttSubscribeMessage message = builder.messageId(123).build();
        boolean offer = oneQosSubQueue.offer(message);

    }

//    /**
//     * 发布主题【Qos目前只支持0、1】
//     * @param topic
//     * @param qos
//     */
//    private void publishTopic(String topic,AbstractMessage.QOSType qos) {
//        //这个字段需要设置，否者无法发送消息
//        PublishMessage publishMessage = new PublishMessage();
//        publishMessage.setTopicName(topic);
//        publishMessage.setQos(qos);
//        int random = new Random().nextInt(10000);//<2^32-1
//        /**
//         *   qos=0情况下服务端接收到数据后MessageID会被设置为null【应该是编解码的时候被设置】
//         */
//        publishMessage.setMessageID(random);
//        publishMessage.setRetainFlag(false);
//        publishMessage.setPayload(ByteBuffer.wrap("I am payload".getBytes()));
//        if(qos== AbstractMessage.QOSType.MOST_ONE){
//            boolean offer = zeroQosPubQueue.offer(publishMessage);
//            if(offer){
//                System.out.println("publishMessage {} insert zeroQosPubQueue success ",publishMessage.getTopicName());
//            }
//        }else if(qos== AbstractMessage.QOSType.LEAST_ONE){
//            boolean offer = oneQosSubQueue.offer(publishMessage);
//            if(offer){
//                System.out.println("publishMessage {} insert oneQosSubQueue success ",publishMessage.getTopicName());
//            }
//        }
//    }

    private void processConnectAckResp(ChannelHandlerContext ctx, Object msg) {
        MqttConnAckMessage ackMessage = (MqttConnAckMessage) msg;
        if (ackMessage.variableHeader().connectReturnCode() == MqttConnectReturnCode.CONNECTION_ACCEPTED) {
            channel = ctx.channel();
            System.out.println("get connectAck success, start heart beat");
            //存
//            ctx.channel().attr(MyConstants.pingKey).setIfAbsent("firstPing");
            // 定时心跳任务
            scheduledFuture = ctx.channel().eventLoop().scheduleAtFixedRate(() -> {
                MqttPingReq pingReqMessage = new MqttPingReq();
                MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
                MqttMessage message = new MqttMessage(mqttFixedHeader);
                ctx.writeAndFlush(message, ctx.newPromise().addListener((future) -> {
                    if (future.cause() == null) {
                        System.out.println("send heartBeat success");
                    } else {
                        logger.error("send heartBeat error");
                    }
                }));
            }, 2, 55, SECONDS);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }
}

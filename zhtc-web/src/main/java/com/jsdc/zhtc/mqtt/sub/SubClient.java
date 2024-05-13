package com.jsdc.zhtc.mqtt.sub;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.logging.LoggingHandler;

public class SubClient {
    public static void main(String[] args) {
        SubClient.startMqttClient();
    }

    public static void startMqttClient() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast("log", new LoggingHandler());
                    pipeline.addLast("decoder", new MqttDecoder());//解码
                    pipeline.addLast("encoder", MqttEncoder.INSTANCE);//编码
                    pipeline.addLast("handler", new SubMQTTClientHandler());
                }
            });
            ChannelFuture ch = b.connect("clouddev.parkingquickly.com", 1883).sync();
            ch.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            group.shutdownGracefully();
        }
    }
}

package com.jsdc.zhtc.nettyServer;


import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.springframework.stereotype.Component;

/**
 * @projectName: zhtc
 * @className: BootNettyChannelInitializer
 * @author: wp
 * @description:
 * @date: 2022/11/25 19:50
 */
@Component
public class BootNettyChannelInitializer<SocketChannel> extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {

        // ChannelOutboundHandler，依照逆序执行
        ch.pipeline().addLast("encoder", new MyEncoder());

        // 属于ChannelInboundHandler，依照顺序执行
        ch.pipeline().addLast("decoder", new MyDecoder());
        /**
         * 自定义ChannelInboundHandlerAdapter
         */
        ch.pipeline().addLast(new BootNettyChannelInboundHandlerAdapter());

    }

}

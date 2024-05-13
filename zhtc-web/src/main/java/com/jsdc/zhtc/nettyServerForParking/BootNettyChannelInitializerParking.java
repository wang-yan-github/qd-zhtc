package com.jsdc.zhtc.nettyServerForParking;


import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

/**
 * @projectName: zhtc
 * @className: BootNettyChannelInitializer
 * @author: wp
 * @description:
 * @date: 2022/11/25 19:50
 */
@Component
public class BootNettyChannelInitializerParking<SocketChannel> extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {

        // ChannelOutboundHandler，依照逆序执行
        //ch.pipeline().addLast("encoder", new MyEncoder());

        // 属于ChannelInboundHandler，依照顺序执行
        //ch.pipeline().addLast("decoder", new MyDecoder());
        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
        /**
         * 自定义ChannelInboundHandlerAdapter
         */
        ch.pipeline().addLast(new BootNettyChannelInboundHandlerAdapterParking());

    }

}

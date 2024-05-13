package com.jsdc.zhtc.nettyServer;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @projectName: zhtc
 * @className: nettyApplication
 * @author: wp
 * @description:
 * @date: 2022/11/28 14:59
 */
@Component
public class NettyApplication implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                new Thread(() -> {
                    try {
                        BootNettyServer.getInstance().bind(8889);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                System.out.println("---------------------netty 8889启动成功-------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package com.jsdc.zhtc.nettyServerForParking;

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
public class NettyApplicationParking implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                new Thread(() -> {
                    try {
                        BootNettyServerParking.getInstance().bind(8890);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                System.out.println("---------------------netty 8890启动成功-------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

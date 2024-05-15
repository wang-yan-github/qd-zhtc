package com.jsdc.zhtc.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @projectName: zhtc
 * @className: MqttApplication
 * @author: wp
 * @description:
 * @date: 2023/7/7 14:52
 */
@Component
public class MqttApplication  implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    InitApplication initApplication;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // todo
        //线上环境只在21上连接MQTT服务，部署其他服务器时注释以下代码
        /*if(event.getApplicationContext().getParent() == null) {
            try {
                new Thread(() -> {
                    try {
                        initApplication.init();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                System.out.println("---------------------MQTT 连接成功-------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}

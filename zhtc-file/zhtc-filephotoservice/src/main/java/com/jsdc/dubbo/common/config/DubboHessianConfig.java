package com.jsdc.dubbo.common.config;

import com.alibaba.dubbo.config.ProtocolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboHessianConfig {

    @Bean("hessian")
    public ProtocolConfig restProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("hessian");
        protocolConfig.setId("hessian");
        protocolConfig.setServer("jetty");
        protocolConfig.setPort(8283);
        protocolConfig.setAccepts(500);
        protocolConfig.setThreads(100);
        return protocolConfig;
    }
}

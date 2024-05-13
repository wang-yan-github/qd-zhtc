package com.jsdc.zhtc.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @projectName: zhtc
 * @className: ThreadPoolConfig
 * @author: wp
 * @description: 全局线程池配置
 * @date: 2022/12/15 9:21
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {
    @Bean("orderThreadPool")
    public Executor orderThreadPool() {
        //机器核数
        int cpuNum = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cpuNum);
        executor.setMaxPoolSize(cpuNum * 2);
        executor.setQueueCapacity(1024);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("pool-thread-");
        executor.initialize();
        return executor;
    }
}

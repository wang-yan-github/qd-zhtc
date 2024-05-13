package com.jsdc.zhtc.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jsdc.zhtc.mapper")
public class MybatisPlusConfig {

    // @Bean
    // public MybatisPlusInterceptor mybatisPlusInterceptor() {
    //     MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    //     interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.SQL_SERVER2005));
    //     return interceptor;
    // }
}

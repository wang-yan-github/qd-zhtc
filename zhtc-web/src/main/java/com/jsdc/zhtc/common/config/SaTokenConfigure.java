package com.jsdc.zhtc.common.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {
            if (!"OPTIONS".equals(req.getMethod())) {
                if (StpUtil.getLoginIdByToken(StpUtil.getTokenValue()) == null ? false : true) {
                    SaRouter.match("/**", "/login.do");
                } else {
                    SaRouter.match("/**", "/login.do").back(SaResult.CODE_ERROR);
                }
            }
//            SaHolder.getResponse().redirect("/login.do");
//            SaRouter.back();
        })).excludePathPatterns("/**/**");
    }

    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();
        // token名称 (同时也是cookie名称)
        config.setTokenName("Authorization");
        // token风格
        config.setTokenStyle("token-style=random-128");
        // token有效期，单位s 默认30天，不支持自动续签
        config.setTimeout(30 * 60 * 60 * 24);
        // token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒，支持自动续签
//        config.setActivityTimeout((30));
        // 自动续签，指定时间内有操作，则会自动续签
        config.setAutoRenew(true);
        // 是否尝试从header里读取token
        config.setIsReadHead(true);
        // 是否尝试从cookie里读取token
        config.setIsReadCookie(false);
        // 是否尝试从请求体里读取token
        config.setIsReadBody(true);
        // 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
        config.setIsConcurrent(true);
        // 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
        config.setIsShare(false);
        // 是否在初始化配置时打印版本字符画
        config.setIsPrint(true);
        // 是否输出操作日志
        config.setIsLog(true);
        return config;
    }
}

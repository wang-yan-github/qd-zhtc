//package com.jsdc.zhtc.common.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//@Configuration
//public class ErrorConfigurer implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration registration = registry.addInterceptor(new ErrorInterceptor());
//        registration.addPathPatterns("/**");
//        //registration.excludePathPatterns("/ipRedirect.do","/libs/**","/static/**","/login.do");
//    }
//
//}

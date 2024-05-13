package com.jsdc.zhtc.common.aop.logaop;

import com.jsdc.zhtc.enums.LogEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogInfo {
    LogEnums value();
}

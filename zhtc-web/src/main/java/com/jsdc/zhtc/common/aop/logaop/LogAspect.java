package com.jsdc.zhtc.common.aop.logaop;

import com.jsdc.zhtc.common.utils.StringUtils;
import com.jsdc.zhtc.model.SysLog;
import com.jsdc.zhtc.model.SysUser;
import com.jsdc.zhtc.service.SysLogService;
import com.jsdc.zhtc.service.SysUserService;
import com.jsdc.zhtc.vo.ResultInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName LogAspect
 * @Description TODO
 * @Author xujian
 * @Date 2022/1/4 11:48
 * @Version 1.0
 */

@Aspect
@Component
public class LogAspect {
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private SysUserService sysUserService;

    @Pointcut(value = "@annotation(com.jsdc.zhtc.common.aop.logaop.LogInfo)")
    public void logPointcut() {

    }


    @AfterReturning(value = "logPointcut()", returning = "resultInfo")
    public void saveLoginfo(JoinPoint joinPoint, ResultInfo resultInfo) throws Exception {
        if (resultInfo.getCode() == 0 && StringUtils.isNotEmpty(resultInfo.getLogMsg())) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            LogInfo annotation = signature.getMethod().getAnnotation(LogInfo.class);
            SysLog sysLog = new SysLog();
            sysLog.setLog_type(annotation.value().getValue());
            sysLog.setLog_content(resultInfo.getLogMsg());
            sysLog.setOperation_time(new Date());
            sysLog.setIs_del("0");
            sysLog.setCreate_time(new Date());
            //用户信息
            SysUser sysUser = sysUserService.getUser();
            sysLog.setCreate_user(sysUser.getId());
            sysLog.setCreate_time(new Date());
            sysLog.setUser_type("1");
            //获取request信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            sysLog.setIp(request.getRemoteAddr());
            sysLogService.insert(sysLog);
        }

    }
}

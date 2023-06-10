package com.Idcmdzx.aspect;

import com.Idcmdzx.annotation.SystemLog;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.Idcmdzx.annotation.SystemLog)")
    public void pt(){};

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        try {
            handleBefore(joinPoint);
           res = joinPoint.proceed();
            handleAfter();
        }finally {
            log.info("==========End=========="+System.lineSeparator());
        }
        return res;
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("==========start==========");
        log.info("URL            :{}",request.getRequestURI());
        log.info("BusinessName   :{}",systemLog.businessName());
        log.info("HTTP Method    :{}",request.getMethod());
        log.info("Class Method   :{},{}",joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature)joinPoint.getSignature()).getName());
        log.info("IP             :{}",request.getRemoteHost());
        log.info("Request Args   :{}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SystemLog annotation = signature.getMethod().getAnnotation(SystemLog.class);
        return annotation;
    }

    private void handleAfter() {
    }

}

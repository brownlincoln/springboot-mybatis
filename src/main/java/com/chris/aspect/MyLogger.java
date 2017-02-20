package com.chris.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by YaoQi on 2017/2/16.
 */
/*@Aspect
@Component
public class MyLogger {
    Logger log = LoggerFactory.getLogger(MyLogger.class);

    @Before("execution(* com.chris.service.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg + "|");
        }
        log.info("before method" + sb.toString());
    }
}*/

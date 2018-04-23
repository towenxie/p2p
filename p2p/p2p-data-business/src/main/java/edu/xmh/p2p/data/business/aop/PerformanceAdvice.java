package edu.xmh.p2p.data.business.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class PerformanceAdvice {

    @Pointcut("execution(* edu.xmh.p2p.data.business.service.server.*.*(..))")
    public void eventPointCut() {}
    
    @Around("eventPointCut()")   
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {   
        final Logger logger = LogManager.getLogger(pjp.getSignature().getDeclaringType().getName());  
        StopWatch sw = new StopWatch(getClass().getSimpleName());  
        try {   
            sw.start(pjp.getSignature().getName());  
            return pjp.proceed();   
        } finally {   
            sw.stop();  
            logger.debug(sw.prettyPrint());   
        }   
    } 
}
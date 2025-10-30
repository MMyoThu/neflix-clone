package com.mta.core.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Simple logging aspect for all service methods
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.mta.core.domain.service..*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        log.info("Starting method: {}.{}", className, methodName);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.info("Completed method: {}.{} in {} ms", className, methodName, executionTime);
            return result;
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.error("Failed method: {}.{} after {} ms - Error: {}", 
                    className, methodName, executionTime, e.getMessage());
            throw e;
        }
    }
}

package com.mta.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for AOP aspects
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

    public AopConfig() {
        log.info("AOP Configuration initialized - AspectJ AutoProxy enabled");
    }
}

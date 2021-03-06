package com.emmisolutions.emmimanager.service.logging;

import com.emmisolutions.emmimanager.config.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Aspect for logging execution of service Spring components.
 */
@Aspect
@Component("ServiceLayerAutoLogger")
public class LoggingAspect {

    @Resource
    private Environment env;

    /**
     * Get all public methods in the service package
     */
    @Pointcut("execution(public * com.emmisolutions.emmimanager.service.spring..*.*(..)))")
    public void loggingPointcut() {
    }

    /**
     * Log exceptions
     *
     * @param joinPoint the join point
     * @param e         to log
     */
    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Logger log = getLogger(joinPoint);
        if (log.isDebugEnabled() && !env.acceptsProfiles(Constants.SPRING_PROFILE_TEST)){
            log.error("Exception in {}() with cause = {}", joinPoint.getSignature().getName(), e.getCause(), e);
        }
    }

    /**
     * Logging method
     *
     * @param joinPoint to log around
     * @return the original method result
     * @throws Throwable if there is an exception
     */
    @Around("loggingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = getLogger(joinPoint);
        log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {}({})", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
            throw e;
        }
    }

    private Logger getLogger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
    }

}

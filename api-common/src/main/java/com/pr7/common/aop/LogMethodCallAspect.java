/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.aop;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Aspect
public class LogMethodCallAspect {

    private static final Logger logger = LogManager.getLogger(LogMethodCallAspect.class);

    @Around(value = "@annotation(logMethodCall)", argNames = "logMethodCall")
    public Object logMethodCall(final ProceedingJoinPoint pjp, final LogMethodCall logMethodCall) throws Throwable {
        return logMethod(pjp, logMethodCall);
    }

    @Around(value = "@within(logMethodCall)")
    public Object logMethodCallForClass(final ProceedingJoinPoint pjp, final LogMethodCall logMethodCall) throws Throwable {
        LogMethodCall lg = logMethodCall;
        if (pjp.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            LogMethodCall annotation = signature.getMethod().getAnnotation(LogMethodCall.class);
            if (annotation != null) {
                lg = annotation;
            }
        }
        return logMethod(pjp, lg);
    }

    private static Object logMethod(final ProceedingJoinPoint pjp, final LogMethodCall logMethodCall) throws Throwable {
        LogMethodCall.LogLevel logLevel = logMethodCall.logLevel();
        long startTime = System.currentTimeMillis();
        String methodName = pjp.getSignature().getName();
        String methodNameFull = pjp.getSignature().toString();
        MethodSignature signature = null;
        if (pjp.getSignature() instanceof MethodSignature) {
            signature = (MethodSignature) pjp.getSignature();
        }

        if (logMethodCall.logBeginNEndMethod()) {
            logger.log(logLevel.getLevel(), "### BEGIN " + methodNameFull);
        }
        if (logMethodCall.logAgruments() && signature != null) {
            if (!ArrayUtils.isEmpty(pjp.getArgs())) {
                logger.log(logLevel.getLevel(), methodName + " Args: " + constructArgumentsString(signature.getParameterNames(), pjp.getArgs(), logMethodCall.ignoreArgs()));
            }
        }
        try {
            Object ob = pjp.proceed();

            if (logMethodCall.logReturnValue() && signature != null) {
                signature = (MethodSignature) pjp.getSignature();
                Class<?> returnType = signature.getReturnType();
                if (returnType.getName().compareTo("void") != 0) {
                    logger.log(logLevel.getLevel(), methodName + " return: " + ob);
                }
            }
            return ob;
        } finally {
            if (logMethodCall.logPerformanceTime()) {
                logger.log(logLevel.getLevel(), methodName + " spent " + (System.currentTimeMillis() - startTime) + " ms");
            }
            if (logMethodCall.logBeginNEndMethod()) {
                logger.log(logLevel.getLevel(), "### END " + methodNameFull);
            }
        }
    }

    private static String constructArgumentsString(String[] names, Object[] arguments, String[] ignoreNames) {
        StringBuilder buffer = new StringBuilder();
        if (names == null || arguments == null || arguments.length == 1) {
            if (arguments != null && arguments.length == 1) {
                buffer.append(arguments[0]);
            }
        } else {
            for (int i = 0; i < names.length; i++) {
                if (i < arguments.length && !ArrayUtils.contains(ignoreNames, names[i])) {
                    buffer = buffer.append(names[i]).append("=").append(arguments[i]).append(", ");
                }
            }
        }
        return buffer.toString();
    }
}

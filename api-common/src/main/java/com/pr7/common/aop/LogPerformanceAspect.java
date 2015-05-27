package com.pr7.common.aop;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
//@Component
public class LogPerformanceAspect {

    private static final Logger logger = Logger.getLogger(LogPerformanceAspect.class.getName());

    @Around(value = "@annotation(timeInvocation)", argNames = "timeInvocation")
    public Object recordTimeSpentMethod(final ProceedingJoinPoint pjp, final LogPerformance timeInvocation) throws Throwable {
        return recordTimeSpent(pjp);
    }

    @Around(value = "@within(timeInvocation)")
    public Object recordTimeSpentClass(final ProceedingJoinPoint pjp, final LogPerformance timeInvocation) throws Throwable {
        return recordTimeSpent(pjp);
    }

    private static Object recordTimeSpent(final ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            logger.log(Level.INFO, "{0} spent {1}ms", new Object[]{pjp.getSignature(),
                        BigDecimal.valueOf(System.nanoTime() - startTime).divide(BigDecimal.valueOf(1000000))});
        }
    }
}

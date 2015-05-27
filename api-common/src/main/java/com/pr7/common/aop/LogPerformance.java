package com.pr7.common.aop;

import java.lang.annotation.*;

/**
 * will print a log about how much time spent in the annotated method 
 * @author dapeng
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPerformance {
}

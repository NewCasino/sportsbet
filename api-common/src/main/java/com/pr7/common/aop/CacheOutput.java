package com.pr7.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * execution result of the annotated method will be cached for a period of time
 * specified by the ttl in secs
 *
 * take notice, that the input parameters of the annotated methods will be used to compute a key. 
 * for the non primitive type which the <b>toString()</b> method is used to
 * compute the key 
 * 
 * @author dapeng.liu
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CacheOutput {

   /**
    * time to live in seconds
    * @return
    */
   int ttl() default 10;
}

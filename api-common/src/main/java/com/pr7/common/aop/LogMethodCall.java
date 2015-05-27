/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.aop;

import java.lang.annotation.*;
import org.apache.log4j.Level;

/**
 * Log Method Call
 * @author 
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMethodCall {    
    boolean logBeginNEndMethod() default false;
    boolean logPerformanceTime () default false;
    boolean logAgruments () default false;
    boolean logReturnValue () default false;
    String [] ignoreArgs() default {};
    LogLevel logLevel () default LogLevel.DEBUG;
    
    public static enum LogLevel {
	DEBUG (Level.DEBUG),
	ERROR (Level.ERROR),
	FATAL (Level.FATAL),
	INFO (Level.INFO),	
	WARN (Level.WARN);
        
        Level level;

        private LogLevel(Level level) {
            this.level = level;
        }

        public Level getLevel() {
            return level;
        }        
    }
}

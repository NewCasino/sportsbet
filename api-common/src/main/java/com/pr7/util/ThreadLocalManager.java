package com.pr7.util;

public class ThreadLocalManager {
	private static final ThreadLocal<ThreadContext> userThreadLocal = new ThreadLocal<ThreadContext>();
	
	public static void unset() {
		userThreadLocal.remove();
	}
	
    public static ThreadContext getThreadContext() {
    	ThreadContext context = userThreadLocal.get();
    	if (context == null) {
    		userThreadLocal.set(new ThreadContext());
    	}
    	
    	return userThreadLocal.get();
    }  
}

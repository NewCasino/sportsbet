package com.pr7.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public final class ReflectionUtil {

    private static final Logger _logger = LogManager.getLogger(ReflectionUtil.class);

    public static String printPropertyValues(Object obj) {
        if (obj == null) {
            return "\nType: " + null;
        }

        String log = "\nType: " + obj.getClass().getSimpleName() + "\n    ";
        try {
        	if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                log += " -> size = " + list.size();
                for (Object valObj : list) {
                    log += printPropertyValues(valObj);
                }
        	} else {
        		for (Field f : obj.getClass().getDeclaredFields()) {
                	if (Modifier.isStatic(f.getModifiers())) {
                		//Ignore static field
                		continue;
                	}
                	boolean access = f.isAccessible();
            		if (!access)
            			f.setAccessible(true);
            		
                    Object val = f.get(obj);                
                    if (val instanceof List<?>) {
                        List<?> list = (List<?>) val;
                        for (Object valObj : list) {
                            log += printPropertyValues(valObj);
                        }
                    } else if (val instanceof Date) {
                        Date cast = (Date) val;
                        log += String.format("%s: %s", f.getName(), cast.toString() + ", ");
                    } else if (val instanceof Object[]) {
                        log += String.format("%s: %s", f.getName(), "[" + StringUtils.join((Object[]) val, ",") + "], ");
                    } else {
                        log += String.format("%s: %s", f.getName(), val + ", ");
                    }
                    //Revert previous access
                    f.setAccessible(access);
                }
        	}            
        } catch (Exception e) {
            _logger.error("ERROR when call printPropertyValues:: obj = " + obj, e);
        }
        return log;
    }
    
    public static String printGetMethodValues(Object obj) {
        if (obj == null) {
            return "\nType: " + null;
        }

        String log = "\nType: " + obj.getClass().getSimpleName() + "\n    ";
        try {
            for (Method method : obj.getClass().getMethods()) {
            	if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
            		boolean access = method.isAccessible();
            		if (!access)
            			method.setAccessible(true);
            		
                    Object val = method.invoke(obj);
                    if (val instanceof List<?>) {
                        List<?> list = (List<?>) val;
                        for (Object valObj : list) {
                            log += printGetMethodValues(valObj);
                        }
                    } else if (val instanceof Date) {
                        Date cast = (Date) val;
                        log += String.format("%s: %s", method.getName(), cast.toString() + ", ");
                    } else {
                        log += String.format("%s: %s", method.getName(), val + ", ");
                    }
                    //Revert previous access
                    method.setAccessible(access);            		
            	}                
            }

//			Method[] allMethods = obj.getClass().getDeclaredMethods();
//		    for (Method m : allMethods) {
//				String mname = m.getName();
//				if (!mname.startsWith("get")) {
//				    continue;
//				}
//				
//				if (m.isAccessible() && m.getGenericParameterTypes().length == 0) {
//					Object result = m.invoke(obj);
//					log += String.format("%s: %s", mname, result + ", ");
//				}
//		    }
        } catch (Exception e) {
            _logger.error("ERROR when call printPropertyValues:: obj = " + obj, e);
        }
        return log;
    }
}

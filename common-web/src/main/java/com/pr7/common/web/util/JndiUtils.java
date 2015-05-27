/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.util;

import java.util.Properties;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author mark.ma
 */
public class JndiUtils {
    
    private static final Logger logger = Logger.getLogger(JndiUtils.class.getName()); 

    /**
     * simple jndi util
     * 
     * @param jndiName
     * @param key
     * @return 
     */
    public static String getProperty(String jndiName, String key) {
        try {
            Object jndiResource = new InitialContext().lookup(jndiName);
            if (jndiResource instanceof Properties) {
                Properties prop = ((Properties) jndiResource);
                if (prop.containsKey(key)) {
                    return prop.getProperty(key);
                }
            }
        } catch (NamingException ex) {
            logger.warning(ex.getMessage());
        }
        return null;
    }
    
}

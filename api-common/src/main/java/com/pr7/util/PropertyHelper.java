package com.pr7.util;

import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertyHelper {

    private static final Logger _logger = LogManager.getLogger(PropertyHelper.class);

    public static String getResourceProperty(String fileName, String key) {
        String value = "";
        try {
            value = PropertiesLoaderUtils.loadAllProperties(fileName).getProperty(key, key);
        } catch (IOException ex) {
            _logger.debug("Can not get property with key: " + key + " in resource file: " + fileName);
        }
        return value;
    }

    public static String getProperty(String jndiName, String key) {
    	if (jndiName == null || key == null)
    		return null;
    	
        String result = getFromJndi(jndiName, key);

        if (result == null) {
            result = System.getProperty(key);
            _logger.debug("getProperty:: cannot find from JNDI, get from system property - jndiName = " + jndiName + ", key = " + key + ", result = " + result);
        }

        if (result == null) {
            result = System.getenv(key);
            _logger.debug("getProperty:: cannot find from system property, get from system env - jndiName = " + jndiName + ", key = " + key + ", result = " + result);
        }

        return result;
    }
    
    public static String getFromJndi(String jndiName, String key) {
        String result = null;
        try {
            Object jndiResource = new InitialContext().lookup(jndiName);
            if (jndiResource instanceof Properties) {
                Properties prop = ((Properties) jndiResource);
                if (prop.containsKey(key)) {
                    result = prop.getProperty(key);
                }
            }
        } catch (Exception ex) {
            _logger.debug("error when find from JNDI:: jndiName = " + jndiName + ", key = " + key);
        }
        
        return result;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Admin
 */
public abstract class IPAddressUtils {
    public static boolean isValidIP4Adress(String ip){
        return ip.matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$");
    }
    
    /*
		127.0.0.1
     	10.x.x.x
    	172.16.x.x
    	192.168.x.x
     */
    public static boolean isInternal(String ip) {
    	if (StringUtils.isEmpty(ip))
    		return false;
    		
    	return ip.equals("127.0.0.1") || ip.startsWith("10.") || ip.startsWith("172.16.") || ip.startsWith("192.168.");
    }
}

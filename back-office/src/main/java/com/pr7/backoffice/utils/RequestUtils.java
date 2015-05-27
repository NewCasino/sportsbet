/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.utils;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Admin
 */
public class RequestUtils {
    public static boolean isWebServiceRquest(HttpServletRequest request){
        String uri =  request.getRequestURI();
        return uri.indexOf("/sv/") != -1 || uri.contains("/captcha.jpg");
    }

}

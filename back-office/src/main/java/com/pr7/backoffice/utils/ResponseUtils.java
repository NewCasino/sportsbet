/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.utils;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Admin
 */
public class ResponseUtils {

    public static Map<String, Cookie> addCookies(HttpServletResponse response, String setCookies, boolean isSetDomain){
        //cookies = cookies.replaceAll(";\\s*path\\=.?;", "; path=/bwmanager/;");
        Map<String, Cookie> cookies = getCookiesFromString(setCookies, isSetDomain);
        for (Cookie c : cookies.values()) {
            c.setPath("/");
            response.addCookie(c);
        }
        return cookies;
    }

    public static Map<String, Cookie> getCookiesFromString(String setCookies, boolean isSetDomain) {
        Map<String, Cookie> cookies = new HashMap<String, Cookie>();
        if (setCookies == null) {
            setCookies = "";
        }
        String[] setCookiesArray = setCookies.split(";\\s*");
        Cookie cookie = null;
        for (String strCookie : setCookiesArray) {
            if (StringUtils.isNotBlank(strCookie)) {
                String[] pair = strCookie.split("=");
                if ("secure".equalsIgnoreCase(pair[0]) && cookie != null) {
                    cookie.setSecure(true);
                } else if ("expires".equalsIgnoreCase(pair[0]) && cookie != null) {
                    //cookie.getMaxAge()
                } else if ("domain".equalsIgnoreCase(pair[0]) && cookie != null && isSetDomain) {
                    cookie.setDomain(pair[1]);
                } else if ("path".equalsIgnoreCase(pair[0]) && cookie != null) {
                    cookie.setPath(pair[1]);
                } else if ("HttpOnly".equalsIgnoreCase(pair[0]) && cookie != null) {
                    //cookie.setHttpOnly(true);
                } else {
                    if (pair.length == 2) {
                        cookie = new Cookie(pair[0], pair[1]);
                    } else {
                        cookie = new Cookie(pair[0], "");
                    }
                    cookies.put(pair[0], cookie);
                }
            }
        }
        return cookies;
    }
}

package com.pr7.common.web.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.pr7.common.web.localization.I18n;

/**
 *
 * @author mark.ma
 */
final public class WebUtils {

    private static final Logger logger = Logger.getLogger(WebUtils.class);
    final private static Map<String, Locale> SUPPORTED_LOCALE;

    static {
        SUPPORTED_LOCALE = new HashMap<String, Locale>();

        SUPPORTED_LOCALE.put(Locale.US.toString().toLowerCase(), Locale.US);
        SUPPORTED_LOCALE.put(Locale.SIMPLIFIED_CHINESE.toString().toLowerCase(), Locale.SIMPLIFIED_CHINESE);
        SUPPORTED_LOCALE.put(Locale.TRADITIONAL_CHINESE.toString().toLowerCase(), Locale.TRADITIONAL_CHINESE);
    }

    /**
     * get the local string ,
     *
     * cookie > parameter > url regex
     *
     * @param request
     * @return
     */
    public static Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        String guess = guessLanguage(request, response);

        if (StringUtils.isEmpty(guess)) {
            return Locale.US;
        }

        guess = guess.replace('-', '_').toLowerCase();

        if (SUPPORTED_LOCALE.containsKey(guess)) {
            return SUPPORTED_LOCALE.get(guess);
        }

        // defualt to us
        return Locale.US;
    }

    /**
     * parameter > cookie > http header > url regex
     *
     * @param request
     * @return
     */
    private static String guessLanguage(HttpServletRequest request, HttpServletResponse response) {
        String langFromParam = request.getParameter("lang");
        String langFromCookie = getCookieValue("lang", request);
        if (StringUtils.isBlank(langFromParam)) {
            langFromParam = request.getParameter("languageCode");
        }

        if (!StringUtils.isEmpty(langFromParam)) {
            if (StringUtils.isNotBlank(langFromParam) && !langFromParam.equalsIgnoreCase(langFromCookie)) {
                String serverName = request.getServerName();
                if (!serverName.matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$")) {
                    String[] parts = serverName.split("\\.");
                    if (parts.length > 2) {
                        parts[0] = "";
                        serverName = StringUtils.join(parts, ".");
                    }
                }
                langFromParam = langFromParam.toLowerCase().matches("english|en|en\\-gb|en\\-us") ? "english" : "simplified";

                //cookie_user_language
                Cookie cookie = new Cookie("lang", langFromParam);
                cookie.setDomain(serverName);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            return languageNameToCode(langFromParam);
        }

        if (!StringUtils.isEmpty(langFromCookie)) {
            return languageNameToCode(langFromCookie);
        }

        String langFromClient = request.getHeader("Accept-Language");
        if (!StringUtils.isEmpty(langFromClient)) {
            if (langFromClient.contains("zh")) {
                return "zh_CN";
            }
        }

        return "zh_CN";
    }

    public static String languageNameToCode(String code) {
        if ("english".equalsIgnoreCase(code)) {
            return "en_US";
        } else if ("traditional".equalsIgnoreCase(code)) {
            return "zh_TW";
        } else if ("simplified".equalsIgnoreCase(code)) {
            return "zh_CN";
        }
        return null;
    }

    public static String codeToLanguageName(String languageName) {
        if ("en_US".equalsIgnoreCase(languageName) || "en_GB".equalsIgnoreCase(languageName)) {
            return "english";
        } else if ("zh_TW".equalsIgnoreCase(languageName)) {
            return "traditional";
        } else if ("zh_CN".equalsIgnoreCase(languageName)) {
            return "simplified";
        }
        return null;
    }

    public static String codeToShortName(String languageName) {
        if ("en_US".equalsIgnoreCase(languageName)|| "en_GB".equalsIgnoreCase(languageName)) {
            return "en";
        } else if ("zh_TW".equalsIgnoreCase(languageName)) {
            return "cht";
        } else if ("zh_CN".equalsIgnoreCase(languageName)) {
            return "chs";
        }
        return null;
    }

    /**
     * get the css prefix by browser version
     *
     * @param request
     * @return
     */
    public static String getCssPrefixByBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        Integer i = 0;
        if ((i = userAgent.indexOf("Firefox")) != -1) {
            return "moz moz" + userAgent.charAt(i + "Firefox/".length());
        } else if ((i = userAgent.indexOf("MSIE")) != -1) {
            return "msie msie" + userAgent.charAt(i + "MSIE ".length());
        } else if ((i = userAgent.indexOf("Chrome")) != -1) {
            return "webkit chrome" + userAgent.charAt(i + "Chrome/".length());
        } else if ((i = userAgent.indexOf("Safari")) != -1) {
            return "webkit safari" + userAgent.charAt(i + "Safari/".length());
        } else {
            return null;
        }

    }

    public static boolean useQuirksMode(HttpServletRequest request) {
        try {
            String userAgent = request.getHeader("User-Agent");
            Integer i = userAgent.indexOf("MSIE");
            return i != -1 && Integer.valueOf(String.valueOf(userAgent.charAt(i + "MSIE ".length()))) < 9;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getCookieValue(String name, HttpServletRequest request) {
        String value = null;
        Cookie[] cookieList = request.getCookies();
        if (cookieList != null) {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }

        return value;
    }

    public static void setCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void removeAllCookies(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equalsIgnoreCase("JSESSIONID") && cookie.getName().equalsIgnoreCase("CSSID")) {
                continue;
            }
            removeCookie(response, cookie.getName());
        }
    }

    public static void removeAllCookiesExcepts(HttpServletRequest request, HttpServletResponse response, String[] excepts) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equalsIgnoreCase("JSESSIONID") && cookie.getName().equalsIgnoreCase("CSSID")) {
                continue;
            }
            
            if (ArrayUtils.contains(excepts, cookie.getName())) {
                continue;
            }
            
            removeCookie(response, cookie.getName());
        }
    }

    public static String getClientIPAddress(HttpServletRequest httpRequest) {
        String forwardIp = httpRequest.getHeader("x-forwarded-for");
        String userIp = StringUtils.isEmpty(forwardIp) ? httpRequest.getRemoteAddr() : forwardIp.split("\\,")[0];
        logger.debug("getClientIPAddress DONE:: userIp = " + userIp + ", forwardIp = " + forwardIp);
        return userIp;
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }

        return headers;
    }

    public static void log(HttpServletRequest request) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("\n");
            builder.append("!--REQUEST START--!\n");

            builder.append("Request URL: ").append(request.getRequestURL().toString()).append("\n");
            builder.append("Request Method: ").append(request.getMethod()).append("\n");

            builder.append("Request Headers:\n");

            for (String headerName : Collections.list((Enumeration<String>) request.getHeaderNames())) {
                builder.append("\t").append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
            }

            builder.append("Request Body:\n");
            for (String parameter : IOUtils.toString(request.getInputStream(), "UTF-8").split("&")) {
                if (StringUtils.isNotBlank(parameter)) {
                    String[] kvPair = parameter.split("=");
                    builder.append("\t").append(kvPair[0]).append(": ").append(kvPair[1]).append("\n");
                }
            }

            logger.info(builder.toString());
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void log(HttpServletResponse response) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("\n");
            builder.append("Response Headers:\n");

            for (String headerName : response.getHeaderNames()) {
                builder.append("\t").append(headerName).append(": ").append(response.getHeader(headerName)).append("\n");
            }

            builder.append("Response Language: ").append(I18n.getCurrentLocale().toString()).append("\n");

            builder.append("!--REQUEST END--!\n");

            logger.info(builder.toString());
        } catch (Exception ex2) {
            logger.error(ex2);
        }
    }
}

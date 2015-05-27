/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.spring.support;

import com.pr7.common.web.util.WebUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Admin
 */
public class HttpLoggerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(HttpLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getRequestURI().equalsIgnoreCase("/member/")) {
            WebUtils.log(request);
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        if (request.getRequestURI().equalsIgnoreCase("/member/")) {
            WebUtils.log(response);
        }
    }
}

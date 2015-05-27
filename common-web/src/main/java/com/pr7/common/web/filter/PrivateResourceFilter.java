/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.filter;

import com.pr7.common.web.AppSetting;
import com.pr7.common.web.spring.support.SpringApplicationContext;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mark.ma
 */
public class PrivateResourceFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain fc) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) _response;
        // blocking all requests to the resource folder if we are in production

        AppSetting appSetting = (AppSetting)SpringApplicationContext.getCurrent().getBean(AppSetting.class);
        boolean debug = appSetting.isDebugMode();
        
        if (_request.getParameter("debug") != null) {
            debug = "on".equalsIgnoreCase(_request.getParameter("debug"));
        }
        
        if (debug) {
            fc.doFilter(_request, _response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

    }

    @Override
    public void destroy() {
    }
}

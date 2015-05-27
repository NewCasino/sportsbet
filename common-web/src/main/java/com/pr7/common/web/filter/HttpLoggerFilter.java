/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author mark.ma
 */
public class HttpLoggerFilter implements Filter {

    FilterConfig filterConfig;
    
    @Override
    public void init(FilterConfig fc) throws ServletException {
        filterConfig = fc;
    }

    @Override
    public final void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        ServletContext context = filterConfig.getServletContext();
        
        // if not the landing page, just return
        if (!request.getRequestURI().equalsIgnoreCase("/")) {
            chain.doFilter(req, res);
            return;
        }
        
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

            context.log(builder.toString());
        } catch (Exception ex) {
            context.log("Failed to write http log", ex);
        }
            
        chain.doFilter(req, res);
        
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("\n");
            builder.append("Response Headers:\n");

            for (String headerName : response.getHeaderNames()) {
                builder.append("\t").append(headerName).append(": ").append(response.getHeader(headerName)).append("\n");
            }

            builder.append("!--REQUEST END--!\n");

            context.log(builder.toString());
        } catch (Exception ex) {
            context.log("Failed to write http log", ex);
        }
    }
    
    @Override
    public void destroy() {
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.interceptors;

import com.pr7.backoffice.utils.RequestUtils;
import com.pr7.server.session.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Admin
 */
public class AuthenticationCheckInterceptor extends  HandlerInterceptorAdapter{
    @Autowired
    UserSession userSession;

    @Value("${BACK_OFFICE_LOGIN_URI}")
    String backofficeLoginURI;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!RequestUtils.isWebServiceRquest(request)){
            if(!isLoginPage(request) && !userSession.isAuthenticated()){                
                response.sendRedirect(backofficeLoginURI);
                return false;
            }

            if (isLoginPage(request) && userSession.isAuthenticated()){
                response.sendRedirect(request.getContextPath());
                return false;
            }

        }
        return super.preHandle(request, response, handler);
    }

    private boolean isLoginPage(HttpServletRequest request){
         String uri =  request.getRequestURI();
         return uri.equalsIgnoreCase(backofficeLoginURI);
    }
}


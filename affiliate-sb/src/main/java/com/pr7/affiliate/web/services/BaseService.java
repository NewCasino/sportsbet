/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.affiliate.web.services;

import com.pr7.common.web.AppSetting;
import com.pr7.common.web.localization.I18n;
import com.pr7.server.common.exception.AuthenticationException;
import com.pr7.server.common.exception.AuthorizationException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
public class BaseService {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected AppSetting appSetting;

    @ModelAttribute("locale")
    public Locale getLocale() {
        return I18n.getCurrentLocale();
    }

    @ModelAttribute("test")
    public boolean getTest() {
        return "on".equalsIgnoreCase(request.getParameter("test"));
    }

    @ModelAttribute("debug")
    public boolean getDebug() {
        return appSetting.isDebugMode();
    }

//    @ExceptionHandler
//    @ResponseBody
//    public Object ServiceExceptionHandler(Exception ex) {
//        if (isAuthException(ex)) {
//            return "{\"error\": 403, \"msg\":\"Unauthenticated\"}";
//        } else {
//            return "{\"error\": 500, \"msg\":\"Service Unavailable\"}";
//        }
//    }
//
//    private boolean isAuthException(Exception ex) {
//        return ex instanceof AuthorizationException
//                || ex.getCause() instanceof AuthorizationException
//                || ex instanceof AuthenticationException
//                || ex.getCause() instanceof AuthenticationException;
//    }
}

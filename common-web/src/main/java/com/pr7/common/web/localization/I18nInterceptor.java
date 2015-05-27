/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.common.web.localization;

import com.pr7.common.web.util.WebUtils;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author mark.ma
 */
public class I18nInterceptor extends HandlerInterceptorAdapter {

    private void initLocalization(HttpServletRequest request,HttpServletResponse response) {
        Locale locale = WebUtils.getLocale(request,response);
        I18n.setCurrentLocale(locale);
    }

    //before the actual handler will be executed
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        initLocalization(request,response);
        return true;

    }
}

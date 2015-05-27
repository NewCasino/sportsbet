/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pr7.common.web.controller;

import com.pr7.common.web.AppSetting;
import java.util.Locale;
import com.pr7.common.web.localization.I18n;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author mark.ma
 */
public class BaseController {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected AppSetting appSetting;

    @ModelAttribute("locale")
    public Locale getLocale () {
        return I18n.getCurrentLocale();
    }

    @ModelAttribute("test")
    public boolean getTest () {
        return "on".equalsIgnoreCase(request.getParameter("test"));
    }

    @ModelAttribute("debug")
    public boolean getDebug () {
        return appSetting.isDebugMode();
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web;

import com.pr7.backoffice.session.BOUserSession;
import com.pr7.captcha.ocr.AscMSCaptcha;
import com.pr7.common.web.controller.BaseController;
import com.pr7.server.session.UserInfo;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Admin
 */
@Controller
public class PagesController extends BaseController {

    @Autowired
    BOUserSession userSession;

    @ModelAttribute(value = "currentTimeMillis")
    public long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    @ModelAttribute(value = "userSession")
    public BOUserSession getUserSession() {
        return userSession;
    }

    @ModelAttribute(value = "userInfo")
    public UserInfo getUserInfo() {
        return userSession.getUserInfo();
    }

    @RequestMapping("/index")
    public ModelAndView indexPage(ModelMap model) {
        return new ModelAndView("index");
    }

    @RequestMapping("/")
    public ModelAndView rootPage(ModelMap model) {
        return new ModelAndView("index");
    }
    
    
    
}

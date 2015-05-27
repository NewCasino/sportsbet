/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.affiliate.web.controllers;

import com.pr7.affiliate.session.UserSession;
import com.pr7.common.web.controller.BaseController;
import com.pr7.common.web.taglib.StringFunction;
import com.pr7.common.web.util.WebUtils;
import com.pr7.exception.BaseException;
import com.pr7.exception.BusinessError;
import com.pr7.exception.ServiceException;
import com.pr7.modelsb.constant.MemberSessionLogAction;
import com.pr7.sb.service.AffiliateService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.exception.AuthenticationException;
import com.pr7.server.common.exception.AuthorizationException;
import com.pr7.server.constants.UserType;
import com.pr7.server.session.UserInfo;
import com.pr7.server.web.SmartExceptionResolver;
import com.pr7.util.IPAddressUtils;
import flexjson.JSONSerializer;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.apache.bcel.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Admin
 */
@Controller
public class PagesController extends BaseController {
    
    private static final Logger logger = LogManager.getLogger(PagesController.class);

    @Autowired
    UserSession userSession;
    
    @Autowired
    AffiliateService affiliateService;


    @ModelAttribute(value = "currentTimeMillis")
    public long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    @ModelAttribute(value = "userSession")
    public UserSession getUserSession() {
        return userSession;
    }

    @Value("${LIVE_CHAT_URL}")
    protected String liveChatUrl;

    @Value("${affiliate.content.uri}")
    protected String affiliateContentUri;

    @ModelAttribute(value = "liveChatUrl")
    public String getLiveChatUrl() {
        return liveChatUrl;
    }

    @Value("${ROOT_URL}")
    protected String rootUrl;

    @ModelAttribute(value = "rootUrl")
    public String getrootUrl() {
        String value =rootUrl;
        String serverName = request.getServerName();
        if(!IPAddressUtils.isValidIP4Adress(serverName)){
            String[] parts = serverName.split("\\.");
            if(parts.length == 3){                
                parts[0] = "www";
                serverName = StringUtils.join(parts, ".");
                value = "http://" + serverName;
                if(request.getServerPort() != 80){
                    value += (":" + request.getServerPort());
                }
            }
        }
        return value;
    }
    
    @ModelAttribute(value = "affiliateURL")
    public String getAffiliateUrl() {
        return String.format("%s/?id=%s", getrootUrl(), userSession.getUserInfo().getAffiliateCode());
    }
    
    @ModelAttribute(value="cmsLang")
    public String getCmsLang(){
        String code = getLocale().toString().toLowerCase();
        if (code.equalsIgnoreCase("zh_cn")) {
                code = "zh-hans";
        } else if (code.equalsIgnoreCase("en_us")) {
                code = "en";
        }
        return code;
    }

    @ModelAttribute(value = "userInfo")
    public UserInfo getUserInfo() {
        return userSession.getUserInfo();
    }

    @RequestMapping("/")
    public Object rootPage(ModelMap model) {
        return nodeHandler("cooperative", model);
    }

    @RequestMapping("/register")
    public Object registerPage(ModelMap model) {
        if (userSession.isAuthenticated()) {
            return new RedirectView("/", true, true, false);
        }
        return new ModelAndView("register");
    }

    @RequestMapping("/register/success")
    public Object registerSuccessPage(ModelMap model) {
        model.addAttribute("type", "success");
        model.addAttribute("message", StringFunction.i18n("Your registration successful. Thank you for registering with us. Your account is pending for approval."));
        return new ModelAndView("message", model);
    }

    @RequestMapping("/404")
    public Object notFoundPage(ModelMap model) {
        model.addAttribute("type", "failed");
        model.addAttribute("message", "Page Not Found");
        return new ModelAndView("message", model);
    }

    @RequestMapping("/logout")
    public Object logoutHandler(HttpServletRequest req) {
        affiliateService.logAffiliateSession(userSession.getLoginId(), req.getSession().getId(), WebUtils.getClientIPAddress(req), MemberSessionLogAction.LOGOUT);
        userSession.clear();
        return new RedirectView("/", true, true, false);
    }

    @RequestMapping("/change-password")
    @Authentication(type = UserType.AFFILIATE)
    public ModelAndView changePasswordHandler(ModelMap model) {
        return new ModelAndView("reset-password");
    }

    @RequestMapping("/forgot-password")
    @Authentication(type = UserType.ANONYMOUS)
    public ModelAndView forgetPasswordHandler(ModelMap model) {
        return new ModelAndView("forgot-password");
    }

    @RequestMapping("/affiliate-report")
    @Authentication(type = UserType.AFFILIATE)
    public ModelAndView affiliateReportHandler(ModelMap model) {
        return new ModelAndView("affiliate-report");
    }

    @RequestMapping("/affiliate/summary")
    @Authentication(type = UserType.AFFILIATE)
    public ModelAndView affiliateReportSummaryHandler(ModelMap model) {
        model.addAttribute("menu", "summary");
        return new ModelAndView("affiliate/summary");
    }
    
    @RequestMapping("/profile/{tab}")
    @Authentication(type = UserType.AFFILIATE)
    public ModelAndView affiliateProfileHandler(@PathVariable("tab") String tab, ModelMap model) {
        model.addAttribute("tab", tab);
        return new ModelAndView("profile", model);
    }
    
    @RequestMapping("/affiliate/messages/{tab}")
    @Authentication(type = UserType.AFFILIATE)
    public ModelAndView affiliateMessageHandler(@PathVariable("tab") String tab, ModelMap model) {
        model.addAttribute("tab", tab);
        model.addAttribute("menu", "messages");
        return new ModelAndView("affiliate/messages", model);
    }

    @RequestMapping("/reset-password/{token}")
    public ModelAndView resetPasswordHandler(@PathVariable("token") String token, ModelMap model) {
        try {
            token = URLDecoder.decode(token, "UTF-8");
            logger.debug("received password token: " + token);
            model.addAttribute("username", affiliateService.verifyChangePasswordToken(token));
            model.addAttribute("token", token);
        } catch (Exception ex) {
            logger.warn(ex);
            model.addAttribute("error", ex.getMessage().trim());
            model.addAttribute("invalidToken", true);
        }
        return new ModelAndView("reset-password", model);
    }
    
    
    @RequestMapping("/static/{page}")
    public ModelAndView nodeHandler(
            @PathVariable("page") String page, 
            ModelMap model) {
        model.addAttribute("page", page);
        model.addAttribute("libDepends", false);
        return new ModelAndView("static", model);
    }
    
    @RequestMapping("/affiliate/banners")
    @Authentication(type = UserType.AFFILIATE)
    public ModelAndView bannerHandler(
            ModelMap model) {
        model.addAttribute("affiliateContentUri", affiliateContentUri);
        model.addAttribute("menu", "banners");
        return new ModelAndView("affiliate/banners", model);
    }
    
    @RequestMapping("/affiliate/announcements")
    @Authentication(type = UserType.AFFILIATE)
    public ModelAndView announcementHandler(
            ModelMap model) {
        model.addAttribute("menu", "announcements");
        return new ModelAndView("affiliate/announcements", model);
    }
    
     
    @ExceptionHandler
    public ModelAndView resolveException(HttpServletRequest request,HttpServletResponse response,Exception ex) throws IOException {
        if (isAuthException(ex)) {
            return new ModelAndView("redirect:/");            
        }else{            
            response.sendError(500);
        }
        return null;
    }
    
    private boolean isAuthException(Exception ex) {
        return ex instanceof AuthorizationException
                || ex.getCause() instanceof AuthorizationException
                || ex instanceof AuthenticationException
                || ex.getCause() instanceof AuthenticationException;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.affiliate.web.services;

import com.pr7.affiliate.session.UserSession;
import com.pr7.common.web.util.WebUtils;
import com.pr7.modelsb.constant.MemberSessionLogAction;
import com.pr7.modelsb.entity.Affiliate;
import com.pr7.sb.constant.LoginStatus;
import com.pr7.sb.model.LoginResult;
import com.pr7.sb.security.CaptchaService;
import com.pr7.sb.service.AffiliateService;
import com.pr7.server.constants.UserType;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/auth")
public class AuthService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);
    
    @Autowired
    private UserSession userSession;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AffiliateService affiliateService;
    @Autowired
    private CaptchaService captchaService;

    @RequestMapping("/login")
    @ResponseBody
    public Object loginHandler(String username, String password, String captcha, HttpServletRequest request) {
        LoginStatus result = LoginStatus.INVALID_UN_PW;
        
        if (!isValidCaptcha(captcha)) {
            result = LoginStatus.INVALID_CAPTCHA;
        } else {
            LoginResult loginResult = affiliateService.verify(username, password);
            if (loginResult.getStatus() != LoginStatus.SUCCESS) {
                result = loginResult.getStatus();
            } else if (loginResult.getUserData() != null) {
                Affiliate affiliate = (Affiliate) loginResult.getUserData();
                userSession.authenticate(affiliate.getId(), username, UserType.AFFILIATE);
                userSession.getUserInfo().setName(String.format("%s %s", affiliate.getFirstName(), affiliate.getLastName()));
                userSession.getUserInfo().setLoginId(affiliate.getUsername());
                userSession.getUserInfo().setAffiliateCode(affiliate.getCode());
                userSession.getUserInfo().setUserId(affiliate.getId());
                result = loginResult.getStatus();
                
                affiliateService.logAffiliateSession(userSession.getLoginId(), request.getSession().getId(), WebUtils.getClientIPAddress(request), MemberSessionLogAction.LOGIN);
            } 
        }
        
        logger.debug(String.format("member %s is logging in to affiliate site via IP %s, return %s", username, WebUtils.getClientIPAddress(request), result.toString()));

        return result;
    }

    @RequestMapping(value = "/captcha.jpg")
    @ResponseBody
    public void captchaHandler(
            HttpServletResponse response) {
        ServletOutputStream responseOutputStream = null;
        try {
            BufferedImage challenge = captchaService.getImageChallengeForID(getCaptchaId());
    
            BufferedImage newImage = new BufferedImage( challenge.getWidth(), challenge.getHeight(), BufferedImage.TYPE_INT_RGB);
            newImage.createGraphics().drawImage( challenge, 0, 0, new Color(224, 66, 123), null);
            
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            responseOutputStream = response.getOutputStream();
            ImageIO.write(newImage, "jpg", responseOutputStream);
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    private boolean isValidCaptcha(String captcha) {
        return captchaService.validateResponseForID(getCaptchaId(), captcha);
    }

    private String getCaptchaId() {
        return request.getSession().getId();
    }
}

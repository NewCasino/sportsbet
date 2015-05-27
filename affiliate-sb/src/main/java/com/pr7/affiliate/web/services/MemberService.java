/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.affiliate.web.services;

import com.pr7.affiliate.EmailSender;
import com.pr7.affiliate.session.UserSession;
import com.pr7.common.web.controller.BaseController;
import com.pr7.common.web.localization.I18n;
import com.pr7.modelsb.constant.SecurityQuestion;
import com.pr7.modelsb.dto.AffiliateDto;
import com.pr7.modelsb.entity.Affiliate;
import com.pr7.exception.ServiceException;
import com.pr7.modelsb.constant.Gender;
import com.pr7.modelsb.entity.AffiliateWebsite;
import com.pr7.sb.constant.LoginStatus;
import com.pr7.sb.model.ChangePasswordTokenResult;
import com.pr7.sb.security.CaptchaService;
import com.pr7.sb.service.AffiliateService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.Ensure;
import flexjson.JSONDeserializer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.stringtemplate.v4.ST;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/member")
public class MemberService extends BaseService {

    private static final Logger logger = LogManager.getLogger(MemberService.class);
    private static final String siteName = "Betmart";
    @Autowired
    private AffiliateService affiliateService;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserSession userSession;
    @Autowired
    private CaptchaService captchaService;

    @RequestMapping("/register")
    @ResponseBody
    public Object registerHandler(
            String captcha, 
            @RequestParam("website") String[] website,
            @RequestParam("affiliate") String affiliateJson) {
        Affiliate affiliate = new JSONDeserializer<Affiliate>().deserialize(affiliateJson.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.Affiliate\" }"));
        affiliate.setLangId(getLocale().toString());
        if (!isValidCaptcha(captcha)) {
            throw new ServiceException(LoginStatus.INVALID_CAPTCHA.toString());
        }

        // create affiliate
        affiliate = affiliateService.create(affiliate);
        
        // store affiliate websites
        List<AffiliateWebsite> affiliateWebsites = new ArrayList<AffiliateWebsite>();
        for (String url : website) {
            AffiliateWebsite affiliateWebsite = new AffiliateWebsite();
            affiliateWebsite.setUrl(url);
            affiliateWebsites.add(affiliateWebsite);
        }
        affiliateService.setAffiliateWebsites(affiliate.getId(), affiliateWebsites);
        
        // send email notification
        try {
            String template = "registration-email.template".concat(getLocale() == Locale.US ? "" : "." + getLocale().toString()).concat(".html");
            InputStream file = getClass().getClassLoader().getResource(template).openStream();
            ST st = new ST(IOUtils.toString(file, "UTF-8"), '$', '$');
            st.add("user", affiliate);
            st.add("userGender", I18n._n(affiliate.getGender().equalsIgnoreCase(Gender.FEMALE.toString()) ? "Ms." : "Mr."));
            st.add("websites", website);
            
            String to = affiliate.getEmail();
            String subject = I18n._n("Welcome to BETMART Business!");
            String body = st.render();
            emailSender.send(to, subject, body);
            file.close();
        } catch (MessagingException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        }
        
        AffiliateDto result = new AffiliateDto();
        result.copyFromEntity(affiliate);
        return result;
    }

    @Authentication(type = UserType.AFFILIATE)
    @RequestMapping("/profile")
    @ResponseBody
    public Object profileHandler() {
        Affiliate affiliate = affiliateService.getById(userSession.getUserId());

        AffiliateDto result = new AffiliateDto();
        result.copyFromEntity(affiliate);
        return result;
    }

    @RequestMapping("/edit")
    @ResponseBody
    @Authentication(type = UserType.AFFILIATE)
    public Object editHandler(@RequestParam("affiliate") String affiliateJson) {
        Affiliate affiliate = new JSONDeserializer<Affiliate>().deserialize(affiliateJson.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.Affiliate\" }"));

        if (affiliate.getId() != userSession.getUserId()) {
            throw new ServiceException("Edit information for other member is not allowed");
        }

        affiliate = affiliateService.update(affiliate);

        AffiliateDto result = new AffiliateDto();
        result.copyFromEntity(affiliate);
        return result;
    }

    @RequestMapping("/isExistsUsername")
    @ResponseBody
    public Object checkUsernameExistsHandler(String username) {
        return affiliateService.getByUsername(username) != null;
    }

    @RequestMapping("/isExistsEmail")
    @ResponseBody
    public Object checkEmailExistsHandler(String email) {
        return affiliateService.getByEmail(email) != null;
    }

    @RequestMapping("/changePassword")
    @ResponseBody
    @Authentication(type = UserType.AFFILIATE)
    public Object changePasswordHandler(String oldPassword, String newPassword, String confirmPassword) {
        return affiliateService.changePassword(userSession.getLoginId(), oldPassword, newPassword, confirmPassword);
    }

    @RequestMapping("/forgetPassword")
    @ResponseBody
    public Object forgetPasswordHandler(String email, SecurityQuestion securityQuestion, String securityAnswer) {
        if(StringUtils.isBlank(email) || StringUtils.isBlank(securityAnswer)){
            throw new ServiceException("Invalid arguments.");
        }

        Affiliate affiliate = affiliateService.getByEmail(email);
        if(affiliate == null) {
            throw new ServiceException("Email address does not exist.");
        }

        ChangePasswordTokenResult result = affiliateService.generateChangePasswordToken(affiliate.getUsername(), securityQuestion, securityAnswer);
        if (result.isValid()) {
            try {
                String link = String.format("http://%s/affiliate/web/reset-password/%s?lang=%s", request.getHeader("Host"), URLEncoder.encode(URLEncoder.encode(result.getToken(), "UTF-8"), "UTF-8"), getLocale().getLanguage());
                String body = String.format(
                        I18n._n("Dear %s, <br /><br />Please <a href='%s'>click here</a> or open the following link in order to reset your password. <br /> %s"),
                        affiliate.getFirstName(), link, link);
                emailSender.send(email, String.format(I18n._n("Continue to reset your %s affiliate password"), siteName), body);
            } catch (UnsupportedEncodingException ex) {
                logger.error(ex);
                throw new ServiceException(ex.getMessage());
            } catch (MessagingException ex) {
                String message = String.format(I18n._n("Failed to send email to %s"), email);
                logger.error(message, ex);
                throw new ServiceException(message);
            }
        }

        return result;
    }

    @RequestMapping("/resetPassword")
    @ResponseBody
    public Object resetPasswordHandler(String token, String newPassword, String confirmPassword) {
        return affiliateService.changePassword(token, newPassword, confirmPassword);
    }

    @RequestMapping(value = "/captcha.jpg")
    @ResponseBody
    public void captchaHandler(
            HttpServletResponse response) {
        ServletOutputStream responseOutputStream = null;
        try {
            BufferedImage challenge = captchaService.getImageChallengeForID(getCaptchaId());
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/png");
            responseOutputStream = response.getOutputStream();
            ImageIO.write(challenge, "png", responseOutputStream);
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (IOException ex) {
        }
    }

    private boolean isValidCaptcha(String captcha) {
        return captchaService.validateResponseForID(getCaptchaId(), captcha);
    }

    private String getCaptchaId() {
        return getCaptchaId("for-register");
    }

    private String getCaptchaId(String page) {
        return request.getSession().getId() + "-"  + page;
    }
}
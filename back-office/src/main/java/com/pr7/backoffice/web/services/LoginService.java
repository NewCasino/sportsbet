/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.session.BOUserSession;
import com.pr7.common.web.util.WebUtils;
import com.pr7.modelsb.entity.Admin;
import com.pr7.sb.constant.LoginStatus;
import com.pr7.sb.model.LoginResult;
import com.pr7.sb.security.CaptchaService;
import com.pr7.sb.service.AdminService;
import com.pr7.server.constants.UserType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/sv/login")
public class LoginService {
    @Autowired
    private BOUserSession session;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CaptchaService captchaService;

    @RequestMapping("/doLogin.sv")
    @ResponseBody
    public Object doLogin(
            HttpServletRequest request,
            @RequestParam(value="username") String userName,
            @RequestParam(value="password") String password,
            @RequestParam(value="captcha") String captcha
            )
    {
        LoginStatus status = LoginStatus.INVALID_UN_PW;
        if(!isValidCaptcha( request, captcha)){
            status = LoginStatus.INVALID_CAPTCHA;
        }else{
            LoginResult loginResult = adminService.doAdminLogin(userName, password);
            status = loginResult.getStatus();
            if(status == LoginStatus.SUCCESS && loginResult.getUserData()!=null){
                Admin admin = (Admin)loginResult.getUserData();
                session.authenticate(admin.getId(), userName, UserType.BO);
                session.getUserInfo().setName(admin.getFullname());
                session.getUserInfo().setLoginId(admin.getUsercode());
                session.getUserInfo().setUserId(admin.getId());
            }
        }
        return status.toString();
    }

    @RequestMapping("/doLogout.sv")
    @ResponseBody
    public Object doLogout(HttpServletRequest request, HttpServletResponse response){
        session.clear();
        WebUtils.removeAllCookies(request, response);
        return true;
    }

    @RequestMapping(value="/captcha.jpg")
    @ResponseBody
    public void getCaptCha(
            HttpServletRequest request, 
            HttpServletResponse response){
        ServletOutputStream responseOutputStream = null;
        try {
            BufferedImage challenge = captchaService.getImageChallengeForID(getCaptchaId(request));
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

    private boolean isValidCaptcha(HttpServletRequest request, String captcha){
        return captchaService.validateResponseForID(getCaptchaId(request) , captcha);
    }

    private String getCaptchaId(HttpServletRequest request){
        return request.getSession().getId();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.controllers;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.modelsb.entity.AffiliateComment;
import com.pr7.sb.service.AffiliateCommentService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/comment")
@Authentication(type= UserType.BO)
@Authorization(hasAnyPermission={Permissions.ListPrivateMessage})
public class AffiliateCommentController {
    
    @Autowired
    AffiliateCommentService affiliateCommentService;
    
    @RequestMapping("/reply")
    @ResponseBody
    public Object replyHandler(int id) {
        /*model.addAttribute("comment", affiliateCommentService.getById(id));
        return new ModelAndView("affiliate/comment-reply", model);*/
        AffiliateComment ac = (AffiliateComment)affiliateCommentService.getById(id);       
        return (ac);
    }
    
    @RequestMapping("/edit")
    @ResponseBody
    public Object editHandler(int id) {
        /*model.addAttribute("comment", affiliateCommentService.getById(id));
        return new ModelAndView("affiliate/comment-edit", model);*/
        AffiliateComment ac = (AffiliateComment)affiliateCommentService.getById(id);       
        return (ac);
    }
}

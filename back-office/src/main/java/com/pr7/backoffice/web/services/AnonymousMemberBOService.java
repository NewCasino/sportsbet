/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.BaseFilterService;
import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.constant.Language;
import com.pr7.modelsb.constant.AnonymousMemberType;
import com.pr7.modelsb.constant.MemberStatus;
import com.pr7.modelsb.dto.AnonymousMemberDto;
import com.pr7.sb.service.AnonymousMemberService;
import com.pr7.sbasc.service.SBAscMemberService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
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
@RequestMapping("/sv/anonymos-member")
@Authentication(type = {UserType.BO})
@Authorization(hasAnyPermission = {Permissions.EditAnonymousMember, Permissions.ListAnonymousMember})
public class AnonymousMemberBOService extends BaseFilterService {

    @Autowired
    AnonymousMemberService anonymousMemberService;
    @Autowired
    SBAscMemberService ascMemberService;

    @RequestMapping(value = "/list.sv")
    @ResponseBody
    public Object list(@RequestParam(value = "type", defaultValue = "SPORTSBOOK") AnonymousMemberType type, @RequestParam(value = "lang", defaultValue = "ENGLISH") Language language) {
        if (type == AnonymousMemberType.SPORTSBOOK) {
            language = null;
        }
        return new PaginateRecordJSON(getBaseFilter(), anonymousMemberService.getAnonymousMembers(type, language).toArray());
    }

    @RequestMapping(value = "/updateStatus.sv")
    @Authorization(hasAnyPermission = {Permissions.EditAnonymousMember})
    @ResponseBody
    public boolean updateStatus(String coreMemberCode, @RequestParam(value = "type", defaultValue = "SPORTSBOOK") AnonymousMemberType type, MemberStatus status) {
        anonymousMemberService.updateStatus(coreMemberCode, type, status);
        return true;
    }

    @RequestMapping(value = "/create.sv")
    @Authorization(hasAnyPermission = {Permissions.EditAnonymousMember})
    @ResponseBody
    public boolean create(@RequestParam(value = "type", defaultValue = "SPORTSBOOK") AnonymousMemberType type, @RequestParam(value = "lang", defaultValue = "ENGLISH") Language language) throws Exception {
        AnonymousMemberDto anonymousMemberDto;
        switch (type) {
            case SPORTSBOOK:
                anonymousMemberDto = anonymousMemberService.createAnonymousMember();
                break;
            case SPORTSBOOK2:
                anonymousMemberDto = anonymousMemberDto = ascMemberService.createAnonymousMember(language);
                break;
        }
        return true;
    }

    @RequestMapping(value = "/resetPassword.sv")
    @Authorization(hasAnyPermission = {Permissions.EditAnonymousMember})
    @ResponseBody
    public String resetPassword(String coreMemberCode, @RequestParam(value = "type", defaultValue = "SPORTSBOOK") AnonymousMemberType type) {
        String newPw = "";

        switch (type) {
            case SPORTSBOOK:
                newPw = anonymousMemberService.resetPassword(coreMemberCode);
                break;
            case SPORTSBOOK2:
                newPw = ascMemberService.resetAnnoymousMemberPassword(coreMemberCode);
                break;
        }
        return newPw;
    }

    @RequestMapping(value = "/showPassword.sv")
    @Authorization(hasAnyPermission = {Permissions.EditAnonymousMember})
    @ResponseBody
    public String showPassword(String coreMemberCode, @RequestParam(value = "type", defaultValue = "SPORTSBOOK") AnonymousMemberType type) {
        return anonymousMemberService.getAnonymousMemberPassword(coreMemberCode, type);
    }
    
    @RequestMapping(value = "/forceChangeLanguage.sv")    
    @ResponseBody
    public String forceChangeLanguage(String coreMemberCode, String langauge, @RequestParam(value = "type", defaultValue = "SPORTSBOOK") AnonymousMemberType type) {
        Language lang = Language.getLanguageByNamedValue(langauge);
        AnonymousMemberDto anonymousMember = anonymousMemberService.getAnonymousMember(coreMemberCode, type);
        return ascMemberService.changeLanguage(lang, anonymousMember.getCookie()) ? "OK" : "FAILED";
    }
}

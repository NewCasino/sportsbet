/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.casino.dsp.common.AccountType;
import com.pr7.casino.dsp.common.DSPException;
import com.pr7.casino.dsp.dto.EH2AgentInfo;
import com.pr7.casino.dsp.dto.EHAgentInfo;
import com.pr7.casino.dsp.dto.MemberDspTrialDto;
import com.pr7.casino.dsp.dto.MemberDspTrialFilter;
import com.pr7.casino.dsp.service.MemberDspService;
import com.pr7.casino.dsp.service.MemberDspTrialService;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.constant.MemberStatus;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.server.session.UserSession;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
@RequestMapping("/sv/memberdsp")
@Authentication(type={UserType.BO})
public class MemberDspBOService {
    private static final Logger logger = LogManager.getLogger(MemberDspBOService.class);

    @Autowired
    private UserSession userSession;
    
    @Autowired
    private MemberDspService dspService;
    
    @Autowired
    private MemberDspTrialService memberDspTrialService;
    
    @Autowired
    EHAgentInfo ehAgentInfo;
    
    @Autowired
    EH2AgentInfo eh2AgentInfo;

    @RequestMapping("/createmember.sv")
    @ResponseBody
    @Authorization(hasAnyPermission={Permissions.ListEhTrial})
    public Object createExtMemberMapping(String memberCode, String extMemberCode, String product) throws DSPException {
        MemberDspTrialDto result = null;
        ExtProductVendor vendor = ExtProductVendor.parseFromCode(product);
        switch(vendor){
            case EHCASINO:
                result = memberDspTrialService.createTrialAccount(memberCode, extMemberCode, AccountType.TRIAL, ehAgentInfo, vendor, MemberStatus.ACTIVE, userSession.getLoginId());
                break;
            case EH2CASINO:
                result = memberDspTrialService.createTrialAccount(memberCode, extMemberCode, AccountType.TRIAL, eh2AgentInfo, vendor, MemberStatus.ACTIVE, userSession.getLoginId());
                break;
        }
        return result;
    }
    
    @RequestMapping("/updatestatus.sv")
    @ResponseBody
    @Authorization(hasAnyPermission={Permissions.ListEhTrial})
    public Object updateStatus(long id, MemberStatus status){
        memberDspTrialService.updateStatus(id, status);
        return true;
    }
    
    @RequestMapping("listMemberDspTrial.sv")
    @Authorization(hasAnyPermission={Permissions.ListEhTrial})
    @ResponseBody
    public Object listMemberDspTrial(            
            @RequestParam(defaultValue="") String memberCode,
            @RequestParam(defaultValue="") String ehCode,
            @RequestParam(defaultValue="INVALID") ExtProductVendor product,
            @RequestParam(defaultValue="ACTIVE") MemberStatus status,
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate
            ){

        MemberDspTrialFilter filter = new MemberDspTrialFilter();
        filter.setMemberCode(memberCode);
        filter.setEhCode(ehCode);        
        filter.setProduct(product);
        filter.setStatus(status);
        filter.setStartDate(new Date(startDate));
        filter.setEndDate(new Date(endDate));
        
        return memberDspTrialService.getListByFilter(filter);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.casino.dsp.common.AccountType;
import com.pr7.casino.dsp.common.DSPException;
import com.pr7.casino.dsp.dto.EH2AgentInfo;
import com.pr7.casino.dsp.dto.EHAgentInfo;
import com.pr7.casino.dsp.service.MemberDspService;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.constant.MemberStatus;
import com.pr7.modelsb.dto.ExternalMemberFilter;
import com.pr7.sb.service.ExternalMemberMapService;
import com.pr7.sb3.service.SB3MemberService;
import com.pr7.sbasc.service.SBAscMemberService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
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
@RequestMapping("/sv/ex-member")
@Authentication(type={UserType.BO})
public class ExtMemberMapBOService {
    private static final Logger logger = LogManager.getLogger(ExtMemberMapBOService.class);
    @Autowired
    private ExternalMemberMapService memberMapService;

    @Autowired
    private MemberDspService dspService;
    
    @Autowired
    private SBAscMemberService ascMemberService;
    
    @Autowired
    EHAgentInfo ehAgentInfo;
    
    @Autowired
    EH2AgentInfo eh2AgentInfo;

    @Autowired
    SB3MemberService sB3MemberService;
    @RequestMapping("/create-ext-member.sv")
    @ResponseBody
    @Authorization(hasAnyPermission={Permissions.EditMemberMaping})
    public Object createExtMemberMapping(String memberCode, String extMemberCode, ExtProductVendor vendor, AccountType type) throws DSPException{
        Object result = Boolean.FALSE;
        switch(vendor){
            case EHCASINO:
                return dspService.createAccount(memberCode, extMemberCode, type, ehAgentInfo);
            case EH2CASINO:
                return dspService.createAccount(memberCode, extMemberCode, type, eh2AgentInfo);
        }
        return result;
    }
    
    @RequestMapping("/update-status.sv")
    @ResponseBody
    @Authorization(hasAnyPermission={Permissions.EditMemberMaping})
    public Object updateStatus(String memberCode, ExtProductVendor vendor, MemberStatus status){
        if(vendor == ExtProductVendor.SBASC){
            return ascMemberService.updateMemberStatus(memberCode, status);
        }else if (vendor == ExtProductVendor.SB3){
            return sB3MemberService.updateMemberStatus(memberCode, status);
        }else{
            return memberMapService.updateStatus(memberCode, vendor, status);
        }
    }
    
    @RequestMapping("/reset-password.sv")
    @ResponseBody
    @Authorization(hasAnyPermission={Permissions.EditMemberMaping})
    public Object resetPassword(String memberCode, ExtProductVendor vendor, String pwd){
        if(vendor == ExtProductVendor.SBASC){
            return ascMemberService.resetMemberPassword(memberCode, pwd);
        }else{
            return false;
        }
    }

     /**
     *
     * @param memberCode Member code
     * @param exMemberCode external member code
     * @param status Member status, ACTIVE OR INACTIVE
     * @param agentCode external agent code
     * @param dateFilterType 0 filter by register date, 1 filter by external account creation date
     * @param dateFrom
     * @param dateTo
     * @return
     */
    @RequestMapping("list.sv")
    @Authorization(hasAnyPermission={Permissions.ListMemberMaping})
    @ResponseBody
    public Object listExtMemberMap(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="0") int rp,
            @RequestParam(defaultValue="") String memberCode,
            @RequestParam(defaultValue="") String exMemberCode,
            @RequestParam(defaultValue="ACTIVE") MemberStatus status,
            @RequestParam(defaultValue="") String agentCode,
            @RequestParam(defaultValue="") String affCode,
            @RequestParam(defaultValue="REGISTER_DATE") ExternalMemberFilter.DateFilterType dateFilterType,
            ExtProductVendor vendor,
            long dateFrom,
            long dateTo
            ){

        ExternalMemberFilter filter = new ExternalMemberFilter();
        filter.setMemberCode(memberCode);
        filter.setExMemberCode(exMemberCode);
        filter.setDateFilterType(dateFilterType);
        filter.setStatus(status);
        filter.setVendor(vendor);
        filter.setCreatedDateFrom(new Date(dateFrom));
        filter.setCreatedDateTo(new Date(dateTo));
        filter.setAgentCode(agentCode);
        filter.setAffCode(affCode);
        PaginateRecordJSON paginateRecords = new PaginateRecordJSON(page,rp);
        paginateRecords.setRecords(memberMapService.getExternalMemersMap(filter).toArray());

        return paginateRecords;
    }
}

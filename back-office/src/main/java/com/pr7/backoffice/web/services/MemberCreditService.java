/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.BaseFilterService;
import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.sb.constant.Constants;
import com.pr7.sb.processor.agent.model.MemberCredits;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.ThreadLocalManager;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/sv/member-credit")
@Authentication(type = {UserType.BO})
public class MemberCreditService extends BaseFilterService {
    private static final Logger _logger = LogManager.getLogger(MemberCreditService.class);

    @Autowired
    private com.pr7.sb.service.MemberService memberService;

    @ResponseBody
    @RequestMapping(value="/list.sv")
    @Authorization(hasAnyPermission={Permissions.ListMemberCredit})
    public Object list(
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String coreMemberCode) {
        List<Map<String, Object>> list = memberService.getMemberCreditsFromCoreSysTransferPage(memberCode, coreMemberCode);
        
        return new PaginateRecordJSON(this.getBaseFilter(), list.toArray());
    }
    
    @ResponseBody
    @RequestMapping(value="/clean.sv")
    @Authorization(hasAnyPermission={Permissions.CleanUpMemberCredit})
    public Object clean(String memberCode) throws Exception {
        try {
            Map<String, Object> result = memberService.cleanUpGivenCredit(memberCode);
            String traceLog = (String) ThreadLocalManager.getThreadContext().getDataMap().get(Constants.TRACE_LOG);
            MemberCredits mcs = (MemberCredits)result.get("instance");
            if(mcs != null)
            {
                mcs.setLog(traceLog);
                memberService.memberCreditAudit(mcs);
            }           
            result.remove("instance"); //remove "instance" element before sending result to client
            result.put("log", traceLog);
            
            return result;
        } catch(Exception e) {
            String traceLog = (String) ThreadLocalManager.getThreadContext().getDataMap().get(Constants.TRACE_LOG);
            _logger.error("ERROR tracelog = " + traceLog);
            
            _logger.error(e.getMessage(), e);
            throw e;
        }
    }
    
}

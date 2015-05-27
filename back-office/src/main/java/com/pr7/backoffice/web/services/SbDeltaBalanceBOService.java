/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.BaseFilterService;
import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.exception.ServiceException;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.constant.MemberStatus;
import com.pr7.modelsb.dto.BaseFilter;
import com.pr7.modelsb.dto.SbDeltaBalanceFilter;
import com.pr7.sb.service.SbDeltaBalanceService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/sv/delta-balance")
@Authentication(type = {UserType.BO})
public class SbDeltaBalanceBOService extends BaseFilterService {

    @Autowired
    SbDeltaBalanceService deltaBalanceService;

    @ResponseBody
    @RequestMapping(value="/cleanUp.sv")
    @Authorization(hasAnyPermission={Permissions.EditDeltaBalance})
    public Object cleanUpDeltaBalance(String memberCode){
        deltaBalanceService.clearnUpSbDeltaBalance(memberCode);
        return true;
    }
    
    @ResponseBody
    @RequestMapping(value="/filter.sv")
    @Authorization(hasAnyPermission={Permissions.ListDeltaBalance, Permissions.EditDeltaBalance})
    public Object filterBalance(
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String exMemberCode,
            @RequestParam(defaultValue = "ACTIVE") MemberStatus status,
            @RequestParam(defaultValue = "") String agentCode,
            @RequestParam(defaultValue = "GREATER") BaseFilter.CompareType compareType,
            @RequestParam(defaultValue = "0") String balance,
            long dateFrom,
            long dateTo) {
        SbDeltaBalanceFilter filter = (SbDeltaBalanceFilter) getPaginationInfo(new SbDeltaBalanceFilter());
        BigDecimal deltaBalance = BigDecimal.ZERO;
        if(StringUtils.isNotBlank(balance)){
            balance = balance.replace(",", "");
            try {
                deltaBalance = new BigDecimal(balance);
            } catch (Exception e) {
                throw new ServiceException("Can not convert " + balance + " to number.");
            }            
        }
        filter.setBalance(deltaBalance);
        filter.setMemberCode(memberCode);
        filter.setCoreMemberCode(exMemberCode);
        filter.setBalanceCompareOperator(compareType);
        filter.setRegisterDateFrom(new Date(dateFrom));
        filter.setRegisterDateTo(new Date(dateTo));
        return new PaginateRecordJSON(filter, deltaBalanceService.filterDeltaBalance(filter).toArray());
        
    }
}

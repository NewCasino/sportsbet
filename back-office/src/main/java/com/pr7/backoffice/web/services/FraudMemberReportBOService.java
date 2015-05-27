/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.BaseFilterService;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.modelsb.constant.FraudMemberRelateType;
import com.pr7.modelsb.dto.FraudMemberDto;
import com.pr7.modelsb.dto.FraudMemberReportFilter;
import com.pr7.modelsb.dto.FraudMemberSummaryDto;
import com.pr7.sb.service.FraudMemberReport;
import com.pr7.server.common.Authentication;
import com.pr7.server.constants.UserType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/sv/fraud-member")
@Authentication(type={UserType.BO})
//@Authorization(hasAnyPermission={Permissions.ListFraudMember})
public class FraudMemberReportBOService extends BaseFilterService{
    @Autowired
    private FraudMemberReport fraudMemberReport;
    
    @RequestMapping("/summary.sv")    
    @ResponseBody
    public Object fraudMemberReport(String memberCodes, String relateTypes, long loginDateFrom, long loginDateTo){
        FraudMemberReportFilter filter = (FraudMemberReportFilter) getPaginationInfo(new FraudMemberReportFilter(memberCodes, relateTypes, 7));
        List<FraudMemberSummaryDto> fraudMembersSummary;
        if(loginDateFrom >0 &&loginDateTo > 0 ){
            filter.setFromLoginDate(new Date(loginDateFrom));
            filter.setToLoginDate(new Date(loginDateTo));
        }
        if(filter.getMemberCodes().isEmpty()){
            fraudMembersSummary = fraudMemberReport.fraudMembersSummary(filter.getFromLoginDate() , filter.getToLoginDate(), filter.getRelateTypes().toArray(new FraudMemberRelateType[0]));
        }else{
            fraudMembersSummary = fraudMemberReport.fraudMembersSummary(filter.getMemberCodes().toArray(new String[0]) ,filter.getRelateTypes().toArray(new FraudMemberRelateType[0]), filter.getFromLoginDate() , filter.getToLoginDate());
        }
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
        int index = 0;
        for (FraudMemberSummaryDto fraudMemberSummaryDto : fraudMembersSummary) {
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("index", String.valueOf(++index));
            map.put("memberCode", fraudMemberSummaryDto.getMemberCode());
            map.putAll(fraudMemberSummaryDto.getRelateMemberCodeByType());
            list.add(map);
        }
        return new PaginateRecordJSON(filter, list.toArray());
    }
    
    @RequestMapping("/details.sv")    
    @ResponseBody
    public Object fraudMemberDetail(String memberCode, String relateTypes, long loginDateFrom, long loginDateTo){
        FraudMemberReportFilter filter = (FraudMemberReportFilter) getPaginationInfo(new FraudMemberReportFilter(memberCode, relateTypes, 7));        
         if(loginDateFrom >0 &&loginDateTo > 0 ){
            filter.setFromLoginDate(new Date(loginDateFrom));
            filter.setToLoginDate(new Date(loginDateTo));
        }
        
        List<FraudMemberDto> findFraudMember = fraudMemberReport.findFraudMember(new String[]{memberCode}, filter.getRelateTypes().toArray(new FraudMemberRelateType[0]), filter.getFromLoginDate(), filter.getToLoginDate());
        return new PaginateRecordJSON(filter, findFraudMember.toArray());
    }
}


package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.modelsb.dto.DailyFinanceRptDto;
import com.pr7.sb.service.AffiliateSessionLogService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import java.util.Date;
import java.util.List;
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
@RequestMapping("/sv/report")
@Authentication(type={UserType.BO})
public class ReportService {

    private static final Logger logger = LogManager.getLogger(ReportService.class);

    @Autowired
    private com.pr7.sb.service.ReportService reportService;
    
    @Autowired
    private AffiliateSessionLogService affiliateSessionLogService;
    
    @Authorization(hasAnyPermission={Permissions.ListFinancialReportDaily})
    @RequestMapping("/searchFinanceDaily.sv")
    @ResponseBody
    public Object searchFinanceDailyHandler(            
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate) throws Exception {        
        Date fromDate = new Date(startDate);
        Date toDate = new Date(endDate);
        
        List<DailyFinanceRptDto> list = reportService.getDailyFinanceRpt(fromDate, toDate);
        return list;
    }

    @Authorization(hasAnyPermission={Permissions.ListIpAddressReport})
    @RequestMapping("/searchIpAddress.sv")
    @ResponseBody
    public Object searchIpAddress(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="0") int rp,
            @RequestParam(defaultValue="") String userid,
            @RequestParam(defaultValue="") String ipaddress,
            @RequestParam(defaultValue="") Long startDate,
            @RequestParam(defaultValue="") Long endDate) throws Exception {        
        Date fromDate = new Date(startDate);
        Date toDate = new Date(endDate);
        
        PaginateRecordJSON paginateRecords = new PaginateRecordJSON(page,rp);
        paginateRecords.setRecords(affiliateSessionLogService.findByIp(userid, ipaddress, fromDate, toDate).toArray());

        return paginateRecords;
    }
}

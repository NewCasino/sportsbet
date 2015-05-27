/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.affiliate.web.services;

import com.pr7.affiliate.session.UserSession;
import com.pr7.common.web.localization.I18n;
import com.pr7.modelsb.dto.AffiliateFilter;
import com.pr7.modelsb.dto.AffiliateReportDto;
import com.pr7.modelsb.entity.AffiliateArchiveLog;
import com.pr7.sb.service.AffiliateService;
import com.pr7.server.common.Authentication;
import com.pr7.server.constants.UserType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/misc")
public class MiscService extends BaseService{
    
    @Value("${affiliate.report.archive.minMonth}")
    private String minDateStr;    
    
    @Autowired
    private UserSession userSession;
    
    @Autowired
    private AffiliateService affiliateService;
    
    private static final Logger logger = LogManager.getLogger(MiscService.class);
    
    @RequestMapping("/searchReport")
    @ResponseBody
    @Authentication(type = UserType.AFFILIATE)
    public Object searchReportHandler(
            @RequestParam(defaultValue="") String loginId,
            @RequestParam(defaultValue="") Date startDate,
            @RequestParam(defaultValue="") Date endDate) {
        
        if (StringUtils.isBlank(userSession.getLoginId())) {
            return new ArrayList<AffiliateReportDto>();
        }
        
        AffiliateFilter filter = new AffiliateFilter();
        filter.setMemberCode(loginId);
        filter.setCreatedFrom(startDate);
        filter.setCreatedTo(endDate);
        filter.setUserName(userSession.getLoginId());
        filter.setHideMemberCode(Boolean.TRUE);
        return affiliateService.getBetTurnOverByAffiliateFilter(filter);
    }
    
    @RequestMapping("/searchReportSummary")
    @ResponseBody
    @Authentication(type = UserType.AFFILIATE)
    public Object searchReportSummaryHandler(            
            @RequestParam(defaultValue="") Long startDate,
            @RequestParam(defaultValue="") Long endDate) {
        
        if (StringUtils.isBlank(userSession.getLoginId())) {
            return new ArrayList<AffiliateReportDto>();
        }
        
        AffiliateFilter filter = new AffiliateFilter();
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setUserName(userSession.getLoginId());
        
        return affiliateService.getAffiliateReportSummaryWithMemberTierSetting(filter, true, true);
    }

    @RequestMapping("/archivedAffUserDevReport")
    @ResponseBody
    @Authentication(type = UserType.AFFILIATE)
    public Object archivedAffUserDevReport(
            @RequestParam(defaultValue="") String affUsername,
            @RequestParam(defaultValue="") String affCode,
            @RequestParam(defaultValue="") String affType,
            @RequestParam(defaultValue="") Long startDate,
            @RequestParam(defaultValue="") Long endDate,
            @RequestParam(defaultValue="") String monthStr,
            @RequestParam(defaultValue="ALL") AffiliateFilter.DisplayMode displayMode
            ) throws ParseException
    {
        Date date = DateUtils.parseDate(monthStr + "01", new String []{"yyyyMMdd"});
        Date currentDate = new Date();
        Date maxThreshold = DateUtils.addMonths(currentDate, -3);
        maxThreshold = DateUtils.setDays(maxThreshold, 1);
        SimpleDateFormat sdfMonthInt = new SimpleDateFormat("MM");
        SimpleDateFormat sdfYearInt = new SimpleDateFormat("yyyy");
        String intYearPeriod = " " + sdfYearInt.format(new Date(startDate));
        String strMonthPeriod = getMonthName( Integer.parseInt(sdfMonthInt.format(new Date(startDate)) ));
        String reportPeriod = strMonthPeriod + intYearPeriod;
        if (date.before(maxThreshold)) 
        {
            //check whether both 2 dates are same
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if(! df.format(date).equalsIgnoreCase(df.format(maxThreshold)))
            {
                logger.debug("archivedAffUserDevReport: Selected date less than maximal previous date threshold. Threshold is 3 months before current date");
                logger.debug("Return : LESS_THAN_THRESHOLD, MaxPreviousDate: " + df.format(maxThreshold) + " ,Selected Date: " + df.format(date));
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("warning", I18n._n("Please select a month not less than previous 3 months from today"));
                result.put("month",reportPeriod);
                return result; //next handled by javascript
            }
        }
        if(StringUtils.isBlank(minDateStr) || minDateStr.equalsIgnoreCase("${affiliate.report.archive.minMonth}")){
            minDateStr = "201210";
        }        
        Date minArchiveDate = DateUtils.parseDate(minDateStr + "01", new String []{"yyyyMMdd"});
        
        if (date.before(minArchiveDate) || StringUtils.isBlank(userSession.getLoginId())) {
            return new ArrayList<AffiliateReportDto>();
        }
        
        AffiliateFilter filter = new AffiliateFilter();
        filter.setDisplayMode(displayMode);
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setUserName(userSession.getLoginId());
        filter.setAffiliateCode(affCode);
        filter.setAffiliateType(affType);
        
        AffiliateArchiveLog affiliateArchiveLog = affiliateService.getActiveArchiveByMonth(filter.getCreatedFrom());
        Map<String, Object> result = new HashMap<String, Object>();
        if (affiliateArchiveLog == null) {
            result.put("result", affiliateService.getAffiliateReportSummaryWithMemberTierSetting(filter, true, true));
        }
        else
        {
            result.put("archived", affiliateArchiveLog);
            result.put("result", affiliateService.getArchivedAffiliateReportSummary(affiliateArchiveLog.getId(), filter));
        }
        result.put("month",reportPeriod);
        return result;
    }
    private String getMonthName(int index)
    {
        String arr[] ={"January","February","March","April","May","June","July","August", "September","October", "November","December"};
        String result="";
        result = I18n._n(arr[index-1]);
        return result;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.session.BOUserSession;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.modelsb.constant.AffiliateStatus;
import com.pr7.modelsb.constant.AffiliateWLType;
import com.pr7.modelsb.constant.ApplicationStatus;
import com.pr7.modelsb.dto.*;
import com.pr7.modelsb.entity.Affiliate;
import com.pr7.modelsb.entity.AffiliateArchiveLog;
import com.pr7.modelsb.entity.AffiliateSetting;
import com.pr7.sb.service.AffiliateCommissionService;
import com.pr7.sb.service.AffiliateDeviationSettingService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.server.utils.EmailSender;
import flexjson.JSONDeserializer;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import org.apache.commons.io.IOUtils;
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
import org.stringtemplate.v4.ST; 
/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/sv/affiliate")
@Authentication(type = {UserType.BO})
public class AffiliateService {

    @Autowired
    private com.pr7.sb.service.AffiliateService affiliateService;
    @Autowired
    private AffiliateDeviationSettingService percentageService;
    @Autowired
    private AffiliateCommissionService affiliateCommissionService;
    @Autowired
    BOUserSession session;
    @Autowired
    private EmailSender emailSender;

    private static final Logger logger = LogManager.getLogger(AffiliateService.class);
    
    @Value("${affiliate.report.archive.minMonth}")
    private String minDateStr;    
        
    @Authorization(hasAnyPermission = {Permissions.ListAffiliate})
    @RequestMapping("/search.sv")
    @ResponseBody
    public Object searchHandler(
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String applicationStatus) {
        AffiliateFilter filter = new AffiliateFilter();
        filter.setUserName(username);
        filter.setCode(code);
        filter.setStatus(status);
        filter.setApplicationStatus(applicationStatus);
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        return affiliateService.getAffiliateListByFilter(filter);
    }

    @Authorization(hasAnyPermission = {Permissions.ListAffiliateReport})
    @RequestMapping("/searchReport.sv")
    @ResponseBody
    public Object searchHandler(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rp,
            @RequestParam(defaultValue = "") String sortname,
            @RequestParam(defaultValue = "asc") String sortorder,
            @RequestParam(defaultValue = "") String loginId,
            @RequestParam(defaultValue = "") String affUsername,
            @RequestParam(defaultValue = "0") Long startDate,
            @RequestParam(defaultValue = "0") Long endDate) {
        AffiliateFilter filter = new AffiliateFilter();
        filter.setMemberCode(loginId);
        filter.setUserName(affUsername);
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setHideMemberCode(Boolean.FALSE);
        filter.setSortByCols(sortname);
        filter.setOrderBy("asc".equalsIgnoreCase(sortorder) ? BaseFilter.SortOrder.ASC : BaseFilter.SortOrder.DESC);

        return new PaginateRecordJSON(page, rp, affiliateService.getBetTurnOverByAffiliateFilter(filter).toArray());

    }

    @Authorization(hasAnyPermission = {Permissions.EditAffiliate})
    @RequestMapping("/getSetting.sv")
    @ResponseBody
    public Object getSettingHandler(
            @RequestParam(defaultValue = "-1") Long affiliateId) {
        AffiliateSettingDto result = new AffiliateSettingDto();

        Affiliate affiliate = affiliateService.getById(affiliateId);
        AffiliateSetting affiliateSetting = affiliateService.getAffiliateSetting(affiliateId);
        result.copyFromEntity(affiliate, affiliateSetting, affiliateCommissionService.getDefaultCommissionRules());

        return result;
    }

    @Authorization(hasAnyPermission = {Permissions.EditAffiliate})
    @RequestMapping("/updateStatus.sv")
    @ResponseBody
    public Object updateStatusHandler(
            @RequestParam(defaultValue = "") Long id,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String applicationStatus) {
        Affiliate affiliate = affiliateService.getById(id);

        if (StringUtils.isNotBlank(status)) {
            affiliate.setStatus(status);
        }
        if (StringUtils.isNotBlank(applicationStatus)) {
            affiliate.setApplicationStatus(applicationStatus);

            if (applicationStatus.equals(ApplicationStatus.APPROVED.toString())) {
                affiliate.setStatus(AffiliateStatus.ACTIVE.toString());
                //sending Email indicating approved affiliate
                  // send email notification
                try {
                    String registeredLangId = StringUtils.defaultIfBlank( affiliate.getLangId(),"zh_CN");
                    String langId = "";
                    if(!registeredLangId.equalsIgnoreCase("en_us"))
                    {
                        langId = "."+registeredLangId;
                    }
                    String template = "approval.template".concat(langId).concat(".html");
                    InputStream file = getClass().getClassLoader().getResource(template).openStream();
                    ST st = new ST(IOUtils.toString(file, "UTF-8"), '$', '$');
                    st.add("user", affiliate);
                        String to = affiliate.getEmail();
                    String subject = "Welcome to BETMART Business!";
                    if (registeredLangId.equalsIgnoreCase("zh_CN"))
                    {
                        subject = "欢迎您加入博坊商业合作!";
                    }
                    String body = st.render();
                    logger.debug (String.format(">>> Email Content:\n To:%s\nSubject:%s\nBody:%s",to,subject,body));
                    emailSender.send(to, subject, body);
                    logger.debug(">>> Email Sent Successfully");
                    file.close();
                } catch (MessagingException ex) {
                    logger.error(ex);
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }

            if (applicationStatus.equals(ApplicationStatus.REJECTED.toString())) {
                affiliate.setStatus(AffiliateStatus.INACTIVE.toString());
            }
        }

        affiliateService.update(affiliate);

        AffiliateDto result = new AffiliateDto();
        result.copyFromEntity(affiliate);
        return result;
    }

    @Authorization(hasAnyPermission = {Permissions.EditAffiliate})
    @RequestMapping("/active-carry-over.sv")
    @ResponseBody
    public Object activeCarryOver(long aid) {
        return affiliateService.activeCarryOver(aid);
    }

    @Authorization(hasAnyPermission = {Permissions.EditAffiliate})
    @RequestMapping("/update.sv")
    @ResponseBody
    public Object updateAffiliateHandler(
            @RequestParam(defaultValue = "") String json) {
        AffiliateDto affDto = new JSONDeserializer<AffiliateDto>().deserialize(json.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.dto.AffiliateDto\" }"));
        Affiliate affiliate = affiliateService.getById(affDto.getId());
        if (affiliate != null) {
            affiliate.setFirstName(affDto.getFirstName());
            affiliate.setLastName(affDto.getLastName());
            affiliate.setCode(affDto.getCode());
            affiliate.setContactNo(affDto.getContactNo());
            affiliate.setCountry(affDto.getCountry());
            affiliate.setAddress(affDto.getAddress());;
            affiliate.setCity(affDto.getCity());
            affiliate.setPostalCode(affDto.getPostalCode());
            affiliate.setState(affDto.getState());
            affiliate.setGender(affDto.getGender());
            affiliateService.update(affiliate);
        }
        return affiliate != null;
    }

    @Authorization(hasAnyPermission = {Permissions.EditAffiliate})
    @RequestMapping("/updateSettings.sv")
    @ResponseBody
    public Object updateSettingHandler(
            @RequestParam(defaultValue = "") String json) {
        AffiliateSettingDto affSettingDto = new JSONDeserializer<AffiliateSettingDto>().deserialize(json.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.dto.AffiliateSettingDto\" }"));

        // ignore type and bizmodel as per now
        // update affiliate 
//        Affiliate affiliate = affiliateService.getById(affSettingDto.getAffiliateId());
//        if(affiliate != null){
//            affiliate.setType(affSettingDto.getType());
//            affiliate.setBizModel(affSettingDto.getBizModel());
//            affiliateService.update(affiliate);
//        }

        if (affSettingDto.getTier4Max() < 0) { // if infinity
            affSettingDto.setTier4Max(2147483647.0);
        }

        // update affiliate setting
        affiliateService.updateSettings(affSettingDto.toAffiliateSetting());

        return true;
    }

    @Authorization(hasAnyPermission = {Permissions.ListAffiliateReport})
    @RequestMapping("/affUserReport.sv")
    @ResponseBody
    public Object searchAffiliateUserReport(
            @RequestParam(defaultValue = "") String affUsername,
            @RequestParam(defaultValue = "") String affCode,
            @RequestParam(defaultValue = "") String affType,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "ALL") AffiliateFilter.DisplayMode displayMode) {
        AffiliateFilter filter = new AffiliateFilter();
        filter.setDisplayMode(displayMode);
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setUserName(affUsername);
        filter.setAffiliateCode(affCode);
        filter.setAffiliateType(affType);

        //#904: SIT : Affiliate Report (Actual) to calculate Affiliate Tier and Earning based on Affiliate Tier Setting
        //return affiliateService.getAffiliateReportSummary(filter, false, false);
        return affiliateService.getAffiliateReportSummaryWithMemberTierSetting(filter, false, false);
    }

    @Authorization(hasAnyPermission = {Permissions.ListAffiliateDeviationReport})
    @RequestMapping("/affUserDeviationReport.sv")
    @ResponseBody
    public Object searchAffiliateDeviationReport2(
            @RequestParam(defaultValue = "") String affUsername,
            @RequestParam(defaultValue = "") String affCode,
            @RequestParam(defaultValue = "") String affType,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "ALL") AffiliateFilter.DisplayMode displayMode) {
        AffiliateFilter filter = new AffiliateFilter();
        filter.setDisplayMode(displayMode);
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setUserName(affUsername);
        filter.setAffiliateCode(affCode);
        filter.setAffiliateType(affType);
        return affiliateService.getAffiliateReportSummaryWithMemberTierSetting(filter, true, false);
    }

    @Authorization(hasAnyPermission = {Permissions.ListAffiliateDeviationReport})
    @RequestMapping("/archivedAffUserDevReport.sv")
    @ResponseBody
    public Object archivedAffUserDevReport(
            @RequestParam(defaultValue = "") String affUsername,
            @RequestParam(defaultValue = "") String affCode,
            @RequestParam(defaultValue = "") String affType,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "ALL") AffiliateFilter.DisplayMode displayMode) {
        AffiliateFilter filter = new AffiliateFilter();
        filter.setDisplayMode(displayMode);
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setUserName(affUsername);
        filter.setAffiliateCode(affCode);
        filter.setAffiliateType(affType);

        AffiliateArchiveLog affiliateArchiveLog = affiliateService.getActiveArchiveByMonth(filter.getCreatedFrom());
        if (affiliateArchiveLog == null) {
            return affiliateService.getAffiliateReportSummaryWithMemberTierSetting(filter, true, false);
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("archived", affiliateArchiveLog);
        result.put("result", affiliateService.getArchivedAffiliateReportSummary(affiliateArchiveLog.getId(), filter));
        return result;
    }

    @Authorization(hasAnyPermission = {Permissions.ArchiveAffiliateDeviationReport})
    @RequestMapping("/archiveAffiliateDeviationReport.sv")
    @ResponseBody
    public Object archivedAffUserDevReport(@RequestParam(defaultValue = "") Long month, @RequestParam(defaultValue = "") String monthStr) throws ParseException {
        Date date = DateUtils.parseDate(monthStr + "01", new String []{"yyyyMMdd"});        
        if(StringUtils.isBlank(minDateStr) || minDateStr.equalsIgnoreCase("${affiliate.report.archive.minMonth}")){
            minDateStr = "201210";
        }        
        Date minArchiveDate = DateUtils.parseDate(minDateStr + "01", new String []{"yyyyMMdd"});
        
        if(date.before(minArchiveDate)){
            throw new IllegalArgumentException("You are not allowed to archive report for months before " + DateFormatUtils.format(minArchiveDate, "yyyy MMM"));
        }
        
        affiliateService.archiveAffiliateReport(new Date(month), session.getLoginId());
        return "SUCCESS";
    }

    @Authorization(hasAllPermission = {Permissions.ListAffiliateDeviationSettings, Permissions.EditAffiliateDeviationSettings})
    @RequestMapping("/list-wl-percentage.sv")
    @ResponseBody
    public Object listAffiliateWLPercentage(
            @RequestParam(defaultValue = "") String month,
            @RequestParam(defaultValue = "") String affiliateCode,
            @RequestParam(defaultValue = "ALL") String type) {
        if (StringUtils.isBlank(month) || !month.matches("^(1[0-2]|0[1-9])\\d{4}$")) {
            throw new IllegalArgumentException("Month should should be in MMyyyy format.");
        }
        AffiliateWLType awlt = AffiliateWLType.valueOf(type);

        AffiliateWLPercentageFilter filter = new AffiliateWLPercentageFilter();

        filter.setMonth(month);
        filter.setType(awlt);
        filter.setAffiliateCode(affiliateCode);

        return percentageService.getAffilateWLPercenage(filter);
    }

    @Authorization(hasAllPermission = {Permissions.EditAffiliateDeviationSettings})
    @RequestMapping("/update-wl-percentage.sv")
    @ResponseBody
    public Object updateAffiliateWLPercentage(
            @RequestParam String month,
            @RequestParam String affiliateCode,
            @RequestParam AffiliateWLType type,
            @RequestParam float percentage) {

        if (StringUtils.isBlank(month) || !month.matches("^(1[0-2]|0[1-9])\\d{4}$")) {
            throw new IllegalArgumentException("Month should should be in MMyyyy format.");
        }
        if (type == AffiliateWLType.ALL) {
            for (AffiliateWLType affiliateWLType : AffiliateWLType.values()) {
                if (affiliateWLType != AffiliateWLType.ALL) {
                    percentageService.addOrUpdatePercentage(affiliateCode, affiliateWLType, month, percentage);
                }
            }
            return true;
        } else {
            return percentageService.addOrUpdatePercentage(affiliateCode, type, month, percentage);
        }
    }

    @Authorization(hasAllPermission = {Permissions.ListAffiliateReport})
    @RequestMapping("/get-carry-over-balance.sv")
    @ResponseBody
    public Object getCarryOverBalance(int affiliateId) {
        //Only get carry over balance from Nov 2012
        return affiliateService.getCarryOverBalance(affiliateId, "201211");
    }
}
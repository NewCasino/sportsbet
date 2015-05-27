/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.BaseFilterService;
import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.constant.MemberMigrationStatus;
import com.pr7.modelsb.constant.MemberStatus;
import com.pr7.modelsb.constant.MigrationStatus;
import com.pr7.modelsb.dto.ExternalMemberFilter;
import com.pr7.modelsb.dto.MemberMigrationFilter;
import com.pr7.sb.service.MemberMigrationService;
import com.pr7.sbasc.service.MemberMigrationAscService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.GenericValidator;
import java.math.BigDecimal;
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
@RequestMapping(value = "/sv/migration/")
@Authentication(type = UserType.BO)
public class MemberMigrationBOService extends BaseFilterService{
    private static final Logger _logger = LogManager.getLogger(MemberMigrationBOService.class);
    
    @Autowired
    MemberMigrationService memberMigrationService;
    @Autowired
    MemberMigrationAscService memberMigrationAscService;
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
    @RequestMapping("migrate.sv")
    @Authorization(hasAnyPermission={Permissions.EditMemberMigration})
    @ResponseBody
    public Object doMigrate(int memberId, String deltaBalance, String betCredit,ExtProductVendor vendor){
        _logger.debug("doMigrate STARTED:: memberId = " + memberId + ", deltaBalance = " + deltaBalance + ", betCredit = " + betCredit);        
        BigDecimal fBeltaBalance = GenericValidator.tryParseBigDecimal(deltaBalance);
        BigDecimal fBetCredit = GenericValidator.tryParseBigDecimal(betCredit, BigDecimal.ZERO);
        boolean result = false;
        switch(vendor){
            case SPORTBOOK_SB8:
              result= memberMigrationService.migrateMember(memberId, fBeltaBalance, fBetCredit);break;
            case SBASC:
              result = memberMigrationAscService.migrateMember(memberId, fBeltaBalance, fBetCredit);break;
        }
        return result;
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
    @Authorization(hasAnyPermission={Permissions.EditMemberMigration})
    @ResponseBody
    public Object listExtMemberMap(            
            @RequestParam(defaultValue="") String memberCode,
            @RequestParam(defaultValue="") String exMemberCode,
            @RequestParam(defaultValue="ACTIVE") MemberStatus status,
            @RequestParam(defaultValue="") String agentCode,
            @RequestParam(defaultValue="") String affCode,
            @RequestParam(defaultValue="NOT_DONE") MemberMigrationStatus memberMigrationStatus,
            @RequestParam(defaultValue="PROCESSING") MigrationStatus migrationStatus,
            @RequestParam(defaultValue="REGISTER_DATE") ExternalMemberFilter.DateFilterType dateFilterType,
            ExtProductVendor vendor,
            long dateFrom,
            long dateTo
            ){

        MemberMigrationFilter filter = new MemberMigrationFilter();
        getPaginationInfo(filter);
        filter.setMemberCode(memberCode);
        filter.setExMemberCode(exMemberCode);
        filter.setDateFilterType(dateFilterType);
        filter.setStatus(status);
        filter.setVendor(vendor);
        filter.setCreatedDateFrom(new Date(dateFrom));
        filter.setCreatedDateTo(new Date(dateTo));
        filter.setAgentCode(agentCode);
        filter.setAffCode(affCode);
        filter.setMemberMigrationStatus(memberMigrationStatus);
        filter.setMigrationStatus(migrationStatus);
        
        PaginateRecordJSON paginateRecords = new PaginateRecordJSON(filter.getPageNo(),filter.getRecordsPerPage());
        switch(vendor){
            case SPORTBOOK_SB8:
                paginateRecords.setRecords(memberMigrationService.filterMemberMigrationInfo(filter).toArray());
                break;
            case SBASC:
                paginateRecords.setRecords(memberMigrationAscService.filterMemberMigrationInfo(filter).toArray());
                break;
        }

        return paginateRecords;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.BaseFilterService;
import com.pr7.backoffice.constants.Permissions;
import com.pr7.modelsb.constant.DbActionType;
import com.pr7.modelsb.dto.AuditLogFilter;
import com.pr7.modelsb.entity.AuditLog;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pr7.backoffice.web.json.PaginateRecordJSON;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/sv/auditlog")
@Authentication(type = {UserType.BO})
@Authorization(hasAnyPermission = {Permissions.ListAudit})
public class AuditLogService extends BaseFilterService{
    
    @Autowired
    private com.pr7.sb.service.AuditLogService auditLogService;
    
    @RequestMapping("/findAuditLog.sv")
    @ResponseBody   
    
    public Object listAuditLog(
            @RequestParam(defaultValue="") String lastUpdateBy, 
            @RequestParam(defaultValue="") String table, 
            @RequestParam(defaultValue="") String action,
            @RequestParam(defaultValue="") String observations,
            long startDate,
            long endDate) {     
        
        AuditLogFilter filter = new AuditLogFilter(); 
        getPaginationInfo(filter);
        filter.setStartDate(new Date(startDate));
        filter.setEndDate(new Date(endDate));
        filter.setModifier(lastUpdateBy);
        filter.setObservations(observations);
        filter.setTableName(table);
        if(StringUtils.isNotBlank(action)){
            filter.setActionType(DbActionType.valueOf(action));
        }
        List<AuditLog> dtos = auditLogService.getAuditLogListByFilter(filter);                
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(filter.getPageNo(), filter.getRecordsPerPage());                        
        paginateRecordJSON.setRecords(dtos.toArray());
        return paginateRecordJSON;
    }
}

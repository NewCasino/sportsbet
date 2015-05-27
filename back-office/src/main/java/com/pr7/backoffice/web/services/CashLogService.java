package com.pr7.backoffice.web.services;

import com.pr7.backoffice.BaseFilterService;
import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.session.BOUserSession;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.modelsb.constant.TransactionStatus;
import com.pr7.modelsb.constant.TransactionType;
import com.pr7.modelsb.dto.CashLogDto;
import com.pr7.modelsb.dto.CashLogFilter;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import java.util.Date;
import java.util.List;
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
@RequestMapping("/sv/log")
@Authentication(type = {UserType.BO})
@Authorization(hasAnyPermission = {Permissions.ListCashLog, Permissions.EditCashLog})
public class CashLogService extends BaseFilterService {

    @Autowired
    BOUserSession session;

    @Autowired
    com.pr7.sb.service.CashLogService cashLogService;

    @RequestMapping("/searchCashLog.sv")
    @ResponseBody
    public Object searchCashLogHandler(
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate) {
        return cashLogService.findCashLog(memberCode, new Date(startDate), new Date(endDate));
    }

    @RequestMapping("/filterCashLog.sv")
    @ResponseBody
    public Object filterCashLogHandler(

            @RequestParam(defaultValue = "ALL") TransactionStatus status,
            @RequestParam(defaultValue = "ALL") TransactionType transactionType,
            @RequestParam(defaultValue = "") String refNo,
            @RequestParam String memberCode,
            @RequestParam Long startDate,
            @RequestParam Long endDate) {
        CashLogFilter filter = new CashLogFilter();
        getPaginationInfo(filter);
        filter.setMemberCode(memberCode);
        filter.setDtFrom(new Date(startDate));
        filter.setDtTo(new Date(endDate));
        filter.setRefNoLike(refNo);
        filter.setStatus(status);
        filter.setTransactionType(transactionType);
        List<CashLogDto> filterCashLog = cashLogService.filterCashLog(filter);
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(filter.getPageNo(), filter.getRecordsPerPage(), filterCashLog.toArray());
        return paginateRecordJSON;
    }

    @RequestMapping("/updateCashLog.sv")
    @ResponseBody
    @Authorization(hasAllPermission = {Permissions.EditCashLog})
    public Object updateCashLogHandler(Long id, String status) {
        return cashLogService.updateCashLogStatus(id, TransactionStatus.valueOf(status), session.getLoginId());
    }
}

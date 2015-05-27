/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.keno.KenoWSException;
import com.pr7.keno.constant.KenoGameType;
import com.pr7.keno.constant.KenoStatus;
import com.pr7.keno.entity.KenoTransaction;
import com.pr7.keno.model.KenoFilter;
import com.pr7.keno.services.KenoServiceImpl;
import com.pr7.modelsb.constant.PurposeReportEnum;
import com.pr7.modelsb.dto.CasinoTransactionDto;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.DateUtil;
import com.pr7.util.excel.ColumnMap;
import com.pr7.util.excel.ExcelGenerator;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller("KenoServiceController")
@RequestMapping("/sv/keno")
@Authentication(type = UserType.BO)
public class KenoService {
    private static final Logger logger = LogManager.getLogger(KenoServiceImpl.class);
    @Autowired
    private com.pr7.keno.services.KenoService kenoService;

    @ResponseBody
    @RequestMapping("/getPnL.sv")
    @Authorization(hasAnyPermission = {Permissions.ListAffiliateReport})
    public Object getKenoProfitAndLoss(long date, com.pr7.keno.services.KenoService.PnLHourType hourType) throws KenoWSException {
        return kenoService.getProfitLoss(new Date(date), hourType);
    }

    @ResponseBody
    @RequestMapping("/getPnLByDate.sv")
    @Authorization(hasAnyPermission = {Permissions.ListAffiliateReport})
    public Object getKenoProfitAndLoss(String date, com.pr7.keno.services.KenoService.PnLHourType hourType) throws KenoWSException {
        return kenoService.getProfitLoss(date, hourType);
    }

    @ResponseBody
    @RequestMapping("/searchKenoTransaction.sv")
    @Authorization(hasAnyPermission = {Permissions.ListKenoTransactions})
    public Object searchKenoTransaction(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String gameCode,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "0") int betType,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "MATCH_DATE") PurposeReportEnum purpose) throws KenoWSException {
        KenoFilter filter = new KenoFilter();
        filter.setTransactionId(transactionId);
        filter.setMemberCode(memberCode);
        filter.setGameCode(gameCode);
        filter.setStartDate(new Date(startDate));
        filter.setEndDate(new Date(endDate));
        filter.setKenoGameType(KenoGameType.parseFromValue(betType));
        filter.setStatus(KenoStatus.parseFromValue(status));
        filter.setPurposeFilter(purpose);
      
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(page, rows);
        List<KenoTransaction> dtos = kenoService.searchKenoTransaction(filter);
        Map<Object,Object> result=new HashMap<Object, Object>();
        result.put("lastUpdateDate",kenoService.getLastUpdateDate(memberCode));
        paginateRecordJSON.setUserdata(result);
        paginateRecordJSON.setRecords(dtos.toArray());
        return paginateRecordJSON;
    }
   
    
    @Authorization(hasAnyPermission = {Permissions.ListKenoTransactions})
    @RequestMapping("/generateExcel.sv")
    @ResponseBody
    public void generateReportExcel(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String gameCode,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "0") int betType,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "binary") String type,
            @RequestParam(defaultValue="480") final int tz,
            @RequestParam(defaultValue = "MATCH_DATE") PurposeReportEnum purpose
            ) throws Exception {
        Date start = new Date(startDate);
        Date end = new Date(endDate);
        KenoFilter filter = new KenoFilter();
        filter.setTransactionId(transactionId);
        filter.setMemberCode(memberCode);
        filter.setGameCode(gameCode);
        filter.setStartDate(new Date(startDate));
        filter.setEndDate(new Date(endDate));
        filter.setKenoGameType(KenoGameType.parseFromValue(betType));
        filter.setStatus(KenoStatus.parseFromValue(status));
        filter.setPurposeFilter(purpose);
      
        List<KenoTransaction> kenoTransactions = kenoService.searchKenoTransaction(filter);

         ColumnMap.Validator dateTZConvert = new ColumnMap.Validator() {            
            @Override
            public Object preProcessData(Object value, Object obj, ColumnMap map, int rowIndex, int colIndex) {
                if(value != null){
                    Date date = (Date)value;                    
                    value = DateUtil.serverToClientDate(date, tz);
                }
                return value;
            }
        };
        ExcelGenerator excelGenerator = new ExcelGenerator(
                "Keno-Report_" + transactionId + "_" + memberCode + "_" + DateFormatUtils.format(start, "yyyyMMdd") + "_" + DateFormatUtils.format(end, "yyyyMMdd"), kenoTransactions, Arrays.asList(
                new ColumnMap("id", "Id"),
                new ColumnMap("transactionId", "Trans ID", "text", ""),
                new ColumnMap("memberCode", "Member Code", "text", ""),
                new ColumnMap("gameCode", "Game Code", "text", ""),
                new ColumnMap("betType", "Bet Type", "text", ""),
                new ColumnMap("betAmount", "Bet Amount", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("vto", "VTO", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("winloss", "Win Loss", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("selection", "Selection", "text", "-"),
                new ColumnMap("result", "Result", "text", "-"),
                new ColumnMap("marketDesc", "Market", "text", "-"),
                new ColumnMap("dateCreated", "Date Created", "dd mmm yyyy h:mm:ss", "",dateTZConvert),
                new ColumnMap("datePayout", "Date Payout", "dd mmm yyyy h:mm:ss", "",dateTZConvert),
                new ColumnMap("statusStr", "Status", "text", ""),
                new ColumnMap("currentBalance", "Current Balance", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("betDetail", "Detail", "text", "")), ExcelGenerator.ExcelFileFormat.XLS);
        if ("binary".equalsIgnoreCase(type)) {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + excelGenerator.getFileName() + excelGenerator.getFileExtension());
            excelGenerator.write(response.getOutputStream());
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            excelGenerator.write(outputStream);
            if (StringUtils.defaultIfBlank(request.getHeader("Accept-Encoding"), "").contains("gzip")) {
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(response.getOutputStream());
                response.setHeader("Content-Encoding", "gzip");
                IOUtils.write(Base64.encodeBase64(outputStream.toByteArray()), gzipOutputStream);
                gzipOutputStream.finish();
            } else {
                IOUtils.write(Base64.encodeBase64(outputStream.toByteArray()), response.getOutputStream());
            }
        }
    }
    
    @Authorization(hasAnyPermission = {Permissions.ListBetInquiry})
    @RequestMapping("/generateKenoReportExcel.sv")
    @ResponseBody
    public void generateKenoReportExcel(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String gameCode,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "0") int betType,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue="480") final int tz,
            @RequestParam(defaultValue = "binary") String type,
            @RequestParam(defaultValue = "MATCH_DATE") PurposeReportEnum purpose
            ) throws Exception {
        
        Date start = new Date(startDate);
        Date end = new Date(endDate);
        if(!isDateRangeValid(start, end)) //if (!isFromDateValid(start, -1))
            {
                response.getOutputStream().print("INVALID_STATE");//handled later by javascript
                return;
            }
            // inspect whether memberCode is empty or not
            if (memberCode.trim() == "") {
                response.getOutputStream().print("INVALID_FILTER");
                return;
            }
        KenoFilter filter = new KenoFilter();
        filter.setTransactionId(transactionId);
        filter.setMemberCode(memberCode);
        filter.setGameCode(gameCode);
        filter.setStartDate(new Date(startDate));
        filter.setEndDate(new Date(endDate));
        filter.setKenoGameType(KenoGameType.parseFromValue(betType));
        filter.setStatus(KenoStatus.parseFromValue(status));
        filter.setPurposeFilter(purpose);
      
        List<KenoTransaction> kenoTransactions = kenoService.searchKenoTransaction(filter);

         ColumnMap.Validator dateTZConvert = new ColumnMap.Validator() {            
            @Override
            public Object preProcessData(Object value, Object obj, ColumnMap map, int rowIndex, int colIndex) {
                if(value != null){
                    Date date = (Date)value;                    
                    value = DateUtil.serverToClientDate(date, tz);
                }
                return value;
            }
        };
        ExcelGenerator excelGenerator = new ExcelGenerator(
                "Keno-Report_" + transactionId + "_" + memberCode + "_" + DateFormatUtils.format(start, "yyyyMMdd") + "_" + DateFormatUtils.format(end, "yyyyMMdd"), kenoTransactions, Arrays.asList(
                new ColumnMap("id", "Id"),
                new ColumnMap("transactionId", "Trans ID", "text", ""),
                new ColumnMap("memberCode", "Member Code", "text", ""),
                new ColumnMap("gameCode", "Game Code", "text", ""),
                new ColumnMap("betType", "Bet Type", "text", ""),
                new ColumnMap("betAmount", "Bet Amount", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("vto", "VTO", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("winloss", "Win Loss", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("selection", "Selection", "text", "-"),
                new ColumnMap("result", "Result", "text", "-"),
                new ColumnMap("marketDesc", "Market", "text", "-"),
                new ColumnMap("dateCreated", "Date Created", "dd mmm yyyy h:mm:ss", "",dateTZConvert),
                new ColumnMap("datePayout", "Date Payout", "dd mmm yyyy h:mm:ss", "",dateTZConvert),
                new ColumnMap("statusStr", "Status", "text", ""),
                new ColumnMap("currentBalance", "Current Balance", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("betDetail", "Detail", "text", "")), ExcelGenerator.ExcelFileFormat.XLS);
        if ("binary".equalsIgnoreCase(type)) {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + excelGenerator.getFileName() + excelGenerator.getFileExtension());
            excelGenerator.write(response.getOutputStream());
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            excelGenerator.write(outputStream);
            if (StringUtils.defaultIfBlank(request.getHeader("Accept-Encoding"), "").contains("gzip")) {
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(response.getOutputStream());
                response.setHeader("Content-Encoding", "gzip");
                IOUtils.write(Base64.encodeBase64(outputStream.toByteArray()), gzipOutputStream);
                gzipOutputStream.finish();
            } else {
                IOUtils.write(Base64.encodeBase64(outputStream.toByteArray()), response.getOutputStream());
            }
        }
    }
    
   private boolean isDateRangeValid(Date fromDate, Date toDate)
    {
        boolean result = true;
        SimpleDateFormat fmtInt = new SimpleDateFormat("yyyyMMdd");
        Calendar calThreshold = Calendar.getInstance();
        calThreshold.setTime(toDate);
        calThreshold.add(Calendar.DATE, -31);
        int intThreshold = Integer.parseInt( fmtInt.format(calThreshold.getTime()));
        int intFrom =Integer.parseInt( fmtInt.format(fromDate));
        if(intFrom < intThreshold)
        {
            result =  false; 
        }
        return result;
    }
}

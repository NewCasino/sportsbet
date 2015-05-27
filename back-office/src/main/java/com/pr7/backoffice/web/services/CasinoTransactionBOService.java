/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.session.BOUserSession;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.casino.mgs.MGSCasinoTokenManager;
import com.pr7.casino.mgs.model.MGSMethod;
import com.pr7.casino.mgs.model.MGSResult;
import com.pr7.casino.mgs.model.MGSWrapper;
import com.pr7.casino.mgs.service.MGSCasinoService;
import com.pr7.modelsb.constant.CasinoSiteId;
import com.pr7.modelsb.constant.CoreSiteId;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.constant.PtGameCategory;
import com.pr7.modelsb.constant.PurposeReportEnum;
import com.pr7.modelsb.dto.BaseFilter;
import com.pr7.modelsb.dto.CasinoTransactionDto;
import com.pr7.modelsb.dto.CasinoTransactionFilter;
import com.pr7.modelsb.dto.ExternalMemberMapDto;
import com.pr7.sb.service.CasinoTransactionService;
import com.pr7.sb.service.ExternalMemberMapService;
import com.pr7.sbasc.dto.WagerAscDto;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.DateUtil;
import com.pr7.util.excel.ColumnMap;
import com.pr7.util.excel.ExcelGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
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
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
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
@RequestMapping(value = "/sv/casino/")
@Authentication(type = UserType.BO)
public class CasinoTransactionBOService {

    private static final Logger logger = Logger.getLogger(CasinoTransactionBOService.class);
    @Autowired
    private MGSCasinoService mgsCasinoService;
    @Autowired
    private ExternalMemberMapService memberMapService;
    @Autowired
    CasinoTransactionService casinoTransactionService;
    @Autowired
    MGSCasinoTokenManager mGSCasinoTokenManager;

    @Autowired
    BOUserSession session;

    
    @RequestMapping("/list-txn.sv")
    @ResponseBody
    @Authorization(hasAnyPermission = {Permissions.ListCasinoTransactions})
    public Object listCasinoTransactions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rp,
            @RequestParam(defaultValue = "") String sortname,
            @RequestParam(defaultValue = "asc") String sortorder,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "") String gameType,
            @RequestParam(defaultValue = "UNKNOWN") CasinoSiteId casinoSiteId,
            @RequestParam(defaultValue = "UNKNOWN") PtGameCategory category,
            long dateFrom,
            long dateTo) {
 
        CasinoTransactionFilter filter = new CasinoTransactionFilter();
        filter.setMemberCode(memberCode);
        filter.setStatus(page);
        filter.setStatus(status);
        filter.setCasinoVendor(casinoSiteId);
        filter.setTransactionId(transactionId);
        filter.setDateFrom(new Date(dateFrom));
        filter.setDateTo(new Date(dateTo));
        filter.setOrderBy(BaseFilter.SortOrder.valueOf(sortorder.toUpperCase()));
        filter.setSortByCols(sortname);
        filter.setGameType(gameType);
        filter.setGameCategoryPT(category);

        logger.debug("Filter By: " + filter.toString());
        
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(page, rp);
        List<CasinoTransactionDto> dtos = casinoTransactionService.listCasinoTransactions(filter);
        paginateRecordJSON.setRecords(dtos.toArray());
        return paginateRecordJSON;
    }

    @Authorization(hasAnyPermission = {Permissions.ListCasinoTransactions})
    @RequestMapping("/generateCasinoReportExcel.sv")
    @ResponseBody
    public Object generateCasinoReportExcel(
             HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rp,
            @RequestParam(defaultValue = "") String sortname,
            @RequestParam(defaultValue = "asc") String sortorder,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "") String gameType,
            @RequestParam(defaultValue = "UNKNOWN") CasinoSiteId casinoSiteId,
            @RequestParam(defaultValue = "UNKNOWN") PtGameCategory category,
            long dateFrom,
            long dateTo) throws IOException, Exception {
        // ### POPULATE DATA =============================
        Date fromDate = new Date(dateFrom);
        Date toDate = new Date(dateTo);
        
        
        CasinoTransactionFilter filter = new CasinoTransactionFilter();
        filter.setMemberCode(memberCode);
        filter.setStatus(page);
        filter.setStatus(status);
        filter.setCasinoVendor(casinoSiteId);
        filter.setTransactionId(transactionId);
        filter.setDateFrom(fromDate);
        filter.setDateTo(toDate);      
        filter.setOrderBy(BaseFilter.SortOrder.valueOf(sortorder.toUpperCase()));
        filter.setSortByCols(sortname);
        filter.setGameType(gameType);
        filter.setGameCategoryPT(category);
        logger.debug("Filter By: " + filter.toString());
        List<CasinoTransactionDto> dtos = casinoTransactionService.listCasinoTransactions(filter);       
        if (dtos.size() == 0)
        {
               return "NO_RECORD"; //handled by javascript
        }
        // ### END OF POPULATE DATA=================================================
        
        // ### GENERATE EXCEL =========================================
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        String strFromDate = df.format(fromDate);
        String strToDate = df.format(toDate);
  
        ColumnMap.Validator dateTZConvert = new ColumnMap.Validator() {
            @Override
            public Object preProcessData(Object value, Object obj, ColumnMap map, int rowIndex, int colIndex) {
                if (value != null) {
                    Date date = (Date) value;
                    value = DateUtil.serverToClientDate(date, 480);
                }
                return value;
            }
        };
        String title = "Casino-Transactions" + transactionId + "_" + memberCode + "_" + strFromDate + "_" + strToDate;
        ExcelGenerator excelGenerator = new ExcelGenerator(
                title, dtos, Arrays.asList(
                new ColumnMap("txnid", "Transaction ID"),
                new ColumnMap("membercode", "Member Code", "text", ""),
                new ColumnMap("casinoVendor", "Casino Vendor", "text", ""),
                new ColumnMap("gameType", "Game Type", "text", ""),
                new ColumnMap("betamount", "Bet Amount", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("validBetAmount", "VTO", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("winloss", "Win/Loss", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("status", "Status", "#,##0", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("datecreated" ,"Transaction Date",  "dd mmm yyyy hh:mm:ss", "", dateTZConvert),
                new ColumnMap("misc", "Remarks", "text","")
                ) 
                ,ExcelGenerator.ExcelFileFormat.XLS, 
               new String [] { "betamount", "winloss","validBetAmount"});
         response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + excelGenerator.getFileName()+ excelGenerator.getFileExtension() );
            excelGenerator.write(response.getOutputStream());
            return "";
        // ### END OF GENERATE EXCEL =========================================
    }
    

    @RequestMapping("/mgs-playcheck.sv")
    @ResponseBody
    @Authorization(hasAnyPermission = {Permissions.ListCasinoTransactions})
    public Object getMGSPlaycheckURL(String memberCode) throws UnsupportedEncodingException {
        ExternalMemberMapDto exMemberMap = memberMapService.getExMemberMap(memberCode, ExtProductVendor.MGS);
        if (exMemberMap != null) {
            return mgsCasinoService.getPlayCheckUrl(memberCode, false, "en");
        }
        return "404";
    
    }

    @RequestMapping("/mgs-action.sv")
    @ResponseBody
    @Authorization(hasAnyPermission = {Permissions.ExecuteCasinoTasks})
    public Object performAction(HttpServletRequest request) throws UnsupportedEncodingException, Exception {
        MGSMethod mGSMethod = new MGSMethod();
        Map<String,String[]> params = request.getParameterMap();
        for (String key : params.keySet()) {
            mGSMethod.setParam(key, "string", request.getParameter(key));
        }
        String memberCode =  request.getParameter("MemberCode");

        if(StringUtils.isBlank(memberCode)){
            throw new Exception("Member Code can not be empty");
        }

        mGSMethod.setMethodName(request.getParameter("Method"));
        if(StringUtils.isBlank(mGSMethod.getMethodName())){
            throw new Exception("Can not determine Action.");
        }
        
        ExternalMemberMapDto exMemberMap = memberMapService.getExMemberMap(memberCode, ExtProductVendor.MGS);

        if(exMemberMap == null){
            throw new Exception("Member dose not exist or have never accessed to MGS Casino");
        }

        if("PlayCheck".equalsIgnoreCase(mGSMethod.getMethodName())){
            return mgsCasinoService.getPlayCheckUrl(memberCode, false, "en");
        }else{
            mGSMethod.setParam("Token", "string", mGSCasinoTokenManager.generateToken(memberCode));
            mGSMethod.setParam("MethodCallBy", "string", "BO_" + session.getLoginId());
            MGSResult mGSResult = mgsCasinoService.getCasinoResponse(new MGSWrapper<MGSMethod>(mGSMethod), "");
            Map returnParams =  mGSResult.getParams();
            returnParams.put("Token", null);
            return returnParams;
        }        
    }
    
    
    @RequestMapping("/searchCasinoTransaction.sv")
    @ResponseBody
    @Authorization(hasAnyPermission = {Permissions.ListBetInquiry})
    public Object searchCasinoTransaction(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue = "asc") String sord,
            @RequestParam(defaultValue = "txnid") String sidx,        
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "UNKNOWN") CasinoSiteId casinoSiteId,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "UNKNOWN") PtGameCategory category,
            @RequestParam(defaultValue = "") Long endDate) throws Exception {
        CasinoTransactionFilter filter = new CasinoTransactionFilter();
        filter.setMemberCode(memberCode);
        filter.setStatus(page);
        filter.setStatus(status);
        filter.setCasinoVendor(casinoSiteId);
        filter.setTransactionId(transactionId);
        filter.setDateFrom(new Date(startDate));
        filter.setDateTo(new Date(endDate));
        filter.setOrderBy(BaseFilter.SortOrder.valueOf(sord.toUpperCase()));
        filter.setSortByCols(sidx);
        filter.setGameCategoryPT(category);
        logger.debug("Filter By: " + filter.toString());
        
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(page, rows);
        List<CasinoTransactionDto> dtos = casinoTransactionService.listCasinoTransactions(filter);
         
        Map<Object,Object> result=new HashMap<Object, Object>();
        result.put("lastUpdateDate",casinoTransactionService.getLastUpdateDate(casinoSiteId));
        paginateRecordJSON.setUserdata(result);
        paginateRecordJSON.setRecords(dtos.toArray());
        return paginateRecordJSON;
    }
    
    @Authorization(hasAnyPermission = {Permissions.ListBetInquiry})
    @RequestMapping("/generateCasinoReportExcelI.sv")
    @ResponseBody
    public void generateCasinoReportExcelI(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue = "asc") String sord,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "UNKNOWN") CasinoSiteId casinoSiteId,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "UNKNOWN") PtGameCategory category,
            @RequestParam(defaultValue = "binary") String type) throws IOException, Exception {
        // ### POPULATE DATA =============================
        Date fromDate = new Date(startDate);
        Date toDate = new Date(endDate);
            if(!isDateRangeValid(fromDate, toDate)) //if (!isFromDateValid(start, -1))
            {
                response.getOutputStream().print("INVALID_STATE");//handled later by javascript
                return;
            }
            // inspect whether memberCode is empty or not
            if (memberCode.trim() == "") {
                response.getOutputStream().print("INVALID_FILTER");
                return;
            }
        CasinoTransactionFilter filter = new CasinoTransactionFilter();
        filter.setMemberCode(memberCode);
        filter.setStatus(page);
        filter.setStatus(status);
        filter.setCasinoVendor(casinoSiteId);
        filter.setTransactionId(transactionId);
        filter.setDateFrom(fromDate);
        filter.setDateTo(toDate);      
        filter.setOrderBy(BaseFilter.SortOrder.valueOf(sord.toUpperCase()));
        filter.setGameCategoryPT(category);
        logger.debug("Filter By: " + filter.toString());
        List<CasinoTransactionDto> dtos = casinoTransactionService.listCasinoTransactions(filter);       
//        if (dtos.size() == 0)
//        {
//               return "NO_RECORD"; //handled by javascript
//        }
        // ### END OF POPULATE DATA=================================================
        
        // ### GENERATE EXCEL =========================================
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strFromDate = df.format(fromDate);
        String strToDate = df.format(toDate);
  
        ColumnMap.Validator dateTZConvert = new ColumnMap.Validator() {
            @Override
            public Object preProcessData(Object value, Object obj, ColumnMap map, int rowIndex, int colIndex) {
                if (value != null) {
                    Date date = (Date) value;
                    value = DateUtil.serverToClientDate(date, 480);
                }
                return value;
            }
        };
        String title = "Casino-Transactions" + transactionId + "_" + memberCode + "_" + strFromDate + "_" + strToDate;
        ExcelGenerator excelGenerator = new ExcelGenerator(
                title, dtos, Arrays.asList(
                new ColumnMap("txnid", "Transaction ID"),
                new ColumnMap("membercode", "Member Code", "text", ""),
                new ColumnMap("casinoVendor", "Casino Vendor", "text", ""),
                new ColumnMap("gameType", "Game Type", "text", ""),
                new ColumnMap("betamount", "Bet Amount", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("validBetAmount", "VTO", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("winloss", "Win/Loss", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("status", "Status", "#,##0", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("datecreated" ,"Transaction Date",  "dd mmm yyyy hh:mm:ss", "", dateTZConvert),
                new ColumnMap("misc", "Remarks", "text","")
                ) 
                ,ExcelGenerator.ExcelFileFormat.XLS, 
               new String [] { "betamount", "winloss","validBetAmount"});
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
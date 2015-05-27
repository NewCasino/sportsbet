/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.newkeno.KenoWSException;
import com.pr7.newkeno.constant.NewKenoGameType;
import com.pr7.newkeno.constant.NewKenoStatus;
import com.pr7.newkeno.entity.NewKenoTransaction;
import com.pr7.newkeno.model.NewKenoFilter;
import com.pr7.newkeno.services.NewKenoServiceImpl;
import com.pr7.modelsb.constant.PurposeReportEnum;
import com.pr7.modelsb.dto.CasinoTransactionDto;
import com.pr7.newkeno.constant.NewKenoPurposeType;
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
@Controller("NewKenoServiceController")
@RequestMapping("/sv/bmssc")
@Authentication(type = UserType.BO)
public class NewKenoService {
    private static final Logger logger = LogManager.getLogger(NewKenoServiceImpl.class);
    @Autowired
    private com.pr7.newkeno.services.NewKenoService kenoService;    

    @ResponseBody
    @RequestMapping("/searchNewKenoTransaction.sv")
    @Authorization(hasAnyPermission = {Permissions.ListNewKenoTransactions})
    public Object searchNewKenoTransaction(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "-1") int gameType,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "0") int betType,
            @RequestParam(defaultValue = "-1") int status,
            @RequestParam(defaultValue = "MATCH_DATE") NewKenoPurposeType purpose) throws KenoWSException {
        NewKenoFilter filter = new NewKenoFilter();
        filter.setTransactionId(transactionId);
        filter.setMemberCode(memberCode);        
        filter.setStartDate(new Date(startDate));
        filter.setEndDate(new Date(endDate));
        filter.setKenoGameType(NewKenoGameType.parseFromValue(gameType));
        filter.setStatus(NewKenoStatus.parseFromValue(status));  
        filter.setPurposeType(purpose);
      
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(page, rows);
        List<NewKenoTransaction> dtos = kenoService.searchNewKenoTransaction(filter);        
        Map<Object,Object> result=new HashMap<Object, Object>();        
        Date lastSettled = kenoService.getLastUpdateDate(null);
        result.put("lastUpdateDate", lastSettled);
        paginateRecordJSON.setUserdata(result);
        paginateRecordJSON.setRecords(dtos.toArray());
        return paginateRecordJSON;
    }
   
    
    @Authorization(hasAnyPermission = {Permissions.ListNewKenoTransactions})
    @RequestMapping("/generateExcel.sv")
    @ResponseBody
    public void generateReportExcel(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue = "") String transactionId,
            @RequestParam(defaultValue = "") String memberCode,
            @RequestParam(defaultValue = "-1") int gameType,
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
         NewKenoFilter filter = new NewKenoFilter();
        filter.setTransactionId(transactionId);
        filter.setMemberCode(memberCode);        
        filter.setStartDate(new Date(startDate));
        filter.setEndDate(new Date(endDate));
        filter.setKenoGameType(NewKenoGameType.parseFromValue(gameType));
        filter.setStatus(NewKenoStatus.parseFromValue(status));              
        
        List<NewKenoTransaction> kenoTransactions = kenoService.searchNewKenoTransaction(filter);

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
                "BMSSC-Report_" + transactionId + "_" + memberCode + "_" + DateFormatUtils.format(start, "yyyyMMdd") + "_" + DateFormatUtils.format(end, "yyyyMMdd"), kenoTransactions, Arrays.asList(                                
                new ColumnMap("loginId", "Member Code", "text", ""),
                new ColumnMap("draw", "Draw", "text", "-"),
                new ColumnMap("wager", "Wager", "text", "-"),
                new ColumnMap("betAmount", "Bet Amount", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),                
                new ColumnMap("currency", "Currency", "text", "-"),
                new ColumnMap("results", "Results", "#,##0.00", "-"),
                new ColumnMap("netResult", "Net Result", "#,##0.00", "-"),
                new ColumnMap("vto", "VTO", "#,##0.00", "-"),
                new ColumnMap("statusStr", "Status", "text", ""),
                new ColumnMap("commission", "Commission", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("updateTime", "Update Time", "dd mmm yyyy h:mm:ss", "",dateTZConvert),
                new ColumnMap("gameStr", "Game Code", "text", ""),                                
                new ColumnMap("market", "Market", "text", "-"),
                new ColumnMap("selection", "Selection", "text", "-"),
                new ColumnMap("odds", "Odds", "text", "-"),
                new ColumnMap("betTime", "Bet Time", "dd mmm yyyy h:mm:ss", "",dateTZConvert),   
                new ColumnMap("contraParentId", "Contra Wager", "text", "-"),
                new ColumnMap("rebateTransId", "Rebate ID", "text", "-"))
                ,ExcelGenerator.ExcelFileFormat.XLS);
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

}

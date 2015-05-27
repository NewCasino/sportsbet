
package com.pr7.backoffice.web.services;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.casino.dsp.dto.EH2AgentInfo;
import com.pr7.casino.dsp.dto.EHAgentInfo;
import com.pr7.casino.dsp.service.MemberDspService;
import com.pr7.casino.entwineasia.services.EAService;
import com.pr7.casino.mgs.service.MGSCasinoService;
import com.pr7.constant.Language;
import com.pr7.keno.services.KenoService;
import com.pr7.modelsb.constant.CoreSiteId;
import com.pr7.modelsb.constant.EmptyDateMatch;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.constant.MemberStatus;
import com.pr7.modelsb.constant.ProductFilter;
import com.pr7.modelsb.constant.PurposeReportEnum;
import com.pr7.modelsb.constant.TransactionStatus;
import com.pr7.modelsb.dto.ExternalMemberMapDto;
import com.pr7.modelsb.dto.MemberDto;
import com.pr7.modelsb.dto.MemberFilter;
import com.pr7.modelsb.dto.MemberSessionLogFilter;
import com.pr7.modelsb.dto.ReferrerFilter;
import com.pr7.modelsb.dto.WagerDto;
import com.pr7.newkeno.services.NewKenoService;
import com.pr7.sb.constant.ChangePasswordResult;
import com.pr7.sb.constant.SpiErrorCode;
import com.pr7.sb.service.ExternalMemberMapService;
import com.pr7.sb.service.WagerService;
import com.pr7.sb3.SB3Exception;
import com.pr7.sb3.constant.SearchType;
import com.pr7.sb3.dto.WagerSb3Dto;
import com.pr7.sb3.model.Sb3TransferHistory;
import com.pr7.sb3.processor.api.SB3ApiProcessor;
import com.pr7.sb3.service.WagerSb3Service;
import com.pr7.sbasc.dto.WagerAscDto;
import com.pr7.sbasc.service.SBAscMemberService;
import com.pr7.sbasc.service.WagerAscService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.DateUtil;
import com.pr7.util.excel.ColumnMap;
import com.pr7.util.excel.ExcelGenerator;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/sv/member")
@Authentication(type={UserType.BO})
public class MemberService {

    private static final Logger logger = LogManager.getLogger(MemberService.class);

    @Autowired
    com.pr7.sb.service.MemberService memberService;

    @Autowired
    MemberDspService memberDspService; 
    
    @Autowired
    com.pr7.sb.service.TransactionService transactionService;

    @Autowired
    KenoService kenoService;

    @Autowired
    EAService eaCasinoService;

    @Autowired
    MGSCasinoService mgsCasinoService;
    
    @Autowired
    EHAgentInfo ehAgentInfo;
    
    @Autowired
    EH2AgentInfo eh2AgentInfo;
    
    @Autowired
    com.pr7.sb.service.MemberSessionLogService memberSessionLogService;
    
    @Autowired
    SBAscMemberService sbAscMemberService;
    
    @Autowired
    WagerAscService wagerAscService;
    
    @Autowired
    WagerService wagerService;
    
    @Autowired
    WagerSb3Service wagerSb3Service;
    
    @Autowired
    NewKenoService newKenoService;
    
    @Autowired
    SB3ApiProcessor sb3Processor;
    
    @Autowired
    ExternalMemberMapService extMemberMapService;
       
    @Authorization(hasAnyPermission={Permissions.ListMemberMaping, Permissions.EditMemberMaping})
    @RequestMapping("/searchMappingReport.sv")
    @ResponseBody
    public Object searchMappingReportHandler(
            @RequestParam(defaultValue="") String agentCode, 
            @RequestParam(defaultValue="") String memberCode, 
            @RequestParam(defaultValue="") String coreMemberCode, 
            @RequestParam(defaultValue="") String casinoCode, 
            @RequestParam(defaultValue="") String dspCode,
            @RequestParam(defaultValue="") String affCode,
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate, 
            @RequestParam(defaultValue="ACTIVE") MemberStatus status) {
        MemberFilter filter = new MemberFilter();
        filter.setAgentCode(agentCode);
        filter.setCoremembercode(coreMemberCode);
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setMemberStatus(status);
        filter.setMembercode(memberCode);
        filter.setCasinocode(casinoCode);
        filter.setDspCode(dspCode);
        filter.setAffCode(affCode);
        filter.setDspAgentCode(ehAgentInfo.getAgentCode());
        filter.setDsp2AgentCode(eh2AgentInfo.getAgentCode());
        List<MemberDto> members = memberService.getMemberListByFilter(filter);
        return members;
    }
    
    @Authorization(hasAnyPermission={Permissions.ListMemberMaping, Permissions.EditMemberMaping})
    @RequestMapping("/searchLoginReport.sv")
    @ResponseBody
    public Object searchLoginReportHandler(
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate) {
        MemberFilter filter = new MemberFilter();
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setMembercode(""); 
        return memberService.getLoginStatisticByFilter(filter);
    }
    
    @Authorization(hasAnyPermission={Permissions.ListMemberMaping, Permissions.EditMemberMaping})
    @RequestMapping("/searchLastLoginReport.sv")
    @ResponseBody
    public Object searchLastLoginReportHandler(
            @RequestParam(defaultValue="") String memberCode, 
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate) {
        MemberFilter filter = new MemberFilter();
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setMembercode(memberCode); 
        return memberService.getLoginMemberByFilter(filter);
    }
    
    @RequestMapping("/updateDateMatch.sv")
    @ResponseBody
    public Object updateDateMatch(
            @RequestParam(defaultValue = "") String wagerNo,
            @RequestParam(defaultValue = "0") long datematch
            ){
        try{
            WagerDto wagerDto = wagerService.getByWagerNo(wagerNo);
            Date date = new Date(datematch);
            wagerDto.setDateMatch(date);
            wagerService.update(wagerDto);
            return true;
        }catch (Exception e){
            return false;
        }
        
    }
    
    @Authorization(hasAnyPermission={Permissions.ListSportsbookWagers})
    @RequestMapping("/searchWagerReport.sv")
    @ResponseBody
    public Object searchWagerReportHandler(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue="") String wagerNo,
            @RequestParam(defaultValue="") String memberCode,            
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate,
            @RequestParam(defaultValue="-1") int status,
            @RequestParam(defaultValue="2") final int siteId,
            @RequestParam(defaultValue="") final String contraWager,
            @RequestParam(defaultValue = "MATCH_DATE") PurposeReportEnum purpose,
            @RequestParam(defaultValue = "off") EmptyDateMatch emptyDateMatch,
            @RequestParam(defaultValue = "HIGH_ROLLER_SB") ProductFilter product) throws Exception {
        
        return commonSearchHandler(contraWager,page, rows, wagerNo, memberCode, startDate, endDate, status, siteId, purpose, product,emptyDateMatch);
    }
    
    @Authorization(hasAnyPermission={Permissions.ListBetInquiry})
    @RequestMapping("/searchWagerReportI.sv")
    @ResponseBody
    public Object searchWagerReportHandlerI(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue="") String wagerNo,
            @RequestParam(defaultValue="") String memberCode,            
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate,
            @RequestParam(defaultValue="-1") int status,
            @RequestParam(defaultValue="2") final int siteId,
            @RequestParam(defaultValue="") final String contraWager,
            @RequestParam(defaultValue = "MATCH_DATE") PurposeReportEnum purpose,
            @RequestParam(defaultValue = "HIGH_ROLLER_SB") ProductFilter product) throws Exception {
        return commonSearchHandler(contraWager, page, rows, wagerNo, memberCode, startDate, endDate, status, 0, purpose, product,EmptyDateMatch.off);
    }
    public Object commonSearchHandler(String contraWager,int page,int rows,String wagerNo,String memberCode,Long startDate,Long endDate,int status,final int siteId,PurposeReportEnum purpose,ProductFilter product,EmptyDateMatch emptyDateMatch) throws Exception 
    {
         CoreSiteId coreSiteId = CoreSiteId.parseFromValue(siteId);

        if(coreSiteId == CoreSiteId.UNKNOWN)
        {
             // inspect whether fromDate value less than previous 1 month or not
            if(!isDateRangeValid(new Date(startDate), new Date(endDate)))//if(!isFromDateValid(new Date(startDate), -1))
            {
                return "INVALID_DATE"; //handled later by javascript
            } 
            // inspect whether memberCode is empty or not
            if(memberCode.trim() == "")
            {
                return "INVALID_FILTER";
            }
        }
        //lastSettled and lastUnsettled to be displayed in Bet Inquiry report
        Date lastSettled = null;
        Date lastUnsettled = null;
        if(coreSiteId == CoreSiteId.UNKNOWN)
        {
            switch(product)
            {
                case STANDARD_SB: 
                    lastSettled = wagerAscService.getLastUpdateSettled(wagerNo, memberCode);
                    lastUnsettled = wagerAscService.getLastUpdateUnsettled(wagerNo, memberCode);
                    break;
                case HIGH_ROLLER_SB : 
                    lastSettled = wagerService.getLastUpdateSettled(wagerNo, memberCode);
                    lastUnsettled = wagerService.getLastUpdateUnsettled(wagerNo, memberCode);
                    break;
                case SB3:
                    lastSettled = wagerSb3Service.getLastUpdateSettled(wagerNo, memberCode);
                    lastUnsettled = wagerSb3Service.getLastUpdateUnsettled(wagerNo, memberCode);
            }
        }
        
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(page, rows);
        List<WagerAscDto> dtoASC =null;
        List<WagerDto> dtoSB = null;
        List<WagerSb3Dto> dtoSB3 = null;
        if (coreSiteId == CoreSiteId.ASC || (coreSiteId == CoreSiteId.UNKNOWN && product == ProductFilter.STANDARD_SB))
        {
            dtoASC= wagerAscService.getWagerStatementForBO(contraWager, wagerNo, memberCode, new Date(startDate), new Date(endDate), TransactionStatus.parse(status), Language.ENGLISH, purpose);
            paginateRecordJSON.setRecords(dtoASC.toArray());
            paginateRecordJSON.setUserdata(getASCFooter(dtoASC,lastSettled, lastUnsettled));
        }
        else if(coreSiteId == CoreSiteId.SB3 || product == ProductFilter.SB3){
            
            dtoSB3 = wagerSb3Service.getWagerStatementForBO(contraWager, wagerNo, memberCode, new Date(startDate), new Date(endDate), TransactionStatus.parse(status), Language.ENGLISH, purpose);
            paginateRecordJSON.setRecords(dtoSB3.toArray());
            paginateRecordJSON.setUserdata(getSB3Footer(dtoSB3,lastSettled, lastUnsettled));
        }
        else //High Roller
        {
            dtoSB = transactionService.getWagerStatementForBO(contraWager,wagerNo, memberCode, new Date(startDate), new Date(endDate), TransactionStatus.parse(status), Language.ENGLISH, purpose, emptyDateMatch);
            paginateRecordJSON.setRecords(dtoSB.toArray());
             paginateRecordJSON.setUserdata(getSBFooter(dtoSB,lastSettled, lastUnsettled));
        }
         
        return paginateRecordJSON;
    }
    
    private Map<Object,Object> getASCFooter(List<WagerAscDto> l, Date lastSettled, Date lastUnsettled) //for jqGrid custom footer
    {
        Map<Object,Object> result=new HashMap<Object, Object>();
        Double varStakeF = 0.00 ;
        Double varWinLoss = 0.00 ;
        Double varVTO  = 0.00 ;
        for(WagerAscDto wd : l)
        {
            varStakeF += (wd.getStakeF()==null)?0:wd.getStakeF();
            varWinLoss  += (wd.getResultF()==null)?0:wd.getResultF();
            varVTO += (wd.getValidBetAmount()==null)?0:wd.getValidBetAmount();
        }
        result.put("betTypeEN","Grand Total : ");
        result.put("stakeF", varStakeF);
        result.put("resultF", varWinLoss);
        result.put("validBetAmount", varVTO);
        result.put("lastSettled", lastSettled);
        result.put("lastUnsettled", lastUnsettled);
        return result;
    }
    private Map<Object,Object> getSBFooter(List<WagerDto> l, Date lastSettled, Date lastUnsettled)//for jqGrid custom footer
    {
        Map<Object,Object> result= new HashMap<Object, Object>();
        Double varStakeF = 0.00 ;
        Double varWinLoss = 0.00 ;
        Double varVTO  = 0.00 ;
        for(WagerDto wd : l)
        {
            varStakeF += (wd.getStakeF()==null)?0:wd.getStakeF();
            varWinLoss  += (wd.getResultF()==null)?0:wd.getResultF();
            varVTO += (wd.getValidBetAmount()==null)?0:wd.getValidBetAmount();
        }
        result.put("betTypeEN","Grand Total : ");
        result.put("stakeF", varStakeF);
        result.put("resultF", varWinLoss);
        result.put("validBetAmount", varVTO);
        result.put("lastSettled", lastSettled);
        result.put("lastUnsettled", lastUnsettled);
        return result;
    }
    private Map<Object,Object> getSB3Footer(List<WagerSb3Dto> l, Date lastSettled, Date lastUnsettled)//for jqGrid custom footer
    {
        Map<Object,Object> result= new HashMap<Object, Object>();
        Double varStakeF = 0.00 ;
        Double varWinLoss = 0.00 ;
        Double varVTO  = 0.00 ;
        for(WagerSb3Dto wd : l)
        {
            varStakeF += (wd.getStakeF()==null)?0:wd.getStakeF();
            varWinLoss  += (wd.getResultF()==null)?0:wd.getResultF();
            varVTO += (wd.getValidBetAmount()==null)?0:wd.getValidBetAmount();
        }
        result.put("betTypeEN","Grand Total : ");
        result.put("stakeF", varStakeF);
        result.put("resultF", varWinLoss);
        result.put("validBetAmount", varVTO);
        result.put("lastSettled", lastSettled);
        result.put("lastUnsettled", lastUnsettled);
        return result;
    }
    @Authorization(hasAnyPermission={Permissions.ListBetInquiry})
    @RequestMapping("/checkDate.sv")
    @ResponseBody
    public Object CheckDate(        
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate,
            @RequestParam(defaultValue="") String memberCode
        ) throws Exception {
         // inspect whether fromDate value less than previous 1 month or not
        if(!isDateRangeValid(new Date(startDate), new Date(endDate)))//if(!isFromDateValid(new Date(startDate), -1))
        {
            return "INVALID_DATE"; //handled later by javascript
        } 
       // inspect whether memberCode is empty or not
        if(memberCode.trim() == "")
        {
            return "INVALID_FILTER";
        }
        return "";
    }
    @Authorization(hasAnyPermission={Permissions.ListSportsbookWagers})
    @RequestMapping("/generateWagerReportExcel.sv")    
    public void generateWagerReportExcel(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue="") String wagerNo,
            @RequestParam(defaultValue="") String memberCode,            
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate,
            @RequestParam(defaultValue="binary") String type,
            @RequestParam(defaultValue="480") final int tz,
            @RequestParam(defaultValue="2") final int siteId,
            @RequestParam(defaultValue="") final String contraWager,
            @RequestParam(defaultValue = "MATCH_DATE") PurposeReportEnum purpose,
            @RequestParam(defaultValue = "off") EmptyDateMatch emptyDateMatch,
            @RequestParam(defaultValue = "HIGH_ROLLER_SB") ProductFilter product
            ) throws Exception {        
         commonGenerateExcel(contraWager,request, response, wagerNo, memberCode, startDate, endDate, type, tz, siteId, purpose, product,emptyDateMatch);
    }
     @Authorization(hasAnyPermission={Permissions.ListBetInquiry})
    @RequestMapping("/generateWagerReportExcelI.sv")    
    public void generateWagerReportExcelI(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue="") String wagerNo,
            @RequestParam(defaultValue="") String memberCode,            
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate,
            @RequestParam(defaultValue="binary") String type,
            @RequestParam(defaultValue="480") final int tz,
            @RequestParam(defaultValue="2") final int siteId,
            @RequestParam(defaultValue="") final String contraWager,
            @RequestParam(defaultValue = "MATCH_DATE") PurposeReportEnum purpose,
            @RequestParam(defaultValue = "HIGH_ROLLER_SB") ProductFilter product
            ) throws Exception {        
         commonGenerateExcel(contraWager,request, response, wagerNo, memberCode, startDate, endDate, type, tz, 0, purpose, product,EmptyDateMatch.off);
    }
    
    public void commonGenerateExcel(String contraWager,HttpServletRequest request,HttpServletResponse response,String wagerNo,String memberCode,Long startDate,Long endDate,
                                    String type,final int tz,final int siteId,PurposeReportEnum purpose,ProductFilter product,EmptyDateMatch emptyDateMatch) throws Exception {
        final CoreSiteId coreSiteId = CoreSiteId.parseFromValue(siteId);
        Date start = new Date(startDate);
        Date end = new Date(endDate);
        // inspect whether fromDate value less than previous 1 month or not
        if (coreSiteId == CoreSiteId.UNKNOWN) {
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
        }

        List wagerStatement = null;
        if (coreSiteId == CoreSiteId.ASC || (coreSiteId == CoreSiteId.UNKNOWN && product == ProductFilter.STANDARD_SB)) {
            wagerStatement = wagerAscService.getWagerStatementForBO(contraWager,wagerNo, memberCode, start, end, TransactionStatus.ALL, Language.ENGLISH, purpose);
        }  else if(coreSiteId == CoreSiteId.SB3 || product == ProductFilter.SB3){
            wagerStatement = wagerSb3Service.getWagerStatementForBO(contraWager, wagerNo, memberCode, start, end, TransactionStatus.ALL, Language.ENGLISH, purpose);
        } else {
            wagerStatement = transactionService.getWagerStatementForBO(contraWager,wagerNo, memberCode, start, end, TransactionStatus.ALL, Language.ENGLISH, purpose,emptyDateMatch);
        }

        ColumnMap.Validator htmlConvertToText = new ColumnMap.Validator() {
            @Override
            public Object preProcessData(Object value, Object obj, ColumnMap map, int rowIndex, int colIndex) {
                if (value != null) {
                    String dataStr = value.toString();
                    dataStr = dataStr.replaceAll("(?i)<hr[^>]*>", "br2n");
                    value = Jsoup.parse(Jsoup.clean(dataStr, Whitelist.basic())).text();
                    value = ((String) value).replaceAll("br2n", "\n");
                }
                return value;
            }

            @Override
            public CellStyle getCellStyle(Object data, ColumnMap col, CellStyle columnCellStyle, Workbook workbook, Row row) {
                if (col.getFieldName().equals("selection") && coreSiteId == CoreSiteId.ASC) {
                    String val = ((WagerAscDto) data).getSelection();
                    int newlineCount = StringUtils.countMatches(val, "<hr") + 1;
                    row.setHeight((short) (row.getHeight() * newlineCount));
                }
                return super.getCellStyle(data, col, columnCellStyle, workbook, row);
            }
        };

        ColumnMap.Validator dateTZConvert = new ColumnMap.Validator() {
            @Override
            public Object preProcessData(Object value, Object obj, ColumnMap map, int rowIndex, int colIndex) {
                if (value != null) {
                    Date date = (Date) value;
                    value = DateUtil.serverToClientDate(date, tz);
                }
                return value;
            }
        };
        
        boolean ignoreColumn = false;
        if (coreSiteId == CoreSiteId.UNKNOWN) {
            ignoreColumn = true;
        }
        ColumnMap colTakingPos = new ColumnMap("takingPos", "Taking Position", "0%", "-", CellStyle.ALIGN_CENTER); 
        ColumnMap colTakingStake = new ColumnMap("takingStake", "Taking Stake", "#,##0.00", "-", CellStyle.ALIGN_RIGHT);
        colTakingPos.setIgnoreThisColumn(ignoreColumn);
        colTakingStake.setIgnoreThisColumn(ignoreColumn);
        
        ExcelGenerator excelGenerator = new ExcelGenerator(
                "Wagers-Report_" + wagerNo + "_" + memberCode + "_" + DateFormatUtils.format(start, "yyyyMMdd") + "_" + DateFormatUtils.format(end, "yyyyMMdd"), wagerStatement, Arrays.asList(
                new ColumnMap("id", "Id"),
                new ColumnMap("memberCode", "Member Code", "text", ""),
                new ColumnMap("coreMemberCode", "Core Member Code", "text", ""),
                new ColumnMap("wagerNo", "Wager No", "text", ""),
                new ColumnMap("dateCreated", "Created Date", "dd mmm yyyy", "", dateTZConvert),
                new ColumnMap("dateCreated", "Created Time", "hh:mm:ss", "", dateTZConvert),
                new ColumnMap("dateMatch", "Date Match", "dd mmm yyyy", "", dateTZConvert),
                new ColumnMap("dateMatch", "Time Match", "h:mm:ss", "", dateTZConvert),
                new ColumnMap("vtoDate", "Date VTO", "dd mmm yyyy", "", dateTZConvert),
                new ColumnMap("vtoDate", "Time VTO", "h:mm:ss", "", dateTZConvert),
                new ColumnMap("league", "League", htmlConvertToText),
                new ColumnMap("runningScore", "Running Score", "text", "-", CellStyle.ALIGN_CENTER),
                new ColumnMap("match", "Match", "", "-", htmlConvertToText),
                new ColumnMap("selection", "Selection", "", "-", htmlConvertToText),
                new ColumnMap("odds", "Odds", "-", CellStyle.ALIGN_CENTER),
                new ColumnMap("oddsType", "Odds Type"),
                new ColumnMap("finalScore", "Final Score", "text", "-", htmlConvertToText),
                new ColumnMap("betTypeEN", "Bet Type"),
                new ColumnMap("stakeF", "stake", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("resultF", "Result W/L", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                new ColumnMap("validBetAmount", "Valid Bet Amount", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),
                colTakingPos,
                colTakingStake,
                new ColumnMap("transactionStatus", "Status", CellStyle.ALIGN_CENTER),
                new ColumnMap("ipAddress", "Ip Address", "text", "-", CellStyle.ALIGN_CENTER),
                new ColumnMap("parentId", "Contra Wager", "text", "-", CellStyle.ALIGN_CENTER),
                new ColumnMap("rebateTransId","Rebate Trans ID", "text", "-", CellStyle.ALIGN_CENTER)), ExcelGenerator.ExcelFileFormat.XLS,
                new String[]{"stakeF", "resultF", "validBetAmount"});
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
    

    @Authorization(hasAnyPermission={Permissions.ResetMemberPwd})
    @RequestMapping("/resetMemberPwd.sv")
    @ResponseBody
    public Object resetMemberPwd(
            @RequestParam(value = "membercode") String memberCode,            
            @RequestParam(value = "newpwd") String newPwd,
            @RequestParam(value = "confirmpwd") String confirmPwd) {
        
        if (newPwd == null || !newPwd.equals(confirmPwd)) {
            return ChangePasswordResult.CONFIRM_PASSWORD_MISSMATCH;
        }
        
        return memberService.resetMemberPwd(memberCode, newPwd, true);
    }
    
    @Authorization(hasAnyPermission={Permissions.EditMemberMaping})
    @RequestMapping("/updateStatus.sv")
    @ResponseBody
    public Object updateStatus(MemberStatus status, long memberId){
        return memberService.updateMemberStatus(status, memberId);
    }


    @Authorization(hasAnyPermission={Permissions.EditMemberMaping})
    @RequestMapping("/updateSBStatus.sv")
    @ResponseBody
    public Object updateSBStatus(MemberStatus status, long memberId){
        return memberService.updateMemberSBStatus(status, memberId);
    }

    @Authorization(hasAnyPermission={Permissions.EditMemberMaping})
    @RequestMapping("/updateCasinoStatus.sv")
    @ResponseBody
    public Object updateCasinoStatus(MemberStatus status, long memberId){
        return memberService.updateMemberCasinoStatus(status, memberId);
    }

    @Authorization(hasAnyPermission={Permissions.EditMemberMaping})
    @RequestMapping("/updateBankingStatus.sv")
    @ResponseBody
    public Object updateBankingStatus(MemberStatus status, long memberId){
        return memberService.updateMemberBankingStatus(status, memberId);
    }

    @Authorization(hasAnyPermission={Permissions.EditMemberMaping})
    @RequestMapping("/updateAffiliateCode.sv")
    @ResponseBody
    public Object updateAffiliateCode(String affiliateCode, long memberId){
        return memberService.updateAffiliateCode(affiliateCode, memberId);
    }
    
    @Authorization(hasAnyPermission={Permissions.EditDeltaBalance})
    @RequestMapping("/updateDeltaBalance.sv")
    @ResponseBody
    public Object updateDeltaBalance(String memberCode, BigDecimal deltaBalance){
        return memberService.updateDeltaBalance(memberCode, deltaBalance);
    }
    
    @Authorization(hasAnyPermission={Permissions.ListSessionLog})
    @RequestMapping("/searchSessionLog.sv")
    @ResponseBody
    public Object searchSessionLogHandler(
            @RequestParam(defaultValue="") String memberCode, 
            @RequestParam(defaultValue="") String ipAddress, 
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate, 
            @RequestParam(defaultValue="") String type) {
        MemberSessionLogFilter filter = new MemberSessionLogFilter();
        filter.setMembercode(memberCode);
        filter.setIpAddress(ipAddress);
        filter.setCreatedFrom(new Date(startDate));
        filter.setCreatedTo(new Date(endDate));
        filter.setType(type);
        return memberSessionLogService.getMemberSessionLogListByFilter(filter);
    } 

    @Authorization(hasAnyPermission={Permissions.ListMemberMaping, Permissions.EditMemberMaping})
    @RequestMapping("/walletBalance.sv")
    @ResponseBody
    public Object getWalletBalance(
            @RequestParam(defaultValue="") String memberCode,
            @RequestParam(defaultValue="sportsbook") String walletCode) throws Exception {
        ExtProductVendor vendor = ExtProductVendor.parseFromCode(walletCode);
        BigDecimal balance = null;
        switch (vendor){
            case MGS:
                try {
                    balance = mgsCasinoService.GetMemberBalance(memberCode);
                } catch(Exception e) {
                    logger.error("ERROR when get MGS balance:: memberCode = " + memberCode, e);
                }
                break;
            case EHCASINO:
                balance = memberDspService.getMemberBalance(memberCode, ehAgentInfo);
                break;
            case EH2CASINO:
                balance = memberDspService.getMemberBalance(memberCode, eh2AgentInfo);
                break;
            case EACASINO:
                balance = eaCasinoService.getBalance(memberCode);
                break;
            case KENO:
                balance = kenoService.getBalance(memberCode);
                break;
            case SBASC:
                balance = sbAscMemberService.getBalance(memberCode);
                break;
            case BMSSC:
                try {
                    balance = newKenoService.getBalance(memberCode);
                } catch (com.pr7.newkeno.KenoWSException kwx) {
                    logger.error("ERROR when get NewKeno balance:: membercode = " + memberCode + " Error :" + (kwx.getErrorCode().toString() + " - " + kwx.getMessage()).replace("\n", " "), kwx);
                } catch (Exception e) {
                    logger.error("ERROR when get NewKeno balance:: memberCode = " + memberCode, e);
                }
                break;
            case SB3:
                try {
                	ExternalMemberMapDto externalMemberMapDto = extMemberMapService.getExMemberMap(memberCode, ExtProductVendor.SB3);
                    balance = sb3Processor.getMemberCredit(externalMemberMapDto.getMemberCode(), externalMemberMapDto.getExtMemberCode());
                } catch (SB3Exception kwx) {
                    logger.error("ERROR when get SB3 balance:: membercode = " + memberCode + " Error :" + (kwx.getSb3Exception().toString() + " - " + kwx.getMessage()).replace("\n", " "), kwx);
                } catch (Exception e) {
                    logger.error("ERROR when get SB3 balance:: memberCode = " + memberCode, e);
                }
                break;
            default:
                try{
                    MemberDto memberBalance = memberService.getMemberBalance(memberCode, walletCode);
                    if(SpiErrorCode.GENERAL_SUCCESS.value().equalsIgnoreCase(memberBalance.getErrorCode())
                            && (StringUtils.isBlank(memberBalance.getAccountactivated()) || "1".equals(memberBalance.getAccountactivated())))
                    {
                        balance = new BigDecimal(memberBalance.getBalance().doubleValue()).setScale(2, RoundingMode.HALF_UP);
                    }
                }catch(Exception ex){
                    logger.error("getWalletBalance got Error: MemberCode:" + memberCode +",ProductCode: " + walletCode + ". Error Message:" + ex.getMessage(), ex);
                }
                break;
        }
        logger.debug("Current " + walletCode + " balance of " + memberCode + " is " + balance);
        return balance == null ? "-" : balance.toPlainString();
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
    private boolean isFromDateValid(Date fromDate,  int differentMonth )
    {
        boolean result = true;
        // inspect whether fromDate value less than previous 1 month or not
        SimpleDateFormat fmtInt = new SimpleDateFormat("yyyyMMdd");
        Calendar calNow = Calendar.getInstance();
        calNow.add(Calendar.MONTH, differentMonth);
        calNow.add(Calendar.DATE,1);

        int intThreshold = Integer.parseInt( fmtInt.format(calNow.getTime()));
        int intFrom =Integer.parseInt( fmtInt.format(fromDate));
       logger.debug("Value of Threshold : " + intThreshold +", value of fromDate : "+ intFrom);
        if(intFrom < intThreshold)
        {
            result =  false; 
        }
        return result;
    }
    
     @Authorization(hasAnyPermission={Permissions.ListMemberMaping, Permissions.EditMemberMaping})
    @RequestMapping("/getTransferInfo.sv")
    @ResponseBody
    public Object getAscTransferHistory(
            @RequestParam(defaultValue="") String memberCode) throws Exception {
         if(StringUtils.isBlank(memberCode))
         {
             throw new Exception("\"Please fill 'Member Code' filter...\"");
         }
         String result = sbAscMemberService.getAscTransferHistory(memberCode);
         logger.debug("Result of ASC Transfer History : " + result);
        String resembleJSON = "{\"page\":1,\"total\":1000,\"recordsPerPage\":1000,\"lastPage\":1.0,\"records\":";
        if(StringUtils.isBlank(result))
        {
            resembleJSON = resembleJSON + "[]}";
        }
        else if (result.contains("["))
        {
            resembleJSON = resembleJSON + result + "}";
        }
        else
        {
             resembleJSON = resembleJSON +"[" +result + "]}";
        }
        
        return resembleJSON;
    }
    
    @Authorization(hasAnyPermission = {Permissions.ListAffiliate, Permissions.ListMemberMaping})
    @RequestMapping("/searchReferrer.sv")
    @ResponseBody
    public Object searchReferrerReport(
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate) {
        ReferrerFilter filter = new ReferrerFilter();
        filter.setMonthFrom(startDate);
        filter.setMonthTo(endDate);
        return memberService.getReferrerReportByFilter(filter);
    }
    
    @Authorization(hasAnyPermission = {Permissions.ListAffiliate, Permissions.ListMemberMaping})
    @RequestMapping("/searchReferrerMember.sv")
    @ResponseBody
    public Object searchReferrerMemberReport(
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String url) {
        ReferrerFilter filter = new ReferrerFilter();
        filter.setMonthFrom(startDate);
        filter.setUrl(url);
        return memberService.getReferrerMemberByFilter(filter);
    }
    
    @Authorization(hasAnyPermission={Permissions.ListSportsbookWagers})
    @RequestMapping("/getTransferInfoSB3.sv")
    @ResponseBody
    public Object getSB3TransferHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue="") String memberCode,
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate) throws Exception {
        if (StringUtils.isBlank(memberCode)) {
            throw new Exception("\"Please fill 'Member Code' filter...\"");
        }
        Date start = new Date(startDate);
        Date end = new Date(endDate);
        String extMemberCode = null;
        try {
            extMemberCode = extMemberMapService.getExMemberMap(memberCode, ExtProductVendor.SB3).getExtMemberCode();
        } catch (Exception e){
            throw new Exception("\"Member Code not found\"");
        }
        
        List<Sb3TransferHistory> list = sb3Processor.getTransferHistory(memberCode, extMemberCode, SearchType.DATE_RANGE, start, end);
        logger.debug("Result of SB3 Transfer History : " + list.size());

        PaginateRecordJSON paginateJSON = new PaginateRecordJSON(page, rows);
        paginateJSON.setRecords(list.toArray());
        return paginateJSON;
    }
}

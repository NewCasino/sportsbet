/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.constant.RebateStatus;
import com.pr7.modelsb.dto.RebateTransDetailDto;
import com.pr7.modelsb.dto.RebateTransDto;
import com.pr7.modelsb.dto.RebateTransFilter;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.server.session.UserSession;
import com.pr7.util.DateUtil;
import com.pr7.util.excel.ColumnMap;
import com.pr7.util.excel.ExcelGenerator;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/sv/rebate")
@Authentication(type = UserType.BO)
public class RebateService {    
    static final Logger _logger = Logger.getLogger(RebateService.class);
    
    @Autowired
    private com.pr7.sb.dao.RebateTransDAO rebateDAO;  
    
    @Autowired
    private com.pr7.sb.scraper.service.RebateService rebateService;  
    
    @Autowired
    UserSession userSession;
    
    @ResponseBody
    @RequestMapping("/searchRebateTransactions.sv")
    @Authorization(hasAnyPermission = {Permissions.ListRebateTransactions})
    public Object searchRebateTransaction(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue="") String wagerNo,
            @RequestParam(defaultValue="") String memberCode,            
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate,
            @RequestParam(defaultValue="UNKNOWN") RebateStatus status,    
            @RequestParam(defaultValue="")String rebateTransID,
            @RequestParam(defaultValue = "ExtProductVendor.invalid") ExtProductVendor product) throws Exception {
        
        RebateTransFilter filter = new RebateTransFilter();
        filter.setFromDate(new Date(startDate));
        filter.setToDate(new Date(endDate));
        filter.setRebateStatus(status);
        filter.setProduct(product);         
        filter.setWagerNo(wagerNo);
        filter.setMemberCode(memberCode);   
        filter.setRebateTransId(rebateTransID);
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(page, rows);        
        List<RebateTransDto> dtos = rebateDAO.searchRebateTrans(filter);             
        Map<Object,Object> result=new HashMap<Object, Object>();               
        paginateRecordJSON.setUserdata(result);
        paginateRecordJSON.setRecords(dtos.toArray());
        return paginateRecordJSON;
    }
   
    
    @Authorization(hasAnyPermission = {Permissions.ListRebateTransactions})
    @RequestMapping("/generateExcel.sv")
    @ResponseBody
    public void generateReportExcel(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue="") String wagerNo,
            @RequestParam(defaultValue="") String memberCode,            
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate,
            @RequestParam(defaultValue="UNKNOWN") RebateStatus status,  
            @RequestParam(defaultValue = "binary") String type,
            @RequestParam(defaultValue="480") final int tz,
            @RequestParam(defaultValue="")String rebateTransID,
            @RequestParam(defaultValue = "ExtProductVendor.invalid") ExtProductVendor product) throws Exception {
        RebateTransFilter filter = new RebateTransFilter();
        filter.setFromDate(new Date(startDate));
        filter.setToDate(new Date(endDate));
        filter.setRebateStatus(status);
        filter.setProduct(product);         
        filter.setWagerNo(wagerNo);
        filter.setMemberCode(memberCode);   
        filter.setRebateTransId(rebateTransID);
        
        Resource resource = new ClassPathResource("text.properties");
        final Properties props = PropertiesLoaderUtils.loadProperties(resource);
        List<RebateTransDto> dtos =rebateDAO.searchRebateTrans(filter);        

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
        ColumnMap.Validator productConvert = new ColumnMap.Validator() {            
            @Override
            public Object preProcessData(Object value, Object obj, ColumnMap map, int rowIndex, int colIndex) {
                if(value != null){
                    value = props.getProperty(value.toString());                    
                }
                return value;
            }
        };        
        ExcelGenerator excelGenerator = new ExcelGenerator(
                "Rebate-Transaction-Report_" + wagerNo + "_" + memberCode + "_" + DateFormatUtils.format(startDate, "yyyyMMdd") + "_" + DateFormatUtils.format(endDate, "yyyyMMdd"), dtos, Arrays.asList(                                
                new ColumnMap("rebateTransId", "Rebate Trans Id", "text", ""),
                new ColumnMap("memberCode", "Member Code", "text", ""),
                new ColumnMap("orgAmt", "Org Amount", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),                
                new ColumnMap("rebateAmt", "Rebate Amount", "#,##0.00", "-", CellStyle.ALIGN_RIGHT),                
                new ColumnMap("productStr", "Product", "text", "",productConvert),
                new ColumnMap("formattedRebateSettingValue", "Rebate Setting", "text", "-"),
                new ColumnMap("statusStr", "Status", "text", ""),
                new ColumnMap("lastUpdateDate", "Last Update Date", "dd mmm yyyy h:mm:ss", "",dateTZConvert),
                new ColumnMap("dateCreated", "Date Created", "dd mmm yyyy h:mm:ss", "",dateTZConvert))               
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
    
    @ResponseBody
    @RequestMapping("/getDetailsRebateTransactions.sv")
    @Authorization(hasAnyPermission = {Permissions.ListRebateTransactions})
    public Object getDetailsRebateTransactions(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue="0") String rebateTransId,
            @RequestParam(defaultValue="INVALID") ExtProductVendor productId){
        
 
        List<RebateTransDetailDto> rt = rebateDAO.getDetailsRebateTrans(rebateTransId,productId);
        return rt;
    }    
     
    @ResponseBody
    @RequestMapping("/rebatePerTrans.sv")
    @Authorization(hasAnyPermission = {Permissions.ListRebateTransactions})
    public Object rebatePerTrans(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue="") String rebateTransId){
        try {
            RebateTransDto rebateTransDto = rebateDAO.getByRebateTransId(rebateTransId);
            rebateTransDto.setLastUpdateBy(userSession.getLoginId());
            if (rebateTransDto.getRebateStatus() == RebateStatus.REBATED) {
                return rebateTransId + " already rebated";
            }
            if (rebateService.rebatePerTrans(rebateTransDto)) {
                return "success";
            }
            return "Unknown error";
        } catch(Exception e) {
            _logger.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}


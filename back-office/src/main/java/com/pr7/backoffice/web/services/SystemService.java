/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.dto.MemberDto;
import com.pr7.modelsb.dto.NewCoreAgentServicePayLoadDto;
import com.pr7.sb.constant.PgaCommonStatus;
import com.pr7.sb.constant.SpiErrorCode;
import com.pr7.sb.constant.SystemParamsName;
import com.pr7.sb.pga.PgaService;
import com.pr7.sb.service.NewCoreAgentServicePayLoadService;
import com.pr7.sb.service.NewCoreSignatureService;
import com.pr7.sb.service.SystemParamsService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.ConnectionHelper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping(value = "/sv/system")
@Authentication(type = {UserType.BO})
public class SystemService {
    private static final Logger _logger = LogManager.getLogger(SystemService.class);    

    @Autowired
    private NewCoreSignatureService newCoreSignatureService;
    
    @Autowired
    private SystemParamsService systemParamsService;
    
    @Autowired
    private PgaService pgaService;
    
    @Autowired
    private NewCoreAgentServicePayLoadService newCoreAgentServicePayLoadService;
    
    @Autowired
    private com.pr7.sb.service.MemberService memberService;
    
    @Value("${host.sbspi}")
    private String sbSpiHost;
    
    @Value("${host.scraper}")
    private String scraperHost;
    
    @Value("${host.ms}")
    private String msHost;

    @Authorization(hasAnyPermission= {Permissions.ListSystemParams, Permissions.EditSystemParams})
    @ResponseBody
    @RequestMapping(value = "/isSBMaintaining.sv", method = RequestMethod.GET)
    public String isSBMaintaining() {
        return systemParamsService.checkMaintaining(SystemParamsName.SB_MAINTAINING)? "1" : "0";
    }

    @Authorization(hasAnyPermission = {Permissions.ListSystemParams, Permissions.EditSystemParams})
    @ResponseBody
    @RequestMapping(value = "/isMaintaining.sv", method = RequestMethod.GET)
    public String isMaintaining(ExtProductVendor productVendor) {
        return systemParamsService.checkMaintaining(productVendor) ? "1" : "0";
    }

    @Authorization(hasAllPermission = {Permissions.EditSystemParams})
    @ResponseBody
    @RequestMapping(value = "/changeMaintainingStatus.sv", method = RequestMethod.GET)
    public boolean changeMaintainingStatus(ExtProductVendor productVendor, String value) throws Exception {
        boolean result = false;
        PgaCommonStatus status = PgaCommonStatus.SUSPENDED;
        if (!"1".equals(value)) {
            value = "0";
            status = PgaCommonStatus.ACTIVE;
        }
        SystemParamsName param = SystemParamsName.INVALID;
        switch (productVendor) {
            case SPORTBOOK_SB8:
                param = SystemParamsName.SB_MAINTAINING;
                break;
            case SBASC:
                param = SystemParamsName.SB2_MAINTAINING;
                break;
            case PTECH:
                param = SystemParamsName.PT_MAINTAINING;
                break;
            case EHCASINO:
                param = SystemParamsName.EH_MAINTAINING;
                break;
            case EH2CASINO:
                param = SystemParamsName.EH2_MAINTAINING;
                break;
            case EACASINO:
                param = SystemParamsName.EA_MAINTAINING;
                break;
            case KENO:
                param = SystemParamsName.KN_MAINTAINING;
                break;
            case MGS:
                param = SystemParamsName.MG_MAINTAINING;
                break;
            case BMSSC:
                param = SystemParamsName.BMSSC_MAINTAINING;
                break;
            case SB3:
                param = SystemParamsName.SB3_MAINTAINING;
                break;
        }

        String errorCode = pgaService.updateWalletStatus(productVendor.getCode(), status);
        if (StringUtils.isBlank(errorCode) || SpiErrorCode.parse(errorCode) != SpiErrorCode.GENERAL_SUCCESS) {
            throw new Exception("Update Wallet Status to " + status.toString() + " failed.");
        }else{
            systemParamsService.saveParam(param.getName(), value);
            result = true;
        }
        
        return result;
    }

    @Authorization(hasAllPermission = {Permissions.EditSystemParams})
    @ResponseBody
    @RequestMapping(value = "/SBMaintaining.sv", method = RequestMethod.GET)
    public boolean changeSBMaintainingStatus(String value) throws Exception {       
        return changeMaintainingStatus(ExtProductVendor.SPORTBOOK_SB8, value);
    }    
    
    @Authorization(hasAllPermission = {Permissions.EditCoreSystemSignature})
    @ResponseBody
    @RequestMapping(value = "/checkSignature.sv")
    public String checkSignature(
        @RequestParam(value = "action", defaultValue = "checkSignature") String action,
        @RequestParam(value="hashService", defaultValue="") String hashServiceName,
        @RequestParam(value="hashValue", defaultValue="") String hashValue,
        @RequestParam(value = "memberCode", defaultValue = "") String memberCode) throws Exception {
        
        _logger.debug("hashService = " + hashServiceName);
        _logger.debug("hashValue = " + hashValue);
        
        if ("checkSignature".equalsIgnoreCase(action)) {
            String[] hashServiceNames = hashServiceName.split(",");
            String[] hashValues = hashValue.split(",");

            if (hashServiceNames.length != hashValues.length) {
                return "EX|Signature changed, hashServices & hashValues have different length -> " + hashServiceNames.length + " vs " + hashValues.length;
            }
            
            Map<String, String> dataDict = new HashMap<String, String>();
            for(int i=0; i < hashServiceNames.length; i++) {
                if (hashServiceNames[i].contains("_")) {
                    hashServiceNames[i] = StringUtils.substringBefore(hashServiceNames[i], "_");
                }
                dataDict.put(hashServiceNames[i], hashValues[i]);
            }

            Map<String, String> dataFromCache = newCoreSignatureService.getDataMapFromCache();
            _logger.debug("checkSignature:: dataDict.size() = " + dataDict.size() + ", dataFromCache.size() = " + dataFromCache.size());
//            if (dataDict.size() != dataFromCache.size())
//                return "EX|dataDict.size() != dataFromCache.size(), need to reload again";
            
            for(Entry<String, String> entry: dataDict.entrySet()) {
                if (!dataFromCache.containsKey(entry.getKey())) {
                    return "EX|Signature changed, key '" + entry.getKey() + "' cannot be found from cache.";
                }
                String oldVal = dataFromCache.get(entry.getKey());
                if (!entry.getValue().equals(oldVal)) {
                    return "EX|Signature changed, key '" + entry.getKey() + "', old value: " + oldVal + ", new value: " + entry.getValue();
                }
            }
            
            return "OK|Signature no change";
        } else if ("testGetBalance".equalsIgnoreCase(action)) {
            MemberDto memberDto = null;
            String errormsg = "";
            try {
                memberDto = memberService.getMemberBalance(memberCode, ExtProductVendor.SPORTBOOK_SB8.getCode());
            } catch(Exception e) {
                errormsg = e.getMessage();
                _logger.debug(e.getMessage(), e);
            }
            
            return (memberDto == null || memberDto.getBalance() == null ? "EX|Cannot get balance, " + errormsg : "OK|" + memberDto.getBalance());
        } else if ("reloadSignature".equalsIgnoreCase(action)) {
            String[] hashServiceNames = hashServiceName.split(",");
            String[] hashValues = hashValue.split(",");

            if (hashServiceNames.length != hashValues.length) {
                return "EX|hashServices & hashValues have different length -> " + hashServiceNames.length + " vs " + hashValues.length;
            }

            Map<String, String> dataDict = new HashMap<String, String>();
            for(int i=0; i < hashServiceNames.length; i++) {
                if (hashServiceNames[i].contains("_")) {
                    hashServiceNames[i] = StringUtils.substringBefore(hashServiceNames[i], "_");
                }
                dataDict.put(hashServiceNames[i], hashValues[i]);
            }
            Entry<Integer, Integer> count = newCoreSignatureService.updateCacheAndDb(dataDict);
            _logger.debug("Done backoffice. Start to trigger sb-spi");
            boolean triggerSbSpi = false;
            try {
                Map<String, Object> response = ConnectionHelper.connectUrl(sbSpiHost + "/updateHashCache", "GET", "triggerFrom=backoffice", Collections.EMPTY_MAP);
                if (!"OK".equals(response.get("body"))) {
                    _logger.error("Trigger SbSpi failed, response = " + ConnectionHelper.printHeader(response));
                } else {
                    triggerSbSpi = true;
                }
            } catch(Exception e) {
                _logger.error("callSbSpi ERROR", e);
            }
            
            _logger.debug("Done sb-spi " + triggerSbSpi + ". Start to trigger scraper");
            boolean triggerScraper = false;
            try {
                Map<String, Object> response = ConnectionHelper.connectUrl(scraperHost + "/sv/updateHashCache", "GET", "triggerFrom=backoffice", Collections.EMPTY_MAP);
                if (!"OK".equals(response.get("body"))) {
                    _logger.error("Trigger Scraper failed, response = " + ConnectionHelper.printHeader(response));
                } else {
                    triggerScraper = true;
                }
            } catch(Exception e) {
                _logger.error("CallScraper ERROR", e);
            }
            
            _logger.debug("Done scraper. " + triggerSbSpi + ". Start to trigger ms");
            boolean triggerMs = false;
            try {
                Map<String, Object> response = ConnectionHelper.connectUrl(msHost + "/updateHashCache", "GET", "triggerFrom=backoffice", Collections.EMPTY_MAP);
                if (!"OK".equals(response.get("body"))) {
                    _logger.error("Trigger MS failed, response = " + ConnectionHelper.printHeader(response));
                } else {
                    triggerMs = true;
                }
            } catch(Exception e) {
                _logger.error("triggerMs ERROR", e);
            }
            _logger.debug("Done triggerMs.");
            
            return "OK|Backoffice success, created " + count.getKey() + ", updated " + count.getValue() 
                    + ". Trigger sb-spi " + (triggerSbSpi ? "success" : "failed")
                    + ". Trigger scraper " + (triggerScraper ? "success" : "failed")
                    + ". Trigger ms " + (triggerMs ? "success" : "failed");
        }
        
        return "OK|OK";
    }
    
    @Authorization(hasAllPermission = {Permissions.EditCoreSystemSignature})
    @ResponseBody
    @RequestMapping(value = "/com.leo.agency.managersite.ManagerSite.nocache.js")
    public String nocacheJs(HttpServletRequest request, HttpServletResponse response) throws Exception {
        _logger.debug("nocacheJs:: request.getRequestURI() = " + request.getRequestURI() + ", request.getServletPath() = " + request.getServletPath());            	
    	return newCoreSignatureService.processNoCache(StringUtils.substringAfterLast(request.getRequestURI(), "/"));
    }
    
    @Authorization(hasAllPermission = {Permissions.EditCoreSystemSignature})
    @RequestMapping(value = "*.cache.html")
    @ResponseBody
    public String cacheHtml(HttpServletRequest request, HttpServletResponse response) throws Exception {
        _logger.debug("cacheHtml:: request.getRequestURI() = " + request.getRequestURI() + ", request.getServletPath() = " + request.getServletPath());
        return newCoreSignatureService.processCacheHtml(StringUtils.substringAfterLast(request.getRequestURI(), "/"));
    }
    
    @Authorization(hasAllPermission = {Permissions.ListServicePayload})
    @RequestMapping(value = "/getAllPayload.sv")
    @ResponseBody
    public Object getAllPayload(String code) throws Exception {
        _logger.debug("getAllPayload:: code = " + code);
        return newCoreAgentServicePayLoadService.findListByCode(code);
    }
    
    @Authorization(hasAllPermission = {Permissions.ListServicePayload})
    @RequestMapping(value = "/updatePayload.sv")
    @ResponseBody
    public Object updatePayload(long id, String payload, String code) throws Exception {
        _logger.debug("getAllPayload:: id = " + id + ", payload = " + payload + ", code = " + code);
        
        NewCoreAgentServicePayLoadDto dto = newCoreAgentServicePayLoadService.findById(id);
        
        if (dto == null)
            return false;
        
        if (!StringUtils.isEmpty(payload))
            dto.setPayloadTemplate(payload);
        
        if (!StringUtils.isEmpty(code))
            dto.setCode(code);
        dto.setVersion(dto.getVersion() + 1);
        newCoreAgentServicePayLoadService.update(dto);
        
        return true;
    }
}

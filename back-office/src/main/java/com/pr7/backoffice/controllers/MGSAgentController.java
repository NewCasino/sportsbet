/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.controllers;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.utils.ResponseUtils;
import com.pr7.casino.mgs.processor.MGSAgentProcessor;
import com.pr7.modelsb.dto.AgentCasinoDto;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.ConnectionHelper;
import com.pr7.util.ConnectionResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping(value = { "/VanguardAdmin", "/VANGUARDADMIN" })
@Authentication(type= UserType.BO)
@Authorization(hasAnyPermission={Permissions.LaunchAgentAdminPortal})
public class MGSAgentController {
    private static final Logger _logger = LogManager.getLogger(MGSAgentController.class);
    private String MGS_BO_HOST;
    
    @Autowired
    private MGSAgentProcessor mgsAgentProcessor;

    @PostConstruct
    public void init() {
        MGS_BO_HOST = mgsAgentProcessor.getMGSBoHostWithProtocol();
    }
    
    @RequestMapping(value = "/searchMGSAdmin.sv")
    @ResponseBody
    public Object searchMGSAdmin(
            HttpServletRequest request){        
        AgentCasinoDto dto = new AgentCasinoDto();
        dto.setLoginId(mgsAgentProcessor.getBOUser());
        
        List<AgentCasinoDto> list = new ArrayList<AgentCasinoDto>();
        list.add(dto);
        
        return list;
    }
    
    @RequestMapping(value = "/login.sv")
    @ResponseBody
    public Object mgslogin(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {        
        Map<String, Cookie> cookies = ResponseUtils.getCookiesFromString(mgsAgentProcessor.getAgentCookies(), false);
        _logger.debug("getContextPath() = " + request.getContextPath());
        for (Map.Entry<String, Cookie> entry : cookies.entrySet()) {            
            entry.getValue().setPath(request.getContextPath());
        }
        
        for (Cookie c : cookies.values()) {
            response.addCookie(c);
        }
        
        return "." + StringUtils.substringAfter(mgsAgentProcessor.getMainUrl(), MGS_BO_HOST);
    }
    
    @RequestMapping(value = "/**")
    public void forwardRequest(
            HttpServletRequest request,
            HttpServletResponse response,            
            @CookieValue(value = ".ASPXAUTH", defaultValue = "") String cookieAuth,
            @CookieValue(value = "ASP.NET_SessionId", defaultValue = "") String cookieSessionId) throws IOException {
        
        //String baseUrl = request.getRequestURL().toString().replace(request.getPathInfo(),"") + "/agent/manager";
        String cookies = ".ASPXAUTH=" + cookieAuth + "; ASP.NET_SessionId=" + cookieSessionId;
        
        _logger.debug("forwardRequest:: cookies = " + cookies);
        
        if(request.getRequestURI().matches(".*\\.(jpg|JPG|gif|GIF|png|css|ico|cache\\.html)$") && StringUtils.isNotBlank(request.getHeader("If-None-Match"))){
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
                
        String url = MGS_BO_HOST + "/VANGUARDADMIN/" + StringUtils.substringAfter(request.getRequestURI(), "/VANGUARDADMIN/");
        //Map<String, String> forwardHeaders = getForwardHeaders(request);
        Map<String, String> forwardHeaders = new HashMap<String, String>();
        forwardHeaders.put("Cookie", cookies);        
        
        String requestPayload = IOUtils.toString(request.getInputStream(), StringUtils.defaultIfBlank(request.getCharacterEncoding(), "UTF-8"));
        
        ConnectionResponse connectionResponse = ConnectionHelper.connectToUrl(url, request.getMethod() , requestPayload , forwardHeaders);        
        
        for (Map.Entry<String, String> entry : connectionResponse.getHeaders().entrySet()) {
            if(!entry.getKey().equalsIgnoreCase("set-cookie") && !entry.getKey().equalsIgnoreCase("content-encoding")){
                response.setHeader(entry.getKey(), entry.getValue());
            }
        }
        
//        for (String cookie : connectionResponse.getCookies()) {
//            response.addHeader("Set-Cookie", ConnectionHelper.extractCookieValues(cookie.replace("JSESSIONID", "CSSID")) + "Path=/");
//        }
        
        response.setHeader("Content-Length", connectionResponse.getData().length + "");
        if(connectionResponse.getStatus() != 304){
            String body = connectionResponse.getBodyAsString();
            if (body != null && body.contains("/VANGUARDADMIN/")) {
                body = body.replace("/VANGUARDADMIN/", "../../VANGUARDADMIN/");
                response.setHeader("Content-Length", body.getBytes("UTF-8").length + "");
                IOUtils.write(body, response.getOutputStream());
            } else {
                IOUtils.write(connectionResponse.getData(), response.getOutputStream());
            }            
        }
        
        if(connectionResponse.getStatus() != 200){
            response.setStatus(connectionResponse.getStatus() );
        }
    }
    
    
    public Map<String,String> getForwardHeaders(HttpServletRequest request) {
        _logger.debug("getForwardHeaders:: request.getRequestURL().toString() = " + request.getRequestURL().toString() + ", request.getPathInfo() = " + request.getPathInfo());
        
        Map<String, String> headers = new HashMap<String, String>();
        
//        String baseUrl = request.getRequestURL().toString().replace(request.getPathInfo(),"") + "/VanguardAdmin";
//        List<String> headerNames = Collections.list(request.getHeaderNames());
//        
//        for (String name : headerNames) {
//            headers.put(name, request.getHeader(name));
//        }
//        
//        headers.put("host", mgsAgentProcessor.getMGSBoHostWithoutProtocol());
//        if(StringUtils.isNotBlank(headers.get("referer"))){
//            headers.put("referer", agentManagerURL);
//        }

        return headers;
    }
}

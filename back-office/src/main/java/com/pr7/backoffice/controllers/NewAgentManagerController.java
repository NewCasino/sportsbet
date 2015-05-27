/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.controllers;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.modelsb.entity.Agent;
import com.pr7.sb.service.AgentService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.ConnectionHelper;
import com.pr7.util.ConnectionResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/agent")
@Authentication(type= UserType.BO)
@Authorization(hasAnyPermission={Permissions.LaunchAgentAdminPortal})
public class NewAgentManagerController {
    
    @Value("${sb.agent.base.url}")
    private String agentManagerURL;
    
    @Value("${host.smartb.mgr}")
    private String agentManagerHost;
    
    @Autowired
    private AgentService agentService;

    @RequestMapping(value = "/**")
    public void forwardRequest(
            HttpServletRequest request,
            HttpServletResponse response,            
            @CookieValue(value = "CSSID", defaultValue = "") String coreJSessionId,
            @CookieValue(value = "NSC_BHFODZ_WT", defaultValue = "") String tokenCookie) throws IOException {
        String baseUrl = request.getRequestURL().toString().replace(request.getPathInfo(),"") + "/agent/manager";
        String cookies ="";
        String requestPayload = "";        
        if(request.getRequestURI().matches(".*\\.(jpg|JPG|gif|GIF|png|css|ico|cache\\.html)$") && StringUtils.isNotBlank(request.getHeader("If-None-Match"))){
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        
        if(StringUtils.isNotBlank(coreJSessionId)){
            cookies = "JSESSIONID=" + coreJSessionId ; 
        }
        
        if(StringUtils.isNotBlank(tokenCookie)){
            cookies += ";NSC_BHFODZ_WT=" + tokenCookie;
        }
        
        if(StringUtils.isNotBlank(request.getContentType())){            
            requestPayload = IOUtils.toString(request.getInputStream(), StringUtils.defaultIfBlank(request.getCharacterEncoding(), "UTF-8"));
            if(requestPayload != null){
                requestPayload = requestPayload.replace(baseUrl, agentManagerURL);
                if(requestPayload.contains("com.leo.agency.ui.service.IUserLoginService|getLoginUser")){
                    requestPayload = populateLoginInfo(requestPayload);
                }
            }
        }else{
            requestPayload = request.getQueryString();
        }
                
        String url = request.getRequestURI().replaceAll(request.getContextPath() + "/web/agent/manager|"+ request.getContextPath() + "/web/agent", agentManagerURL);
        Map<String, String> forwardHeaders = getForwardHeaders(request);
        forwardHeaders.put("cookie", cookies);        
        
        ConnectionResponse connectionResponse = ConnectionHelper.connectToUrl(url, request.getMethod() , requestPayload , forwardHeaders);        
        
        for (Map.Entry<String, String> entry : connectionResponse.getHeaders().entrySet()) {
            if(!StringUtils.containsIgnoreCase("set-cookie,content-encoding,Transfer-Encoding", entry.getKey())){
                response.setHeader(entry.getKey(), entry.getValue());
            }
        }        
        for (String cookie : connectionResponse.getCookies()) {
            response.addHeader("Set-Cookie", ConnectionHelper.extractCookieValues(cookie.replace("JSESSIONID", "CSSID")) + "Path=/");
        }
        response.setHeader("Content-Length", connectionResponse.getData().length + "");
        
        if(connectionResponse.getStatus() != 304){
            IOUtils.write(connectionResponse.getData(), response.getOutputStream());
        }
        
        if(connectionResponse.getStatus() != 200){
            response.setStatus(connectionResponse.getStatus() );
        }
    }
    
    
    public Map<String,String> getForwardHeaders(HttpServletRequest request){
        String baseUrl = request.getRequestURL().toString().replace(request.getPathInfo(),"") + "/agent/manager";
        Map<String, String> headers = new HashMap<String, String>();
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String name : headerNames) {
            headers.put(name, request.getHeader(name));
        }
        headers.put("host", agentManagerHost);
        if(StringUtils.isNotBlank(headers.get("referer"))){
            headers.put("referer", agentManagerURL);
        }
        if(StringUtils.isNotBlank(headers.get("x-gwt-module-base"))){
            headers.put("x-gwt-module-base", headers.get("x-gwt-module-base").replace(baseUrl, agentManagerURL));
        }
        headers.put("origin", "http://" + agentManagerHost);
        return headers;
    }

    @RequestMapping(value = "/manager")
    @Authorization(hasAnyPermission={Permissions.LaunchAgentAdminPortal})
    public Object managerSite(
            HttpServletRequest request, 
            HttpServletRequest response,
            @RequestParam(value= "agentCode" , defaultValue="") String agentCode            
            ) {
        
        
        request.setAttribute("agentCode", "_agentCode_");
        request.setAttribute("password", "__agentPassword");
        return ("/agent/managersite");
    }

    private String populateLoginInfo(String requestPayload) {
        String agentCode = StringUtils.substringBetween(requestPayload, "_agentCode_", "|");
        if(StringUtils.isBlank(agentCode)) return requestPayload;
        
        Agent agent = agentService.getByAgentCode(agentCode);
        if(agent!= null){
            requestPayload = requestPayload.replace("_agentCode_" + agentCode, agentCode);
            requestPayload = requestPayload.replace("__agentPassword", agent.getCredential());
        }
        return requestPayload;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.utils.ResponseUtils;
import com.pr7.modelsb.constant.AgentStatus;
import com.pr7.sb.service.AgentCasinoService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.util.IPAddressUtils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@RequestMapping("/sv/casino-agent")
@Authentication(type={UserType.BO})
@Authorization(hasAnyPermission={Permissions.ListAgent, Permissions.EditAgent, Permissions.LaunchAgentAdminPortal})
public class CasinoAgentService {

    private static final Logger _logger = LogManager.getLogger(CasinoAgentService.class);

    @Autowired
    AgentCasinoService agentCasinoService;

    @Value("${ptech.url.agentportal}")
    String playtechAgentPortalUrl;


    @RequestMapping("/searchCSPtAgent.sv")
    @ResponseBody
    public Object getPtechCasinoAgent(
            @RequestParam(defaultValue = "") String agentCode,
            @RequestParam(defaultValue = "") String currencyCode,
            @RequestParam(defaultValue = "") String status
    ) throws Exception{
        if(StringUtils.isBlank(status)){
            return agentCasinoService.getPtechAgents(agentCode, currencyCode);
        }
        AgentStatus st;

        try {
            st = AgentStatus.parse(Integer.valueOf(status));
        } catch (Exception exception) {
            throw new Exception("Invalid Status: " + status);
        }

        return agentCasinoService.getPtechAgents(agentCode,currencyCode, st);
    }

    @RequestMapping("/loginToPtechAgentPortal.sv")
    @ResponseBody
    @Authorization(hasAnyPermission={Permissions.LaunchAgentAdminPortal})
    public Object getPtechCasinoAgent(
            HttpServletResponse response,
            @RequestParam(defaultValue = "") String agentCode
    ) throws Exception{
        String ptechAdminCookies = agentCasinoService.getPtechAdminCookies(agentCode);
        if(StringUtils.isNotBlank(ptechAdminCookies)){
            Map<String, Cookie> cookies = ResponseUtils.getCookiesFromString(ptechAdminCookies, false);
            if(playtechAgentPortalUrl.matches("^http://.*|^https://.*")) {
                String pth = playtechAgentPortalUrl.replaceAll("http://|https://", "");
                String[] split = pth.split("/");
                if(split.length > 0){
                    String domain = "";
                    if(!IPAddressUtils.isValidIP4Adress(split[0].split(":")[0])){
                        String[] parts = split[0].split("\\.");
                        if(parts.length > 2){
                            parts[0] = "";
                            domain = StringUtils.join(parts,".");
                        }
                    }

                    Set<Entry<String, Cookie>> entrySet = cookies.entrySet();
                    for (Map.Entry<String, Cookie> entry : entrySet) {
                        if(StringUtils.isNotBlank(domain)){
                            domain = domain.split(":")[0];
                            entry.getValue().setDomain(domain);
                        }
//                        if(playtechAgentPortalUrl.startsWith("https")) entry.getValue().setSecure(true);
                        entry.getValue().setPath("/");
                    }
                }
            }
            for (Cookie c : cookies.values()) {
                response.addCookie(c);
            }
        }
        return playtechAgentPortalUrl;
    }
}

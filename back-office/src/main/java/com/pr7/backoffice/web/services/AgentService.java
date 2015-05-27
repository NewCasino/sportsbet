package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.utils.ResponseUtils;
import com.pr7.modelsb.dto.AgentDto;
import com.pr7.modelsb.entity.Agent;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.sb.cache.CacheMgr;
import com.pr7.sb.constant.SmartBLang;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/sv/agent")
@Authentication(type={UserType.BO})
@Authorization(hasAnyPermission={Permissions.ListAgent, Permissions.EditAgent})
public class AgentService {

    @Autowired
    com.pr7.sb.service.AgentService agentService;

    @Autowired
    CacheMgr cacheMgr;

    @RequestMapping("/searchAgent.sv")
    @ResponseBody
    public Object searchMappingReportHandler(@RequestParam(defaultValue = "") String agentCode) {
        List<AgentDto> result = new ArrayList<AgentDto>();
        List<Agent> agents;

        if (StringUtils.isBlank(agentCode)) {
            agents = agentService.findAll();
        } else {
            agents = Arrays.asList(agentService.getByAgentCode(agentCode));
        }

        for (Agent agent : agents) {
            if (agent == null) {
                continue;
            }
            AgentDto agentDto = new AgentDto();
            agentDto.copyFromEntity(agent);
            result.add(agentDto);
        }

        return result;
    }

    @RequestMapping("/loginToAdminPortal.sv")
    @ResponseBody
    @Authorization(hasAnyPermission={Permissions.LaunchAgentAdminPortal})
    public Object loginToAdminPortalHandler(@RequestParam(defaultValue = "") String agentCode, HttpServletResponse response) {
        String cookieStr = cacheMgr.getAgentCookiesBO(agentCode, SmartBLang.MGRSITE_EN.code());
        //cookies = cookies.replaceAll(";\\s*path\\=.?;", "; path=/bwmanager/;");
        Map<String, Cookie> cookies = ResponseUtils.getCookiesFromString(cookieStr, false);
        for (Cookie c : cookies.values()) {
            c.setPath("/");
            response.addCookie(c);
        }
        return null;
    }

    @RequestMapping("/update.sv")
    @ResponseBody
    @Authorization(hasAllPermission={Permissions.EditAgent})
    public Object updateHandler(@RequestParam("agent") String agentJson, @RequestParam("oldPW") String oldPW) {
        Agent agent = new JSONDeserializer<Agent>().deserialize(agentJson.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.Agent\" }"));
        return agentService.update(agent, oldPW);
    }

    @RequestMapping("/create.sv")
    @ResponseBody
    @Authorization(hasAllPermission={Permissions.EditAgent})
    public Object createHandler(@RequestParam("agent") String agentJson) {
        Agent agent = new JSONDeserializer<Agent>().deserialize(agentJson.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.Agent\" }"));
        agent = agentService.create(agent);
        AgentDto agentDto = new AgentDto();
        agentDto.copyFromEntity(agent);
        return agentDto;
    }
}

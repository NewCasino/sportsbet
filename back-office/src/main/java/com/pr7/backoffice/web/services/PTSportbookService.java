/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.modelsb.constant.CoreSiteId;
import com.pr7.modelsb.constant.MemberStatus;
import com.pr7.modelsb.constant.SportIdEnum;
import com.pr7.modelsb.dto.SbCustomPTDto;
import com.pr7.modelsb.entity.Agent;
import com.pr7.modelsb.entity.SbCustomPT;
import com.pr7.modelsb.entity.SportLeague;
import com.pr7.sb.api.cache.SBApiCacheMgr;
import com.pr7.sb.api.processor.agent.SBApiAgentProcessor;
import com.pr7.sb.constant.Constants;
import com.pr7.sb.constant.SmartBLang;
import com.pr7.sb.dao.SbCustomPTDAO;
import com.pr7.sb.dao.SportLeagueDAO;
import com.pr7.sb.processor.agent.model.AgentInfo;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.server.session.UserSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author NTI
 */
@Controller("PTSportbook")
@RequestMapping("/sv/pt-sportbook")
@Authentication(type= UserType.BO)
public class PTSportbookService {
    
    private static final Logger _logger = LogManager.getLogger(PTSportbookService.class);
    
    @Autowired
    private SbCustomPTDAO sbCustomPTDAO;
    @Autowired
    private SportLeagueDAO sportLeagueDAO;
    @Autowired
    private SBApiAgentProcessor sBApiAgentProcessor;
    @Autowired
    private SBApiCacheMgr cacheMgr;
    @Autowired
    private com.pr7.sb.service.AgentService agentService;
    @Autowired
    private UserSession userSession;
    
    @ResponseBody
    @RequestMapping("/getAllLeague.sv")
    @Authorization(hasAnyPermission={Permissions.ListPTSportbook})
    public Object getAllLeague (){
        List<SportLeague> sportLeagues = sportLeagueDAO.getSportLeagueBySportId(1);
        return sportLeagues;
    }

    @ResponseBody
    @RequestMapping("/getAllPTSportbook")
    @Authorization(hasAnyPermission={Permissions.ListPTSportbook})
    public Object getAllPTSportbook(
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(defaultValue = "0") int rows
            ){
        
        List<SbCustomPTDto> sbCustomPTDtos = sbCustomPTDAO.getSbCustomPTAll();
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(page, rows);
        Map<Object,Object> result=new HashMap<Object, Object>();
        paginateRecordJSON.setUserdata(result);
        paginateRecordJSON.setRecords(sbCustomPTDtos.toArray());
        return paginateRecordJSON;
    }
    
    @ResponseBody
    @RequestMapping("/createPTSportbook")
    @Authorization(hasAnyPermission={Permissions.EditPTSportbook})
    public Object createPTSportbook (
            @RequestParam(defaultValue="0") int leagueName,
            @RequestParam(defaultValue="80") int ptValue,
            @RequestParam(defaultValue="0") String id
            ) throws Exception{
//        Agent agent = agentService.getAvailableAgentByCurrency("xxch21010111", CoreSiteId.API_EN); 
        
        List<Agent> agentList = agentService.findBySiteId(CoreSiteId.API_EN.value(), MemberStatus.ACTIVE.value());
        for (Agent agent: agentList){
            AgentInfo agentInfo = cacheMgr.getAgentInfoWithPreLogin(agent.getAgentcode(), SmartBLang.MGRSITE_EN);
            boolean status = sBApiAgentProcessor.setSportLeaguePT(agentInfo,SportIdEnum.SOCCER.getValue(), leagueName, "", ptValue);

            Map<String,String> result = new HashMap();
            if (status) {
                SbCustomPT sbCustomPt = new SbCustomPT();
                sbCustomPt.setLeagueId(leagueName);
                sbCustomPt.setPtValue(ptValue);
                sbCustomPt.setLastUpdateBy(userSession.getLoginId());
                if (!id.equals("_empty")){
                    sbCustomPt.setId(Long.valueOf(id));
                    sbCustomPTDAO.update(sbCustomPt);
                }else{
                    sbCustomPTDAO.create(sbCustomPt);
                }

                result.put("success", "1");
            }
            return result;
        }
        return null;
        
    }
}

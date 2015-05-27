/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.backoffice.web.json.PaginateRecordJSON;
import com.pr7.modelsb.constant.CommonStatus;
import com.pr7.modelsb.constant.ExtProductVendor;
import com.pr7.modelsb.constant.RebateTerm;
import com.pr7.modelsb.constant.RebateType;
import com.pr7.modelsb.constant.SubProductId;
import com.pr7.modelsb.dto.MemberDto;
import com.pr7.modelsb.dto.RebateSettingDto;
import com.pr7.modelsb.dto.RebateSettingFilter;
import com.pr7.modelsb.entity.Member;
import com.pr7.newkeno.services.NewKenoServiceImpl;
import com.pr7.sb.dao.RebateSettingDAO;
import com.pr7.sb.service.MemberService;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import com.pr7.server.session.UserSession;
import com.pr7.util.DateUtil;
import java.math.BigDecimal;
import java.util.Date;
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
 * @author zakky
 */
@Controller("RebateSettingController")
@RequestMapping("/sv/rebate")
@Authentication(type = UserType.BO)
public class RebateSettingService {
    
    private static final Logger logger = LogManager.getLogger(NewKenoServiceImpl.class);
    
    @Autowired
    private RebateSettingDAO rebateSettingDAO;
    @Autowired
    UserSession userSession;
    @Autowired
    MemberService memberService;
    
    @ResponseBody
    @RequestMapping("/searchRebateSetting.sv")
    @Authorization(hasAnyPermission = {Permissions.ListRebateSetting})
    public Object searchRebateSetting(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue = "") Long startDate,
            @RequestParam(defaultValue = "") Long endDate,
            @RequestParam(defaultValue = "") String product,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "NONE") SubProductId subProductId
            ){
        Date start = new Date(startDate);
        Date end = new Date(endDate);
        RebateSettingFilter rebateSettingFilter = new RebateSettingFilter();
        rebateSettingFilter.setFromDate(start);
        rebateSettingFilter.setToDate(end);
        rebateSettingFilter.setProduct(ExtProductVendor.parseFromCode(product));
        rebateSettingFilter.setCommonStatus(CommonStatus.parseFromName(status));
        rebateSettingFilter.setRebateType(RebateType.NONE);
        rebateSettingFilter.setSubProductId(subProductId);
        
        List<RebateSettingDto> rebateSetting = rebateSettingDAO.getByFilter(rebateSettingFilter);
        
        for (RebateSettingDto rebateSettingDto:rebateSetting){
            if (rebateSettingDto.getRebateType()==RebateType.MEMBER){
                MemberDto memberDto  = memberService.getById(rebateSettingDto.getRefId());
                if (memberDto!=null){
                    rebateSettingDto.setMemberCode(memberDto.getMemberCode());
                }
            }
        }
        
        PaginateRecordJSON paginateRecordJSON = new PaginateRecordJSON(page, rows);
        Map<Object,Object> result=new HashMap<Object, Object>();
        paginateRecordJSON.setUserdata(result);
        paginateRecordJSON.setRecords(rebateSetting.toArray());
        
        return paginateRecordJSON;
    }
    
    @ResponseBody
    @RequestMapping("/updateRebateSetting.sv")
    @Authorization(hasAnyPermission = {Permissions.EditRebateSetting})
    public Object updateRebateSetting( 
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int rows,
            @RequestParam(defaultValue = "") long startDate,
            @RequestParam(defaultValue = "") long endDate,
            @RequestParam(defaultValue = "") String product,
            @RequestParam(defaultValue = "") int commonStatus,
            @RequestParam(defaultValue = "") int rebateTerm,
            @RequestParam(defaultValue = "") BigDecimal value,
            @RequestParam(defaultValue = "") int rebateType,
            @RequestParam(defaultValue = "") String oper,
            @RequestParam(defaultValue = "") String id,
            @RequestParam(defaultValue = "") String refId,
            @RequestParam(defaultValue = "") String dateCreated,
            @RequestParam(defaultValue = "") SubProductId subProductId
            ) throws Exception{

        RebateSettingDto dto = new RebateSettingDto();
        
        Map<Object,Object> result=new HashMap<Object, Object>();
        Member member = null;
        Date fromDate = new Date(startDate);
        Date toDate = new Date(endDate);
        
        long refL = 0;
        if (id.equals("_empty")){
            id = "0";
        }
        
        if (rebateType == 3){
            member = memberService.getByMemberCode(refId);
            if (member == null){
                result.put("success", 0);
                result.put("message","Member Code Not Found");
                return result;
            } else {
                refL = member.getId();
                //check is date overlapped 
                if (CommonStatus.ACTIVE == CommonStatus.parseFromValue(commonStatus)) {
                    if(!checkOverlappedDate(fromDate, toDate, ExtProductVendor.parseFromCode(product),subProductId, RebateType.MEMBER, refL,Long.valueOf(id))){
                        result.put("success", 0);
                        result.put("message", "Date is overlapped with existing Rebate Setting");
                        return result;
                    }
                }
            }
        } else if (rebateType == 1) {
            if (CommonStatus.ACTIVE == CommonStatus.parseFromValue(commonStatus)) {
                long tempId = 0;
                if (oper.equals("edit")){
                    tempId = Long.valueOf(id);
                }
                //check is date overlapped 
                if (!checkOverlappedDate(fromDate, toDate, ExtProductVendor.parseFromCode(product),subProductId, RebateType.parseFromValue(rebateType), tempId,Long.valueOf(id))) {
                    result.put("success", 0);
                    result.put("message", "Date is overlapped with existing Rebate Setting");
                    return result;
                }
            }
        } else {
            result.put("success", 0);
            result.put("message", "Not Supported Yet");
            return result;
        }
        
        //date validation
        if(toDate.compareTo(fromDate)<0){
            result.put("success", 0);
            result.put("message", "Start date is after End date");
            return result;
                    
        }
        
        //value validation; Term = Percenteg max Value = 20
        if(value.compareTo(BigDecimal.valueOf(20)) == 1 ){ 
            result.put("success", 0);
            result.put("message", "Max Value is 20");
            return result;
        }
        
       
        dto.setEndDate(toDate);
        dto.setCommonStatus(CommonStatus.parseFromValue(commonStatus));
        dto.setLastUpdateBy(userSession.getLoginId());
        dto.setRefId(refL);
        //sparate add and edit save action
        if (oper.equals("add")){
            
            dto.setDateCreated(new Date());
            dto.setStartDate(fromDate);
            dto.setValue(value.divide(BigDecimal.valueOf(100)));
            dto.setProduct(ExtProductVendor.parseFromCode(product));
            dto.setRebateTerm(RebateTerm.parseFromValue(rebateTerm));
            dto.setRebateType(RebateType.parseFromValue(rebateType));
            dto.setSubProductId(subProductId);
            
            
            rebateSettingDAO.create(dto);
            result.put("success", 1);
            return result;
        }else if(oper.equals("edit")){
            RebateSettingDto rebateSettingDtoTmp = rebateSettingDAO.getById(Long.valueOf(id));
            Date dateTmp = rebateSettingDtoTmp.getEndDate();

            //if rebate setting hasn't been referred from rebate_trans
            if ((Boolean) isUsedInRebateTrans(Long.valueOf(id)) == false) {
                dto.setStartDate(fromDate);
                dto.setValue(value.divide(BigDecimal.valueOf(100)));
                dto.setProduct(ExtProductVendor.parseFromCode(product));
                dto.setRebateTerm(RebateTerm.parseFromValue(rebateTerm));
                dto.setRebateType(RebateType.parseFromValue(rebateType));
                dto.setSubProductId(subProductId);
            }else {
                dto.setStartDate(rebateSettingDtoTmp.getStartDate());
                dto.setValue(rebateSettingDtoTmp.getValue());
                dto.setProduct(rebateSettingDtoTmp.getProduct());
                dto.setRebateTerm(rebateSettingDtoTmp.getRebateTerm());
                dto.setRebateType(rebateSettingDtoTmp.getRebateType());
                dto.setRefId(rebateSettingDtoTmp.getRefId());
                dto.setSubProductId(rebateSettingDtoTmp.getSubProductId());
                //check if input end date not earlier then before
                if (dto.getEndDate().before(dateTmp)){
                    result.put("success", 0);
                    result.put("message", "End Date cannot change to earlier date");
                    return result;
                }
            }
            
            dto.setId(Long.valueOf(id));
            dto.setDateCreated(rebateSettingDtoTmp.getDateCreated());
            rebateSettingDAO.update(dto);
            result.put("success", 1);
            return result;
        }
        
        return result;
    }
    
    @ResponseBody
    @RequestMapping("/searchRebateSettingId.sv")
    @Authorization(hasAnyPermission = {Permissions.EditRebateSetting, Permissions.ListRebateTransactions})
    public Object searchSettingById(@RequestParam(defaultValue = "0") long settingId) {
        RebateSettingDto rs = rebateSettingDAO.getById(settingId);
        rs.setMemberCode("-");
        switch(rs.getRebateType())
        {
            case MEMBER:
               MemberDto memberDto  = memberService.getById(rs.getRefId());
               rs.setMemberCode(memberDto.getMemberCode());break;
        }
        return rs;
    }

    boolean checkOverlappedDate(Date startDate,Date endDate, ExtProductVendor extProductVendor,SubProductId subProductId,RebateType rebateType,long refId,long id){
        Map<Object,Object> result=new HashMap<Object, Object>();
        List<RebateSettingDto> rebateSettingDtos1;
        List<RebateSettingDto> rebateSettingDtos2;
        List<RebateSettingDto> rebateSettingDtos3;
        RebateSettingFilter rebateSettingFilter = new RebateSettingFilter();
        rebateSettingFilter.setFromDate(startDate);
        rebateSettingFilter.setToDate(endDate);
        rebateSettingFilter.setProduct(extProductVendor);
        rebateSettingFilter.setCommonStatus(CommonStatus.ACTIVE);
        rebateSettingFilter.setSubProductId(subProductId);
        if (rebateType == RebateType.MEMBER){
            rebateSettingFilter.setRefId(refId);
            rebateSettingFilter.setRebateType(RebateType.MEMBER);
            rebateSettingDtos1 = rebateSettingDAO.getRebateSettingByEffectiveDate(startDate, extProductVendor,subProductId, RebateType.MEMBER, refId,id); 
            rebateSettingDtos2 = rebateSettingDAO.getRebateSettingByEffectiveDate(endDate, extProductVendor,subProductId, RebateType.MEMBER, refId,id);
            rebateSettingDtos3 = rebateSettingDAO.getByFilter(rebateSettingFilter,id);
            if ((rebateSettingDtos1.size() > 0)
                    || (rebateSettingDtos2.size() > 0) || (rebateSettingDtos3.size() > 0 )){
                return false;
            }
        }else{
            rebateSettingFilter.setRefId(0);
            rebateSettingFilter.setRebateType(rebateType);
            rebateSettingDtos1 = rebateSettingDAO.getRebateSettingByEffectiveDate(startDate, extProductVendor,subProductId, rebateType, 0l,id);
            rebateSettingDtos2 = rebateSettingDAO.getRebateSettingByEffectiveDate(endDate, extProductVendor, subProductId,rebateType, 0l,id);
            rebateSettingDtos3 = rebateSettingDAO.getByFilter(rebateSettingFilter,id);
            if ((rebateSettingDtos1.size() > 0)
                    || (rebateSettingDtos2.size() > 0 ) || (rebateSettingDtos3.size() > 0 )){
                return false;
            }
        }
        return true;
    }
    
    boolean notSelfId(List<RebateSettingDto> dtos,long id){
        if (dtos.size() == 1){
            if (dtos.get(0).getId() == id){
                return true;
            }
        }
        return false;
    }
    
    @ResponseBody
    @RequestMapping("/isUsedInRebateTrans.sv")
    @Authorization(hasAnyPermission = {Permissions.EditRebateSetting})
    Object isUsedInRebateTrans(@RequestParam(defaultValue = "") Long id){
        Map<Object,Object> result=new HashMap<Object, Object>();
        boolean isUsed;
        isUsed = rebateSettingDAO.isUsedInRebateTrans(id);
        return isUsed;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web.services;

import com.pr7.modelsb.entity.ScheduleLog;
import java.util.Date;
import java.util.List;
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
@RequestMapping("/sv/schedule")
public class ScheduleLogService {
    
    @Autowired
    private com.pr7.sb.service.ScheduleLogService scheduleLogService;
    
    @RequestMapping("/findScheduleLog.sv")
    @ResponseBody
    public List<ScheduleLog> findScheduleLog(
            @RequestParam(defaultValue="") String jobName, 
            @RequestParam(defaultValue="") String status, 
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate) {
        return scheduleLogService.getScheduleLogListByFilter(status, jobName, new Date(startDate), new Date(endDate));
    }
    
}

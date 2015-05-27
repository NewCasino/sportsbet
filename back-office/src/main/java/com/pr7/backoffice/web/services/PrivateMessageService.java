package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.modelsb.constant.PrivateMessageStatus;
import com.pr7.modelsb.entity.PrivateMessage;
import com.pr7.server.common.Authentication;
import com.pr7.server.common.Authorization;
import com.pr7.server.constants.UserType;
import flexjson.JSONDeserializer;
import java.util.Date;
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
@RequestMapping("/sv/privatemsg")
@Authentication(type={UserType.BO})
@Authorization(hasAnyPermission={Permissions.ListPrivateMessage, Permissions.EditPrivateMessage})
public class PrivateMessageService {

    @Autowired
    com.pr7.sb.service.PrivateMessageService privateMessageService;

    @RequestMapping("/search.sv")
    @ResponseBody
    public Object searchHandler(
            String receiver, 
            @RequestParam(defaultValue="") Long startDate, 
            @RequestParam(defaultValue="") Long endDate, 
            @RequestParam(defaultValue = "ACTIVE") String status) {
        return privateMessageService.find(receiver, new Date(startDate), new Date(endDate), PrivateMessageStatus.valueOf(status));
    }

    @RequestMapping("/update.sv")
    @ResponseBody
    @Authorization(hasAllPermission={Permissions.EditPrivateMessage})
    public Object updateHandler(@RequestParam("message") String messageJson) {
        PrivateMessage message = new JSONDeserializer<PrivateMessage>().deserialize(messageJson.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.PrivateMessage\" }"));
        return privateMessageService.update(message);
    }

    @RequestMapping("/new.sv")
    @ResponseBody
    @Authorization(hasAllPermission={Permissions.EditPrivateMessage})
    public Object createHandler(@RequestParam("message") String messageJson) {
        PrivateMessage message = new JSONDeserializer<PrivateMessage>().deserialize(messageJson.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.PrivateMessage\" }"));
        return privateMessageService.create(message);
    }
}

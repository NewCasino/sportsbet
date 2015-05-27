package com.pr7.backoffice.web.services;

import com.pr7.backoffice.constants.Permissions;
import com.pr7.constant.Language;
import com.pr7.modelsb.constant.AnnouncementCategory;
import com.pr7.modelsb.constant.AnnouncementStatus;
import com.pr7.modelsb.entity.Announcement;
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
@RequestMapping("/sv/announcement")
@Authentication(type={UserType.BO})
@Authorization(hasAnyPermission={Permissions.ListAnnouncement, Permissions.EditAnnouncement})
public class AnnouncementService {

    @Autowired
    com.pr7.sb.service.AnnouncementService announcementService;

    @RequestMapping("/search.sv")
    @ResponseBody
    public Object searchHandler(
            Long startDate, 
            Long endDate, 
            @RequestParam(defaultValue = "SPORTSBOOK") String category, 
            @RequestParam(defaultValue = "ACTIVE") String status, 
            @RequestParam(defaultValue = "en-gb") String lang) {
        return announcementService.find(new Date(startDate), new Date(endDate), AnnouncementStatus.valueOf(status), AnnouncementCategory.valueOf(category), Language.getLanguageByNamedValue(lang));
    }

    @RequestMapping("/update.sv")
    @ResponseBody
    @Authorization(hasAllPermission={Permissions.EditAnnouncement})
    public Object updateHandler(@RequestParam("announcement") String announcementJson) {
        Announcement announcement = new JSONDeserializer<Announcement>().deserialize(announcementJson.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.Announcement\" }"));
        return announcementService.update(announcement);
    }

    @RequestMapping("/create.sv")
    @ResponseBody
    @Authorization(hasAllPermission={Permissions.EditAnnouncement})
    public Object createHandler(@RequestParam("announcement") String announcementJson) {
        Announcement announcement = new JSONDeserializer<Announcement>().deserialize(announcementJson.replaceFirst("}\\s*$", ", \"class\": \"com.pr7.modelsb.entity.Announcement\" }"));
        return announcementService.create(announcement);
    }
}

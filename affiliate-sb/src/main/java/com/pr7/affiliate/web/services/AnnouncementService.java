package com.pr7.affiliate.web.services;

import com.pr7.constant.Language;
import com.pr7.modelsb.constant.AnnouncementCategory;
import com.pr7.modelsb.constant.AnnouncementStatus;
import com.pr7.modelsb.entity.Announcement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
@RequestMapping("/sv/announcement")
public class AnnouncementService {

    @Autowired
    com.pr7.sb.service.AnnouncementService announcementService;

    @RequestMapping("/list")
    @ResponseBody
    public Object searchHandler(
            @RequestParam(defaultValue = "en-gb") String langCode) {
        return sort(announcementService.find(AnnouncementStatus.ACTIVE, AnnouncementCategory.AFFILIATE, Language.getLanguageByNamedValue(langCode)));
    }
    
    private List<Announcement> sort(List<Announcement> list) {
        Collections.sort(list, new Comparator<Announcement>() {
            @Override
            public int compare(Announcement o1, Announcement o2) {
                if (o1.getSequence() != o2.getSequence()) {
                    return o1.getSequence() < o2.getSequence() ? 1 : -1;
                }
                return o1.getDateCreated().compareTo(o2.getDateCreated());
            }
        });
        return list;
    }
}

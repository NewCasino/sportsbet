package com.pr7.affiliate.web.services;

import com.pr7.affiliate.session.UserSession;
import com.pr7.base.dao.Page;
import com.pr7.modelsb.entity.AffiliateComment;
import com.pr7.sb.constant.Constants;
import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("/sv/comment")
public class AffiliateCommentService {
    
    @Autowired
    UserSession userSession;

    @Autowired
    com.pr7.sb.service.AffiliateCommentService affiliateCommentService;

    @RequestMapping("/list")
    @ResponseBody
    public Object searchHandler(
            @RequestParam(defaultValue="1", value="pageNumber") int pageNumber,
            @RequestParam(defaultValue="10", value="pageSize") int pageSize
            ) {
        
        final Page<AffiliateComment> result = affiliateCommentService.find(userSession.getUserId(), pageNumber, pageSize);
        List<Long> commentIds = new ArrayList<Long>();
        for (AffiliateComment comment : result) {
            commentIds.add(comment.getId());
        }
        final List<AffiliateComment> replies = affiliateCommentService.findReplies(commentIds);
        
        return new HashMap() {{
            put("comments", result);
            put("replies", replies);
            put("pageNumber", result.getPageNumber());
            put("pageSize", result.getPageSize());
            put("totalPages", result.getTotalPages());
        }};
    }
    
    @RequestMapping("/put")
    @ResponseBody
    public Object putHandler(
            String title,
            String content) {
        AffiliateComment comment = new AffiliateComment();
        comment.setTitle(title);
        comment.setContent(content);
        comment.setSenderId(userSession.getUserId());
        comment.setSenderName(userSession.getLoginId());
        comment.setReceiverId(Constants.AFFILIATE_OFFICER_RECEIVER_ID);
        
        return affiliateCommentService.create(comment);
    }
}

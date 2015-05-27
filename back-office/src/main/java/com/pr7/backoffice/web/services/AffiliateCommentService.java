package com.pr7.backoffice.web.services;

import com.pr7.base.dao.Page;
import com.pr7.modelsb.entity.AffiliateComment;
import com.pr7.sb.constant.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    com.pr7.sb.service.AffiliateCommentService affiliateCommentService;

    @RequestMapping("/search")
    @ResponseBody
    public Object searchHandler(
            long startDate,
            long endDate,
            @RequestParam(defaultValue="1", value="pageNumber") int pageNumber,
            @RequestParam(defaultValue="10", value="pageSize") int pageSize
            ) {
        
        final Page<AffiliateComment> result = affiliateCommentService.find(new Date(startDate), new Date(endDate), pageNumber, pageSize);
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
    
    @RequestMapping(value="/reply", method={ RequestMethod.POST })
    @ResponseBody
    public Object replyHandler(
            long receiverId,
            long replyTo,
            String title,
            String content) {
        AffiliateComment comment = new AffiliateComment();
        comment.setReplyTo(replyTo);
        comment.setTitle(title);
        comment.setContent(content);
        comment.setSenderId(Constants.AFFILIATE_OFFICER_RECEIVER_ID);
        comment.setSenderName(Constants.AFFILIATE_OFFICER_RECEIVER_NAME);
        comment.setReceiverId(receiverId);
        
        return affiliateCommentService.create(comment);
    }
    
    @RequestMapping(value="/edit", method={ RequestMethod.POST })
    @ResponseBody
    public Object modifyHandler(
            long id,
            String title,
            String content) {
        AffiliateComment comment = affiliateCommentService.getById(id);
        comment.setTitle(title);
        comment.setContent(content);
        
        return affiliateCommentService.update(comment);
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public Object deleteHandler(
            long id) {
        AffiliateComment comment = affiliateCommentService.getById(id);
        return affiliateCommentService.delete(comment);
    }
}

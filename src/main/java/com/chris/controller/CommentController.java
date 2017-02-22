package com.chris.controller;

import com.chris.model.Comment;
import com.chris.model.EntityType;
import com.chris.model.HostHolder;
import com.chris.service.CommentService;
import com.chris.service.QuestionService;
import com.chris.util.WendaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YaoQi on 2017/2/22.
 */
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(path = "/addComment", method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(WendaUtils.ANONYMOUS_USERID);
            }
            commentService.addComment(comment);

            int count = commentService.getCommentCount(questionId, EntityType.ENTITY_QUESTION);
            System.out.println(count);
            questionService.updateCommentCount(questionId, count);
        } catch (Exception e) {
            logger.error("Failed to add comment " + e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }
}

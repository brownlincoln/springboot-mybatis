package com.chris.controller;


import com.chris.async.EventModel;
import com.chris.async.EventProducer;
import com.chris.async.EventType;
import com.chris.model.Comment;
import com.chris.model.EntityType;
import com.chris.model.HostHolder;
import com.chris.model.User;
import com.chris.service.CommentService;
import com.chris.service.LikeService;
import com.chris.service.UserService;
import com.chris.util.WendaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by YaoQi on 2017/2/23.
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId, @CookieValue("ticket") String ticket) {
/*        if(ticket != null) {
            logger.info(ticket);
        }*/
        User curUser = hostHolder.getUser();
        if (curUser == null) {
           // return WendaUtils.getJSONString(999, "请登录后操作");
            curUser = userService.getUser(WendaUtils.SYSTEM_USERID);
        }
        Comment comment = commentService.getCommentById(commentId);

        //生产事件
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(curUser.getId()).setEntityOwnerId(comment.getUserId())
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityId(commentId)
                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(curUser.getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtils.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = "/dislike", method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        User curUser = hostHolder.getUser();
        if (curUser == null) {
            return WendaUtils.getJSONString(999, "请登录后操作");
        }
        Comment comment = commentService.getCommentById(commentId);

        long likeCount = likeService.dislike(curUser.getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtils.getJSONString(0, String.valueOf(likeCount));
    }
}

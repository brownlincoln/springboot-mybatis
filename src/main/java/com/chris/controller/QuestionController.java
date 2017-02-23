package com.chris.controller;

import com.chris.dao.QuestionDAO;
import com.chris.model.*;
import com.chris.service.CommentService;
import com.chris.service.LikeService;
import com.chris.service.QuestionService;
import com.chris.service.UserService;
import com.chris.util.WendaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YaoQi on 2017/2/21.
 */
@Controller
public class QuestionController {
    private static Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
            question.setCreatedDate(date);
            question.setCommentCount(0);

            if (hostHolder.getUser() == null) {
                //question.setUserId(WendaUtils.ANONYMOUS_USERID);
                question.setUserId(3);
            }
            question.setUserId(hostHolder.getUser().getId());
            if(questionService.addQuestion(question) > 0) {
                return WendaUtils.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("增加问题失败", e.getMessage());
        }
        return WendaUtils.getJSONString(1, "增加问题失败");
    }

    @RequestMapping(value = "/question/{qid}", method = RequestMethod.GET)
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);

        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION, 0, 10);
        List<ViewObject> comments = new ArrayList<>();


        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);

            //得到当前用户对这条评论的态度
            User curUser = hostHolder.getUser();
            if (curUser == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(curUser.getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }

            //得到喜欢的总数量
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
        return "detail";
    }
}


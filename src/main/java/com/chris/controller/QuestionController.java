package com.chris.controller;

import com.chris.dao.QuestionDAO;
import com.chris.model.HostHolder;
import com.chris.model.Question;
import com.chris.service.QuestionService;
import com.chris.util.WendaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}


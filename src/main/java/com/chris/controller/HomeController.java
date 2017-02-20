package com.chris.controller;

import com.chris.model.Question;
import com.chris.model.User;
import com.chris.model.ViewObject;
import com.chris.service.QuestionService;
import com.chris.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YaoQi on 2017/2/19.
 */
@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String index(Model model) {

        List<Question> questionList = questionService.getLatestQuestions(1, 0, 5);
//        System.out.println(user);
//        System.out.println(questionList.size());

        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "index";
    }
}

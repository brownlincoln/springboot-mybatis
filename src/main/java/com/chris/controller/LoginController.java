package com.chris.controller;

import com.chris.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by YaoQi on 2017/2/19.
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password) {
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            } else return "redirect:/";
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "login";
        }
    }

    @RequestMapping(value = "/reglogin", method = RequestMethod.GET)
    public String regloginPage(Model model) {
        return "login";
    }



}

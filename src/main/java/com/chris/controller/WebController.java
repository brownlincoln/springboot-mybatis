package com.chris.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by YaoQi on 2017/2/18.
 */
//@Controller
public class WebController {

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String indexController(HttpSession session) {
        return "<h1>Hello wenda!<h1>" + session.getAttribute("msg2");
    }

    @RequestMapping(path = {"/profile/{groupName}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupName") String groupName,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "page", defaultValue = "1", required = true) int page) {
        return String.format("Profile page of group %s of user %d, his/her homepage is in page %d", groupName, userId, page);
    }

    @RequestMapping(path = "/vm")
    public String template(Model model) {
        model.addAttribute("value1", "template vm");
        return "home";
    }

    @RequestMapping(path = "/request")
    @ResponseBody
    public String request(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        StringBuilder sb = new StringBuilder();

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookie name:" + cookie.getName() + ", cookie value: " + cookie.getValue() + "<br/>");
            }
        }
        //Similar to Iterator, has two methods, it is replaced by Iterator now.
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ": " + request.getHeader(name) + "<br/>");
        }

        sb.append(request.getAuthType() + "<br/>");
        sb.append(request.getQueryString() + "<br/>");
        sb.append(request.getMethod() + "<br/>");

        Cookie cookie = new Cookie("mycookiename", "mycookievalue");
        response.addCookie(cookie);

        response.addHeader("newHeader", "newHeaderValue");
        return sb.toString();
    }

    @RequestMapping(path = "/redirect/{code}", method = RequestMethod.GET)
    public RedirectView redirect(Model model, HttpSession session, @PathVariable("code") int code) {
        session.setAttribute("msg2", "message from redirect/" + code);
        //redirect to the homepage
        RedirectView redview = new RedirectView("/", true);
        //If there is no cache in disk, the session will be null.
        if (code == 301) {
            redview.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redview;
    }

    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if (key.equals("admin")) {
            return "hello world";
        }
        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e.getMessage();
    }
}

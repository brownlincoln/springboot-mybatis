package com.chris.service;

import com.chris.dao.LoginTicketDAO;
import com.chris.dao.UserDAO;
import com.chris.model.LoginTicket;
import com.chris.model.User;
import com.chris.util.WendaUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by YaoQi on 2017/2/19.
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        User user = userDAO.selectById(id);
        return user;
    }

    //Get user by name
    public User getUser(String username) {
        User user = userDAO.selectByName(username);
        return user;
    }

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)) {
            map.put("msg", "错误：用户名为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "错误：密码为空！");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已经被注册！");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(WendaUtils.md5(password + user.getSalt()));
        String headUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(headUrl);

        userDAO.addUser(user);

        //登录
        String ticket = addLoginTicket(getUser(username).getId());
        //String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)) {
            map.put("msg", "错误：用户名为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "错误：密码为空！");
            return map;
        }
        User user = userDAO.selectByName(username);

        if(!username.equals(user.getName())
                || !WendaUtils.md5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "用户名或者密码不正确！");
            return map;
        }
        //Each time login, store a temp ticket(token) for this session, which will expire soon
        int userId = getUser(username).getId();
        String ticket = addLoginTicket(userId);
        map.put("ticket", ticket);
        map.put("userId", userId);
        return map;
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

    //Add token
    public String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        Date date = new Date();
        //Cookie valid for 100 days
        date.setTime(3600*24*100 + date.getTime());
        DateFormat formatter = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
        ticket.setExpired(formatter.format(date));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();

    }

}

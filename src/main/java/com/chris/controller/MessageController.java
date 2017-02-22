package com.chris.controller;

import com.chris.model.HostHolder;
import com.chris.model.Message;
import com.chris.model.User;
import com.chris.model.ViewObject;
import com.chris.service.MessageService;
import com.chris.service.UserService;
import com.chris.util.WendaUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YaoQi on 2017/2/22.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/msg/addMessage", method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            //If the user doesn't login
            User currentUser = hostHolder.getUser();
            if (currentUser == null) {
                return WendaUtils.getJSONString(999, "用户没有登录，不能发送消息");
            }
            Message message = new Message();
            message.setContent(content);
            message.setFromId(currentUser.getId());
            User toUser = userService.getUser(toName);
            if (toUser == null) {
                return WendaUtils.getJSONString(1, "要发送消息的对象不存在，请确认该用户存在");
            }
            message.setToId(toUser.getId());
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            //Note that the conversationId is not set here, instead is computated.
            messageService.addMessage(message);
            return WendaUtils.getJSONString(0);
        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtils.getJSONString(1, "发送消息失败");
        }
    }

    //This method display the message page of the logined user.
    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String getConversationList(Model model) {
        User curUser = hostHolder.getUser();
        if (curUser == null) {
            return "redirect:/reglogin";
        }

        List<Message> messages = messageService.getConversationList(curUser.getId(), 0, 10);
        List<ViewObject> conversations = new ArrayList<>();
        for (Message message : messages) {
            int targetId = message.getFromId() == curUser.getId() ? message.getToId() : message.getFromId();
            ViewObject vo = new ViewObject();
            vo.set("user", userService.getUser(targetId));
            vo.set("message", message);
            int unread = messageService.getConversationUnreadCount(curUser.getId(), message.getConversationId());
            vo.set("unread",unread);
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);
        return "letter";
    }

    //Display the detail of each conversation_id, namely between every two users
    @RequestMapping(path = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(Model model, @Param("conversationId") String conversationId) {
        try {
            User curUser = hostHolder.getUser();
            if (curUser == null) {
                return "redirect:/reglogin";
            }

            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取消息详情失败" + e.getMessage());
        }
        return "letterDetail";
    }
}


package com.chris.service;

import com.chris.dao.MessageDAO;
import com.chris.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/22.
 */
@Service
public class MessageService {
    @Autowired
    private SensitiveService sensitiveService;

    @Autowired
    private MessageDAO messageDAO;

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));

        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount( int userId, String conversationId) {
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }



}

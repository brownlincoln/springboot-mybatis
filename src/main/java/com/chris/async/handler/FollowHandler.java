package com.chris.async.handler;

import com.chris.async.EventHandler;
import com.chris.async.EventModel;
import com.chris.async.EventType;
import com.chris.model.EntityType;
import com.chris.model.Message;
import com.chris.model.User;
import com.chris.service.MessageService;
import com.chris.service.UserService;
import com.chris.util.WendaUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by YaoQi on 2017/2/25.
 */
public class FollowHandler implements EventHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtils.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        int type = model.getEntityType();
        if (type == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
                + "关注了你的问题，http://127.0.0.1:8080/question/" + model.getEntityId());
        } else if (type == EntityType.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你，http://127.0.0.1:8080/user/" + model.getActorId());
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}

package com.chris.async.handler;

import com.chris.async.EventHandler;
import com.chris.async.EventModel;
import com.chris.async.EventType;
import com.chris.model.Message;
import com.chris.service.MessageService;
import com.chris.service.UserService;
import com.chris.util.WendaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by YaoQi on 2017/2/24.
 */
@Component
public class LikeHandler implements EventHandler {

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
        message.setContent("用户" + userService.getUser(model.getActorId()).getName() + "赞了你的评论," +
                "http://localhost:8080/question/" + model.getExt("questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
